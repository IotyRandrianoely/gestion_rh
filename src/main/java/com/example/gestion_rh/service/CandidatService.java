package com.example.gestion_rh.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.model.Diplome;
import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.repository.CandidatRepository;
import com.example.gestion_rh.repository.AnnonceRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class CandidatService {
    private final CandidatRepository repo;
    private final AnnonceRepository annonceRepo;
    private final FileStorageService fileStorageService;

    @PersistenceContext
    private EntityManager entityManager;

    public CandidatService(CandidatRepository repo, AnnonceRepository annonceRepo,
            FileStorageService fileStorageService) {
        this.repo = repo;
        this.annonceRepo = annonceRepo;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public List<Candidat> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Candidat getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // Dans la méthode isEligible, ajouter :

public boolean isEligible(Candidat candidat) {
    if (candidat.getAnnonce() == null || candidat.getAnnonce().getId() == null) {
        return false;
    }

    Annonce annonce = annonceRepo.findById(candidat.getAnnonce().getId()).orElse(null);
    if (annonce == null || annonce.getCritereRech() == null) {
        return false;
    }

    // Vérifier les années d'expérience
    Integer experienceRequise = annonce.getCritereRech().getAnneesExperience();
    Integer experienceCandidat = candidat.getAnneesExperience();

    if (experienceRequise != null && experienceCandidat != null) {
        if (experienceCandidat < experienceRequise) {
            return false;
        }
    }

    // Vérifier l'âge
    Integer ageRequis = annonce.getCritereRech().getAge();
    Integer ageCandidat = candidat.getAge();
    
    if (ageRequis != null && ageCandidat != null) {
        if (ageCandidat > ageRequis) {
            return false;
        }
    }

    // *** NOUVEAU : Vérifier le diplôme ***
    Diplome diplomeRequis = annonce.getCritereRech().getDiplome();
    Diplome diplomeCandidat = candidat.getDiplome();
    
    if (diplomeRequis != null && diplomeCandidat != null) {
        // Assumons que les diplômes ont un ordre hiérarchique par ID
        // (1=BEPC, 2=BAC, 3=Licence, 4=Master, 5=Doctorat)
        if (diplomeCandidat.getId() < diplomeRequis.getId()) {
            return false;
        }
    }
    
    return true;
}
    /**
     * Obtient le message d'inéligibilité détaillé
     */
    public String getIneligibilityMessage(Candidat candidat) {
        if (candidat.getAnnonce() == null || candidat.getAnnonce().getId() == null) {
            return "Aucune annonce sélectionnée.";
        }

        Annonce annonce = annonceRepo.findById(candidat.getAnnonce().getId()).orElse(null);
        if (annonce == null || annonce.getCritereRech() == null) {
            return "Annonce ou critères non trouvés.";
        }

        StringBuilder message = new StringBuilder();
        message.append("Vous n'êtes pas éligible pour cette annonce.");

        // Vérifier les années d'expérience
        Integer experienceRequise = annonce.getCritereRech().getAnneesExperience();
        Integer experienceCandidat = candidat.getAnneesExperience();

        if (experienceRequise != null && experienceCandidat != null) {
            if (experienceCandidat < experienceRequise) {
                message.append("- Expérience insuffisante : ")
                        .append(experienceCandidat)
                        .append(" ans (minimum requis : ")
                        .append(experienceRequise)
                        .append(" ans)\n");
            }
        }

        // Ajouter d'autres critères si nécessaire
        Integer ageRequis = annonce.getCritereRech().getAge();
        Integer ageCandidat = candidat.getAge();

        if (ageRequis != null && ageCandidat != null) {
            if (ageCandidat > ageRequis) {
                message.append("- Âge non conforme : ")
                        .append(ageCandidat)
                        .append(" ans (maximum requis : ")
                        .append(ageRequis)
                        .append(" ans)\n");
            }
        }

        return message.toString();
    }

    @Transactional
    public Candidat save(Candidat candidat) {
        try {
            // Vérification de l'annonce
            if (candidat.getAnnonce() != null && candidat.getAnnonce().getId() != null) {
                Annonce annonce = annonceRepo.findById(candidat.getAnnonce().getId()).orElse(null);
                if (annonce == null) {
                    throw new RuntimeException("Annonce non trouvée avec l'ID: " + candidat.getAnnonce().getId());
                }
                candidat.setAnnonce(annonce);
            }

            // Définir la date si nouvelle candidature
            if (candidat.getId() == null && candidat.getDateCandidature() == null) {
                candidat.setDateCandidature(LocalDate.now());
            }

            // Forcer le flush pour voir l'erreur immédiatement
            Candidat saved = repo.save(candidat);
            entityManager.flush();

            System.out.println("Candidat sauvegardé avec ID: " + (saved != null ? saved.getId() : "null"));
            return saved;

        } catch (Exception e) {
            System.err.println("Erreur dans CandidatService.save: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la sauvegarde: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void delete(Integer id) {
        // Supprimer le fichier CV associé avant de supprimer le candidat
        Candidat candidat = getById(id);
        if (candidat != null && candidat.getCv() != null) {
            fileStorageService.deleteFile(candidat.getCv());
        }

        if (id != null && repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public List<Candidat> getByAnnonceId(Integer annonceId) {
        if (annonceId == null) {
            return getAll();
        }
        return repo.findByAnnonceId(annonceId);
    }
}
