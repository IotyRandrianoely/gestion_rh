package com.example.gestion_rh.lysaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestion_rh.lysaModel.Candidat;
import java.util.List;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    // recherche par id de l'annonce (la classe Annonce utilise Integer comme id)
    List<Candidat> findByAnnonceId(Integer annonceId);
}
