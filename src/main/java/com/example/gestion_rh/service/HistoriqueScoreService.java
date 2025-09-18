package com.example.gestion_rh.service;

import com.example.gestion_rh.model.HistoriqueScore;
import com.example.gestion_rh.repository.HistoriqueScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoriqueScoreService {
    
    @Autowired
    private HistoriqueScoreRepository historiqueScoreRepository;
    
    public HistoriqueScore saveScore(HistoriqueScore score) {
        return historiqueScoreRepository.save(score);
    }
}