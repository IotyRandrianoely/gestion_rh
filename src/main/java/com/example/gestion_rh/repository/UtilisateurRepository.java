package com.example.gestion_rh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestion_rh.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByUsername(String username);

    Optional<Utilisateur> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}