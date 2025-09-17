package com.example.gestion_rh.lysaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestion_rh.lysaModel.Poste;

public interface PosteRepository extends JpaRepository<Poste, Long> {
}
