package com.example.gestion_rh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.gestion_rh.model.ContratEssai;

public interface ContratEssaiRepository extends JpaRepository<ContratEssai, Long> {

    // Nombre de contrats pour un candidat (retourne 0 si aucun)
    @Query("SELECT COUNT(c) FROM ContratEssai c WHERE c.candidat.id = :candidatId")
    long countByCandidatId(@Param("candidatId") Long candidatId);

    // Somme des durées (en jours) pour un candidat (COALESCE -> 0 si null)
    @Query("SELECT COALESCE(SUM(c.duree), 0) FROM ContratEssai c WHERE c.candidat.id = :candidatId")
    int sumDureeByCandidatId(@Param("candidatId") Long candidatId);
}
