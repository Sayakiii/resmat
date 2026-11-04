package fr.resmat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String accueil() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout, Model model) {
        model.addAttribute("erreur", error != null);
        model.addAttribute("deconnecte", logout != null);
        return "login";
    }
}
