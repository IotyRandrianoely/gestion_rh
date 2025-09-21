package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gestion_rh.service.AnnonceService;

@Controller
@RequestMapping("/annonces")
public class ClientController {

    private final AnnonceService annonceService;

    public ClientController(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    // Liste des annonces
    @GetMapping
    public String list(Model model) {
        model.addAttribute("annonces", annonceService.getAll());
        return "annonces/list";  // renvoie vers /WEB-INF/views/annonces/list.jsp
    }

    // Détail d'une annonce
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("annonce", annonceService.getById(id));
        return "annonces/detail"; // renvoie vers /WEB-INF/views/annonces/detail.jsp
    }
}
