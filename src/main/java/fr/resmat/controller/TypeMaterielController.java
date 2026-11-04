package fr.resmat.controller;

import fr.resmat.dto.TypeMaterielDto;
import fr.resmat.service.RegleMetierException;
import fr.resmat.service.TypeMaterielService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD des types de matériel — réservé au gestionnaire (voir SecurityConfig). */
@Controller
@RequestMapping("/gestionnaire/types")
public class TypeMaterielController {

    private static final String LISTE = "redirect:/gestionnaire/types";
    private static final String FORM = "typemateriel/form";

    private final TypeMaterielService service;

    public TypeMaterielController(TypeMaterielService service) {
        this.service = service;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("types", service.findAll());
        return "typemateriel/liste";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        preparerForm(model, new TypeMaterielDto(), "Nouveau type de matériel", "/gestionnaire/types");
        return FORM;
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("typeMateriel") TypeMaterielDto dto, BindingResult result,
                        Model model, RedirectAttributes redirect) {
        String titre = "Nouveau type de matériel", action = "/gestionnaire/types";
        if (result.hasErrors()) {
            return erreurForm(result, model, dto, titre, action);
        }
        try {
            service.create(dto);
        } catch (RegleMetierException e) {
            return erreurMetier(e, model, dto, titre, action);
        }
        redirect.addFlashAttribute("succes", "Type de matériel « " + dto.getLibelle().trim() + " » créé.");
        return LISTE;
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model) {
        preparerForm(model, service.findById(id), "Modifier le type de matériel", "/gestionnaire/types/" + id);
        return FORM;
    }

    @PostMapping("/{id}")
    public String mettreAJour(@PathVariable Long id, @Valid @ModelAttribute("typeMateriel") TypeMaterielDto dto,
                              BindingResult result, Model model, RedirectAttributes redirect) {
        String titre = "Modifier le type de matériel", action = "/gestionnaire/types/" + id;
        dto.setId(id);
        if (result.hasErrors()) {
            return erreurForm(result, model, dto, titre, action);
        }
        try {
            service.update(id, dto);
        } catch (RegleMetierException e) {
            return erreurMetier(e, model, dto, titre, action);
        }
        redirect.addFlashAttribute("succes", "Type de matériel modifié.");
        return LISTE;
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("succes", "Type de matériel supprimé.");
        } catch (RegleMetierException e) {
            redirect.addFlashAttribute("erreurGlobale", e.getMessage());
        }
        return LISTE;
    }

    private void preparerForm(Model model, TypeMaterielDto dto, String titre, String action) {
        model.addAttribute("typeMateriel", dto);
        model.addAttribute("titre", titre);
        model.addAttribute("action", action);
    }

    private String erreurForm(BindingResult result, Model model, TypeMaterielDto dto, String titre, String action) {
        preparerForm(model, dto, titre, action);
        if (result.hasFieldErrors("libelle")) {
            model.addAttribute("erreurLibelle", result.getFieldError("libelle").getDefaultMessage());
        }
        return FORM;
    }

    private String erreurMetier(RegleMetierException e, Model model, TypeMaterielDto dto, String titre, String action) {
        preparerForm(model, dto, titre, action);
        model.addAttribute("erreurLibelle", e.getMessage());
        return FORM;
    }
}
