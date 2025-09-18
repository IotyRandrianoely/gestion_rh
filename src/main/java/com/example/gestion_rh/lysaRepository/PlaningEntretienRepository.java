package com.example.gestion_rh.lysaRepository;

import com.example.gestion_rh.lysaModel.PlaningEntretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlaningEntretienRepository extends JpaRepository<PlaningEntretien, Long> {
    
    // Méthode Spring Data JPA automatique
    List<PlaningEntretien> findByDateDebutBetween(LocalDateTime start, LocalDateTime end);
    
    // Alternative avec requête explicite si la méthode automatique ne fonctionne pas
    @Query("SELECT p FROM PlaningEntretien p WHERE p.dateDebut >= :start AND p.dateDebut <= :end ORDER BY p.dateDebut")
    List<PlaningEntretien> findEntretiensBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}