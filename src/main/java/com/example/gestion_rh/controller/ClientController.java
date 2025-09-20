package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.DiplomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/annonces")
public class ClientController {

    // @Autowired
     private final CandidatService candidatService;
    private final AnnonceService annonceService;
    private final DiplomeService diplomeService;

    public ClientController(CandidatService candidatService, AnnonceService annonceService, DiplomeService diplomeService) {
        this.candidatService = candidatService;
        this.annonceService = annonceService;
        this.diplomeService = diplomeService;
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
    @GetMapping("/{id}/postuler")
    public String mnmn(@PathVariable Integer id, Model model){
        model.addAttribute("candidat", new Candidat());
        model.addAttribute("annonce", annonceService.getById(id));
        model.addAttribute("diplomes", diplomeService.getAll()); // *** NOUVEAU ***
         
        return "admin/candidats/form_client";
    }
    @PostMapping
    public String save(@ModelAttribute Candidat candidat,
            @RequestParam(name = "annonce.id", required = false) Integer annonceId,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation des champs obligatoires
            if (candidat.getNom() == null || candidat.getNom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le nom est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getPrenom() == null || candidat.getPrenom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le prénom est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getEmail() == null || candidat.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "L'email est obligatoire");
                return "redirect:/admin/candidats/new";
            }
            candidat.setAnnonce(annonceService.getById(annonceId));
            if (candidat.getAnnonce() == null || candidat.getAnnonce().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner une annonce");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getAge() == null || candidat.getAge() < 16 || candidat.getAge() > 70) {
                redirectAttributes.addFlashAttribute("error", "L'âge doit être entre 16 et 70 ans");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getGenre() == null) {
                redirectAttributes.addFlashAttribute("error", "Le genre est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getAnneesExperience() == null || candidat.getAnneesExperience() < 0) {
                redirectAttributes.addFlashAttribute("error", "Les années d'expérience sont obligatoires");
                return "redirect:/admin/candidats/new";
            }

            // *** VALIDATION DU DIPLOME ***
            if (candidat.getDiplome() == null || candidat.getDiplome().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner un diplôme");
                return "redirect:/admin/candidats/new";
            }


            System.out.println("=== DEBUG ADMIN CANDIDAT ===");
            System.out.println("Candidat ID: " + annonceId);
            System.out.println("Nom: " + candidat.getNom());
            System.out.println("Annonce ID: " + (candidat.getAnnonce() != null ? candidat.getAnnonce().getId() : "null"));
            System.out.println("Diplome ID: " + (candidat.getDiplome() != null ? candidat.getDiplome().getId() : "null"));

            Candidat savedCandidat = candidatService.save(candidat);
            if (savedCandidat != null && savedCandidat.getId() != null) {
                redirectAttributes.addFlashAttribute("success", "Candidat sauvegardé avec succès !");
                // Passer les IDs comme paramètres dans l'URL
                return "redirect:/qcm/start?candidatId=" + savedCandidat.getId() + "&annonceId=" + annonceId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Erreur lors de la sauvegarde");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde admin: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur technique: " + e.getMessage());
        }
        
        return "redirect:/annonces";  // En cas d'erreur, retour à la liste des annonces
    }
}
