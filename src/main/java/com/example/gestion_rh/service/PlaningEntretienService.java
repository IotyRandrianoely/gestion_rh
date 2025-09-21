package com.example.gestion_rh.service;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.PlaningEntretien;
import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.repository.CandidatRepository;
import com.example.gestion_rh.repository.PlaningEntretienRepository;
import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.repository.PosteRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class PlaningEntretienService {

    private final PlaningEntretienRepository repository;
    private final CandidatRepository candidatRepository;
    private final PosteRepository posteRepository;

    public PlaningEntretienService(PlaningEntretienRepository repository, CandidatRepository candidatRepository, PosteRepository posteRepository) {
        this.repository = repository;
        this.candidatRepository = candidatRepository;
        this.posteRepository = posteRepository;
    }

    public PlaningEntretien creerPlaningEntretien(PlaningEntretien planing) {
        return repository.save(planing);
    }

    public List<PlaningEntretien> listerPLaningEntretien() {
        return repository.findAll();
    }

    public PlaningEntretien getplaning(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PlaningEntretien mettreAJourplaning(Long id, PlaningEntretien planing) {
        planing.setId(id);
        return repository.save(planing);
    }

    public void supprimerplaning(Long id) {
        repository.deleteById(id);
    }
    // ...existing code...
        public void creerPlaningEntretienPourCandidat(int candidatId) {
            Candidat candidat = candidatRepository.findById(candidatId).orElseThrow();
            PlaningEntretien planing = new PlaningEntretien();
            planing.setCandidat(candidat);
            int i = 0;
            PlaningEntretien c = new PlaningEntretien();
            if (this.listerPLaningEntretien().size() != 0){
                
                c = this.listerPLaningEntretien().get(this.listerPLaningEntretien().size()-1);
                LocalDateTime d = c.getDateFin();
                planing.setDateDebut(d.plusHours(2));
                planing.setDateFin(planing.getDateDebut().plusHours(1));
            } else {
                LocalDateTime d = LocalDateTime.now();
                planing.setDateDebut(d.plusHours(2));
                planing.setDateFin(planing.getDateDebut().plusHours(1));

            }
                repository.save(planing);

            // Mettre à jour estPropose à true
            candidat.setEstPropose(true);
            candidatRepository.save(candidat);
        }
    // ...existing code...
}