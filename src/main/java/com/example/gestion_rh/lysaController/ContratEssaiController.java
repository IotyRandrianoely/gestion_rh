package com.example.gestion_rh.lysaController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.gestion_rh.lysaModel.ContratEssai;
import com.example.gestion_rh.lysaModel.Candidat;
import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.lysaService.ContratEssaiService;
import com.example.gestion_rh.repository.PosteRepository;
import com.example.gestion_rh.lysaRepository.CandidatRepository;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/contratEssai")
public class ContratEssaiController {

    private final ContratEssaiService service;
    private final PosteRepository posteRepo;
    private final CandidatRepository candidatRepo;

    public ContratEssaiController(ContratEssaiService service,
                                  PosteRepository posteRepo,
                                  CandidatRepository candidatRepo) {
        this.service = service;
        this.posteRepo = posteRepo;
        this.candidatRepo = candidatRepo;
    }

    // Affiche le formulaire de création de contrat d'essai
    // URL exemple: /contratEssai/contratEssai?candidateId=1
    @GetMapping("/contratEssai")
    public String showForm(@RequestParam(required = false) Long candidateId, Model model) {
        model.addAttribute("postes", posteRepo.findAll());
        if (candidateId != null) {
            Optional<Candidat> cand = candidatRepo.findById(candidateId);
            cand.ifPresent(c -> model.addAttribute("candidate", c));
            model.addAttribute("candidateId", candidateId);
        }
        // le template attendu : src/main/resources/templates/contratEssai/contratEssai.html
        return "contratEssai/contratEssai";
    }

    // Traite la soumission standard depuis le formulaire (en POST)
    @PostMapping("/save")
    public String saveFromForm(@RequestParam(required = false) Long candidateId,
                               @RequestParam Long posteId,
                               @RequestParam String dateDebut,
                               @RequestParam Integer duree,
                               @RequestParam Double salaire,
                               @RequestParam(required = false) String conditions,
                               RedirectAttributes redirectAttrs) {

        ContratEssai contrat = new ContratEssai();

        // Associer candidat si fourni
        if (candidateId != null) {
            Optional<Candidat> cand = candidatRepo.findById(candidateId);
            if (cand.isPresent()) {
                contrat.setCandidat(cand.get());
            } else {
                redirectAttrs.addFlashAttribute("error", "Candidat introuvable");
                return "redirect:/contratEssai";
            }
        }

        // Associer poste
        Optional<Poste> p = posteRepo.findById(posteId);
        if (p.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Poste introuvable");
            return "redirect:/contratEssai" + (candidateId != null ? "?candidateId=" + candidateId : "");
        }
        contrat.setPoste(p.get());

        // Champs date/durée/salaire/conditions
        try {
            contrat.setDateDebut(LocalDate.parse(dateDebut));
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", "Date de début invalide");
            return "redirect:/contratEssai" + (candidateId != null ? "?candidateId=" + candidateId : "");
        }

        contrat.setDuree(duree);
        contrat.setSalaire(salaire);
        contrat.setConditions(conditions);

        service.creerContrat(contrat);
        redirectAttrs.addFlashAttribute("success", "Contrat d'essai proposé");
        return "redirect:/candidats";
    }
}