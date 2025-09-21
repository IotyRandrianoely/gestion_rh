package com.example.gestion_rh.service;

import org.springframework.stereotype.Service;
import com.example.gestion_rh.repository.HistoriqueScoreRepository;
import com.example.gestion_rh.model.HistoriqueScore;

@Service
public class HistoriqueScoreService {
    private final HistoriqueScoreRepository repo;

    public HistoriqueScoreService(HistoriqueScoreRepository repo) {
        this.repo = repo;
    }

    public Double getLatestScoreFor(Long candidatId, Integer annonceId) {
        return repo.findFirstByCandidatIdAndAnnonceIdOrderByIdDesc(candidatId, annonceId)
                   .map(HistoriqueScore::getScore)
                   .orElse(null);
    }
}