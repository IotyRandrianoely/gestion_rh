package com.example.gestion_rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gestion_rh.model.PlaningEntretien;
import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.model.EtatEntretien;
import com.example.gestion_rh.repository.CandidatRepository;
import com.example.gestion_rh.repository.PlaningEntretienRepository;
import com.example.gestion_rh.repository.EtatEntretienRepository;
import com.example.gestion_rh.repository.PosteRepository;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.transaction.Transactional;

@Service
public class PlaningEntretienService {

    private final PlaningEntretienRepository repository;
    private final CandidatRepository candidatRepository;
    private final PosteRepository posteRepository;
    
    @Autowired
    private EtatEntretienRepository etatEntretienRepository;

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
        public void creerPlaningEntretienPourCandidat(int candidatId) {
            Candidat candidat = candidatRepository.findById(candidatId).orElseThrow();
            PlaningEntretien planing = new PlaningEntretien();
            planing.setCandidat(candidat);
            
            // Calculer les dates
            if (this.listerPLaningEntretien().size() != 0) {
                PlaningEntretien dernierPlaning = this.listerPLaningEntretien()
                    .get(this.listerPLaningEntretien().size()-1);
                LocalDateTime d = dernierPlaning.getDateFin();
                planing.setDateDebut(d.plusHours(2));
                planing.setDateFin(planing.getDateDebut().plusHours(1));
            } else {
                LocalDateTime d = LocalDateTime.now();
                planing.setDateDebut(d.plusHours(2));
                planing.setDateFin(planing.getDateDebut().plusHours(1));
            }

            // Sauvegarder le planning
            PlaningEntretien savedPlaning = repository.save(planing);

            // Créer l'état initial de l'entretien
            EtatEntretien etatInitial = new EtatEntretien();
            etatInitial.setIdPlaning(savedPlaning.getId().longValue());
            etatInitial.setEtat(0); // 0 = pas encore passé
            etatInitial.setCommentaire("Entretien planifié");
            etatInitial.setDateModification(LocalDateTime.now());
            etatEntretienRepository.save(etatInitial);

            // Mettre à jour le candidat
            candidat.setEstPropose(true);
            candidatRepository.save(candidat);
        }

    @Transactional
    public PlaningEntretien savePlaning(PlaningEntretien planing) {
        // Sauvegarder d'abord le planning
        PlaningEntretien savedPlaning = repository.save(planing);
        
        // Créer et sauvegarder l'état initial
        EtatEntretien etatInitial = new EtatEntretien();
        etatInitial.setIdPlaning(savedPlaning.getId().longValue());
        etatInitial.setEtat(0); // 0 = pas encore passé
        etatInitial.setCommentaire("Entretien planifié");
        etatInitial.setDateModification(LocalDateTime.now());
        etatEntretienRepository.save(etatInitial);
        
        return savedPlaning;
    }

    // Méthode pour mettre à jour l'état d'un entretien
    @Transactional
    public void updateEtatEntretien(Integer planingId, Integer nouvelEtat, String commentaire) {
        // Mettre à jour l'état dans planing_entretien
        PlaningEntretien planing = repository.findById(Long.valueOf(planingId))
            .orElseThrow(() -> new RuntimeException("Planning non trouvé"));
        planing.setEtat(nouvelEtat);
        repository.save(planing);

        // Créer un nouvel enregistrement dans etat_entretien pour l'historique
        EtatEntretien nouvelEtatEntretien = new EtatEntretien();
        nouvelEtatEntretien.setIdPlaning(planingId != null ? planingId.longValue() : null);
        nouvelEtatEntretien.setEtat(nouvelEtat);
        nouvelEtatEntretien.setCommentaire(commentaire);
        etatEntretienRepository.save(nouvelEtatEntretien);
    }

    @Transactional
    public void marquerEntretienTermine(Long planingId, String commentaire) {
        updateEtatEntretien(planingId.intValue(), 1, commentaire); // 1 = fait
    }

    @Transactional
    public void annulerEntretien(Long planingId, String commentaire) {
        updateEtatEntretien(planingId.intValue(), 2, commentaire); // 2 = annulé
    }

    @Transactional
    public void annulerEntretien(Integer candidatId) {
        // Trouver le dernier entretien planifié pour ce candidat
        PlaningEntretien entretien = repository.findByCandidatIdOrderByDateDebutDesc(candidatId)
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Aucun entretien trouvé pour ce candidat"));

        // Mettre à jour l'état de l'entretien
        updateEtatEntretien(entretien.getId().intValue(), 2, "Entretien annulé"); // 2 = annulé

        // Mettre à jour le candidat
        Candidat candidat = candidatRepository.findById(candidatId)
            .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        candidat.setEstPropose(false);
        candidatRepository.save(candidat);
    }

    @Transactional
    public void reproposerEntretien(Integer candidatId) {
        // Trouver le dernier entretien planifié pour ce candidat
        PlaningEntretien entretien = repository.findByCandidatIdOrderByDateDebutDesc(candidatId)
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Aucun entretien trouvé pour ce candidat"));

        // Mettre à jour l'état de l'entretien
        updateEtatEntretien(entretien.getId().intValue(), 0, "Entretien reproposé"); // 0 = proposé

        // Mettre à jour le candidat
        Candidat candidat = candidatRepository.findById(candidatId)
            .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        candidat.setEstPropose(true);
        candidatRepository.save(candidat);
    }
}