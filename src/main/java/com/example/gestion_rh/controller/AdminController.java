package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.service.CritereRechService;

@Controller
@RequestMapping("/admin/annonces")
public class AdminController {

    private final AnnonceService annonceService;
    private final CritereRechService critereService;

    public AdminController(AnnonceService annonceService, CritereRechService critereService) {
        this.annonceService = annonceService;
        this.critereService = critereService;
    }

    // Liste admin
    @GetMapping
    public String index(Model model) {
        model.addAttribute("annonces", annonceService.getAll());
        return "admin/annonces/index"; // JSP : /WEB-INF/views/admin/annonces/index.jsp
    }

    // Formulaire ajout
    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("annonce", new Annonce());
        model.addAttribute("criteres", critereService.getAll());
        return "admin/annonces/form"; // JSP
    }

    // Formulaire édition
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("annonce", annonceService.getById(id));
        model.addAttribute("criteres", critereService.getAll());
        return "admin/annonces/form"; // JSP
    }

    // Sauvegarde
    @PostMapping
    public String save(@ModelAttribute Annonce annonce) {
        annonceService.save(annonce);
        return "redirect:/admin/annonces";
    }

    // Suppression
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        annonceService.delete(id);
        return "redirect:/admin/annonces";
    }
}
