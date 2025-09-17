package com.example.gestion_rh.lysaController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gestion_rh.lysaRepository.CandidatRepository;
import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.lysaModel.Candidat;

import java.util.List;

@Controller
@RequestMapping("/candidats")
public class CandidatController {

    private final CandidatRepository candidatRepo;
    private final AnnonceService annonceService;

    public CandidatController(CandidatRepository candidatRepo, AnnonceService annonceService) {
        this.candidatRepo = candidatRepo;
        this.annonceService = annonceService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Integer annonceId, Model model) {
        List<Annonce> annonces = annonceService.getAll();
        List<Candidat> candidats;
        if (annonceId != null) {
            candidats = candidatRepo.findByAnnonceId(annonceId);
        } else {
            candidats = candidatRepo.findAll();
        }
        model.addAttribute("annonces", annonces);
        model.addAttribute("candidats", candidats);
        return "candidats/candidats"; // résout vers /WEB-INF/views/candidats.jsp via view resolver
    }
}
