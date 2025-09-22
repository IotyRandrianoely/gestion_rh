package com.example.gestion_rh.model;


import jakarta.persistence.*;

@Entity
@Table(name = "resultat_entretien")
public class ResultatEntretien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_candidat", nullable = false)
    private int idCandidat;

    @Column(name = "niveau", nullable = false)
    private int niveau;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        this.idCandidat = idCandidat;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
}