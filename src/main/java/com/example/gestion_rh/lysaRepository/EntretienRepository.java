package com.example.gestion_rh.lysaRepository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestion_rh.lysaModel.PlaningEntretien;

@Repository
public interface EntretienRepository extends JpaRepository<PlaningEntretien, Long> {
    List<PlaningEntretien> findByDateDebutBetween(LocalDateTime start, LocalDateTime end);
}