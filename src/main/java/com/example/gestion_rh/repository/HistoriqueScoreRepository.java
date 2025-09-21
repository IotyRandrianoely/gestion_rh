package com.example.gestion_rh.repository;

import com.example.gestion_rh.model.HistoriqueScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueScoreRepository extends JpaRepository<HistoriqueScore, Long> {
    Optional<HistoriqueScore> findFirstByCandidatIdAndAnnonceIdOrderByIdDesc(Long candidatId, Integer annonceId);
}