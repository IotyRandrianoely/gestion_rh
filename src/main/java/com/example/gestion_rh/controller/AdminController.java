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
import com.example.gestion_rh.service.DiplomeService;
import com.example.gestion_rh.service.FiliereService;
import com.example.gestion_rh.service.PosteService;

@Controller
@RequestMapping("/admin/annonces")
public class AdminController {

    private final AnnonceService annonceService;
    private final CritereRechService critereService;
    private final PosteService posteService;
    private final DiplomeService diplomeService;
    private final FiliereService filiereService;

    public AdminController(AnnonceService annonceService,
                           CritereRechService critereService,
                           PosteService posteService,
                           DiplomeService diplomeService,
                           FiliereService filiereService) {
        this.annonceService = annonceService;
        this.critereService = critereService;
        this.posteService = posteService;
        this.diplomeService = diplomeService;
        this.filiereService = filiereService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("annonces", annonceService.getAll());
        return "admin/annonces/index";
    }

    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("annonce", new Annonce());
        model.addAttribute("postes", posteService.getAll());
        model.addAttribute("diplomes", diplomeService.getAll());
        model.addAttribute("filieres", filiereService.getAll());
        return "admin/annonces/form";
    }

    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("annonce", annonceService.getById(id));
        model.addAttribute("postes", posteService.getAll());
        model.addAttribute("diplomes", diplomeService.getAll());
        model.addAttribute("filieres", filiereService.getAll());
        return "admin/annonces/form";
    }

    @PostMapping
    public String save(@ModelAttribute Annonce annonce) {
        if (annonce.getCritereRech() != null) {
            CritereRech critere = annonce.getCritereRech();

            if (critere.getDiplome() != null && critere.getDiplome().getId() != null) {
                critere.setDiplome(diplomeService.getById(critere.getDiplome().getId()));
            }
            if (critere.getFiliere() != null && critere.getFiliere().getId() != null) {
                critere.setFiliere(filiereService.getById(critere.getFiliere().getId()));
            }

            CritereRech savedCritere = critereService.save(critere);
            annonce.setCritereRech(savedCritere);
        }

        if (annonce.getPoste() != null && annonce.getPoste().getId() != null) {
            Poste poste = posteService.getById(annonce.getPoste().getId());
            annonce.setPoste(poste);
        }

        annonceService.save(annonce);
        return "redirect:/admin/annonces";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        annonceService.delete(id);
        return "redirect:/admin/annonces";
    }
}
