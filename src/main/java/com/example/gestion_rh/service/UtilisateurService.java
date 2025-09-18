package com.example.gestion_rh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.Utilisateur;
import com.example.gestion_rh.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur authenticate(String username, String password) {
        Optional<Utilisateur> user = utilisateurRepository.findByUsernameAndPassword(username, password);
        return user.orElse(null);
    }

    public boolean isValidUser(String username, String password) {
        return authenticate(username, password) != null;
    }

    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur getById(Integer id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    public Utilisateur getByUsername(String username) {
        return utilisateurRepository.findByUsername(username).orElse(null);
    }

    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(Integer id) {
        utilisateurRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return utilisateurRepository.existsByUsername(username);
    }
}