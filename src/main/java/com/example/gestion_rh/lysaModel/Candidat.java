package com.example.gestion_rh.lysaModel;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.example.gestion_rh.model.Annonce;

@Entity
@Table(name = "candidat")
public class Candidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_annonce", nullable = false)
    private Annonce annonce;

    private String nom;
    private String prenom;
    private Integer age;
    private Integer genre;
    private String adresse;
    private String email;
    private Integer anneesExperience;

    @Column(length = 300)
    private String lettreMotivation;

    private String cv;
    private LocalDate dateCandidature;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAnneesExperience() {
        return anneesExperience;
    }

    public void setAnneesExperience(Integer anneesExperience) {
        this.anneesExperience = anneesExperience;
    }

    public String getLettreMotivation() {
        return lettreMotivation;
    }

    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public LocalDate getDateCandidature() {
        return dateCandidature;
    }

    public void setDateCandidature(LocalDate dateCandidature) {
        this.dateCandidature = dateCandidature;
    }
}
