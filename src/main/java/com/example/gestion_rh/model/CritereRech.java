package com.example.gestion_rh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "critere_rech")
public class CritereRech {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int anneesExperience;
    private int age;
    private int genre;

    @ManyToOne
    @JoinColumn(name = "diplome")
    private Diplome diplome;

    @ManyToOne
    @JoinColumn(name = "filiere")
    private Filiere filiere;

    // --- Getters & Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getAnneesExperience() { return anneesExperience; }
    public void setAnneesExperience(int anneesExperience) { this.anneesExperience = anneesExperience; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getGenre() { return genre; }
    public void setGenre(int genre) { this.genre = genre; }

    public Diplome getDiplome() { return diplome; }
    public void setDiplome(Diplome diplome) { this.diplome = diplome; }

    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }
}
