package com.example.gestion_rh.lysaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestion_rh.Model.Candidat;

public interface CandidatRepository extends JpaRepository<ContratEssai, Long> {
}
