package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.model.CritereRech;
import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.service.CritereRechService;
import com.example.gestion_rh.service.PosteService;

@Controller
@RequestMapping("/admin/annonces")
public class AdminController {

    private final AnnonceService annonceService;
    private final CritereRechService critereService;
    private final PosteService posteService;

    public AdminController(AnnonceService annonceService, 
                         CritereRechService critereService,
                         PosteService posteService) {
        this.annonceService = annonceService;
        this.critereService = critereService;
        this.posteService = posteService;
    }

    // Liste admin
    @GetMapping
    public String index(Model model) {
        model.addAttribute("annonces", annonceService.getAll());
        return "admin/annonces/index";
    }

    // Formulaire ajout
    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("annonce", new Annonce());
        model.addAttribute("criteres", critereService.getAll());
        model.addAttribute("postes", posteService.getAll());
        return "admin/annonces/form";
    }

    // Formulaire édition
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("annonce", annonceService.getById(id));
        model.addAttribute("criteres", critereService.getAll());
        model.addAttribute("postes", posteService.getAll());
        return "admin/annonces/form";
    }

    @PostMapping
public String save(@ModelAttribute Annonce annonce) {
    // Vérifier si CritereRech existe
    if (annonce.getCritereRech() != null && annonce.getCritereRech().getId() != null) {
        CritereRech critereRech = critereService.getById(annonce.getCritereRech().getId());
        if (critereRech != null) {
            annonce.setCritereRech(critereRech); // Attacher l'entité persistée
        } else {
            throw new IllegalArgumentException("Critère de recherche invalide");
        }
    }

    // Vérifier si Poste existe
    if (annonce.getPoste() != null && annonce.getPoste().getId() != null) {
        Poste poste = posteService.getById(annonce.getPoste().getId());
        if (poste != null) {
            annonce.setPoste(poste); // Attacher l'entité persistée
        } else {
            throw new IllegalArgumentException("Poste invalide");
        }
    }

    // Sauvegarder l'annonce
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