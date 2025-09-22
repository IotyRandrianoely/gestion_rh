package com.example.gestion_rh.service;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.ContratEssai;
import com.example.gestion_rh.repository.ContratEssaiRepository;
import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.repository.CandidatRepository;
import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.repository.PosteRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class ContratEssaiService {

    private final ContratEssaiRepository repository;
    private final CandidatRepository candidatRepository;
    private final PosteRepository posteRepository;

    public ContratEssaiService(ContratEssaiRepository repository, CandidatRepository candidatRepository, PosteRepository posteRepository) {
        this.repository = repository;
        this.candidatRepository = candidatRepository;
        this.posteRepository = posteRepository;
    }

    public ContratEssai creerContrat(ContratEssai contrat) {
        return repository.save(contrat);
    }

    public List<ContratEssai> listerContrats() {
        return repository.findAll();
    }

    public ContratEssai getContrat(Long id) {
        return repository.findById(id).orElse(null);
    }

    public ContratEssai mettreAJourContrat(Long id, ContratEssai contrat) {
        contrat.setId(id);
        return repository.save(contrat);
    }

    public void supprimerContrat(Long id) {
        repository.deleteById(id);
    }
    
    public void creerContratPourCandidat(int candidatId) {
        Candidat candidat = candidatRepository.findById(candidatId).orElseThrow();
        ContratEssai contrat = new ContratEssai();
        contrat.setCandidat(candidat);
        Poste poste = posteRepository.findAll().stream().findFirst().orElse(null);
        contrat.setPoste(poste);
        int i = 0;
        ContratEssai c = this.listerContrats().get(this.listerContrats().size()-1);
        LocalDateTime d = c.getDateFin();
        contrat.setDateDebut(d.plusHours(2));
        contrat.setDateFin(contrat.getDateDebut().plusHours(1));
        contrat.setDuree(30);
        repository.save(contrat);

        // Mettre à jour estPropose à true
        candidat.setEstPropose(true);
        candidatRepository.save(candidat);
    }
}