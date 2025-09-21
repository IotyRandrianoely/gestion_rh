package com.example.gestion_rh.dto;

import java.time.LocalDateTime;

public class EntretienDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String poste;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    public EntretienDTO() {}

    public EntretienDTO(Long id, String nom, String prenom, String poste, 
                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
}