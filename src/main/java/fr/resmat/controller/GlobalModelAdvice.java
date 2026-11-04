package fr.resmat.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Ajoute à chaque page les infos de l'utilisateur connecté (pour la barre de navigation). */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void infosUtilisateur(Authentication auth, Model model) {
        boolean connecte = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        model.addAttribute("authentifie", connecte);
        model.addAttribute("gestionnaire", connecte
            && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTIONNAIRE")));
        if (connecte) {
            model.addAttribute("username", auth.getName());
        }
    }
}
