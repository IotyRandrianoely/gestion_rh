package com.example.gestion_rh.lysaRepository;

import com.example.gestion_rh.lysaModel.PlaningEntretien;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaningEntretienRepository extends JpaRepository<PlaningEntretien, Long> {
    List<PlaningEntretien> findByDateDebutBetween(LocalDateTime start, LocalDateTime end);
}