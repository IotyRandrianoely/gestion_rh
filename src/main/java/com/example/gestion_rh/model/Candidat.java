package com.example.gestion_rh.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "candidat")
public class Candidat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidat_seq")
    @SequenceGenerator(name = "candidat_seq", sequenceName = "candidat_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_annonce")
    private Annonce annonce;

    @Column(nullable = false, length = 40)
    private String nom;
    
    @Column(nullable = false, length = 40)
    private String prenom;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(nullable = false)
    private Integer genre;
    
    @Column(length = 60)
    private String adresse;
    
    @Column(nullable = false, length = 40)
    private String email;
    
    @Column(name = "annees_experience", nullable = false)
    private Integer anneesExperience;
    
    @Column(name = "lettre_motivation", length = 300)
    private String lettreMotivation;
    
    @Column(length = 60)
    private String cv;
    
    @Column(name = "date_candidature")
    private LocalDate dateCandidature;

    // *** NOUVELLE RELATION AVEC DIPLOME ***
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diplome")
    private Diplome diplome;

    // Constructeur par défaut
    public Candidat() {
        this.dateCandidature = LocalDate.now();
    }

    // Getters et setters existants...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Annonce getAnnonce() { return annonce; }
    public void setAnnonce(Annonce annonce) { this.annonce = annonce; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public Integer getGenre() { return genre; }
    public void setGenre(Integer genre) { this.genre = genre; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getAnneesExperience() { return anneesExperience; }
    public void setAnneesExperience(Integer anneesExperience) { this.anneesExperience = anneesExperience; }
    
    public String getLettreMotivation() { return lettreMotivation; }
    public void setLettreMotivation(String lettreMotivation) { this.lettreMotivation = lettreMotivation; }
    
    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    
    public LocalDate getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(LocalDate dateCandidature) { this.dateCandidature = dateCandidature; }

    // *** NOUVEAU GETTER/SETTER POUR DIPLOME ***
    public Diplome getDiplome() { return diplome; }
    public void setDiplome(Diplome diplome) { this.diplome = diplome; }

    public Integer getAnneesExperience() {
        return anneesExperience;
    }

    public void setAnneesExperience(Integer anneesExperience) {
        this.anneesExperience = anneesExperience;
    }

   
   
    public Boolean getEstPropose() {
        return estPropose;
    }

    public void setEstPropose(Boolean estPropose) {
        this.estPropose = estPropose;
    }
}