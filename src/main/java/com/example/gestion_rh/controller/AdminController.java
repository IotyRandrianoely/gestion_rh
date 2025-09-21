package com.example.gestion_rh.controller;

import java.time.LocalDate;

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
        model.addAttribute("postes", posteService.getAll());
        model.addAttribute("diplomes", diplomeService.getAll());
        model.addAttribute("filieres", filiereService.getAll());
        return "admin/annonces/form";
    }

    // Formulaire édition
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("annonce", annonceService.getById(id));
        model.addAttribute("postes", posteService.getAll());
        model.addAttribute("diplomes", diplomeService.getAll());
        model.addAttribute("filieres", filiereService.getAll());
        return "admin/annonces/form";
    }

    // Création / Mise à jour
    @PostMapping
    public String save(@ModelAttribute Annonce annonce) {
        boolean isEdit = (annonce.getId() != null);

        // --- Critères ---
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
        } else {
            throw new IllegalArgumentException("Critère de recherche requis.");
        }

        // --- Poste : EXISTANT (et on peut changer) ou NOUVEAU ---
        if (annonce.getPoste() != null) {
            if (annonce.getPoste().getId() != null) {
                // Poste existant (on peut le choisir même en édition)
                Poste poste = posteService.getById(annonce.getPoste().getId());
                if (poste == null) {
                    throw new IllegalArgumentException("Poste invalide.");
                }

                // Optionnel : en MODIFICATION, si le form a envoyé un nouveau texte, on met à jour le poste
                if (isEdit) {
                    String newProfil = annonce.getPoste().getProfil();
                    String newDesc   = annonce.getPoste().getDescription();
                    if (newProfil != null && !newProfil.isBlank()) {
                        poste.setProfil(newProfil);
                    }
                    if (newDesc != null && !newDesc.isBlank()) {
                        poste.setDescription(newDesc);
                    }
                    poste = posteService.save(poste);
                }

                annonce.setPoste(poste);

            } else if (annonce.getPoste().getProfil() != null && !annonce.getPoste().getProfil().isBlank()) {
                // NOUVEAU poste (création) -> INSERT dans "poste" puis lien dans annonce
                Poste savedPoste = posteService.save(annonce.getPoste());
                annonce.setPoste(savedPoste);

            } else {
                throw new IllegalArgumentException("Veuillez sélectionner un poste existant ou saisir un nouveau poste.");
            }
        } else {
            throw new IllegalArgumentException("Poste requis.");
        }

        // --- Date : garder la date d'origine en édition si rien n'est envoyé ---
        if (isEdit && annonce.getDatePublication() == null) {
            LocalDate origin = annonceService.getById(annonce.getId()).getDatePublication();
            annonce.setDatePublication(origin);
        }
        if (!isEdit && annonce.getDatePublication() == null) {
            annonce.setDatePublication(LocalDate.now());
        }

        // Sauvegarde annonce (INSERT ou UPDATE selon présence de id)
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
