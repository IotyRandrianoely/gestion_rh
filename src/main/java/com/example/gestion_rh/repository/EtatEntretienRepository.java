package com.example.gestion_rh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestion_rh.model.EtatEntretien;

public interface EtatEntretienRepository extends JpaRepository<EtatEntretien, Integer> {
    // Méthodes spécifiques si nécessaire
}