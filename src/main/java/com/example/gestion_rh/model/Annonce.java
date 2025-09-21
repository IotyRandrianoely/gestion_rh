package com.example.gestion_rh.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String profil;
    private String description;

    @ManyToOne
    @JoinColumn(name="critere_rech_id")
    private CritereRech critereRech;

    private LocalDate datePublication = LocalDate.now();

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getProfil() { return profil; }
    public void setProfil(String profil) { this.profil = profil; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CritereRech getCritereRech() { return critereRech; }
    public void setCritereRech(CritereRech critereRech) { this.critereRech = critereRech; }
    public LocalDate getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }
}
