package com.example.gestion_rh.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "etat_entretien")
public class EtatEntretien {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "id_planing")
    private Integer idPlaning; // Keep as Integer to match database type
    
    @Column(name = "etat")
    private Integer etat;
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
    
    @Column(name = "commentaire")
    private String commentaire;
    
    // Constructeur par défaut
    public EtatEntretien() {
        this.dateModification = LocalDateTime.now();
    }
    
    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getIdPlaning() {
        return idPlaning != null ? idPlaning.longValue() : null;
    }

    public void setIdPlaning(Long idPlaning) {
        this.idPlaning = idPlaning != null ? idPlaning.intValue() : null;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}