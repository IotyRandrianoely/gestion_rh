package com.example.gestion_rh.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class CritereRech {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer anneesExperience;
    private Integer age;
    private Short genre;

    @ManyToOne
    @JoinColumn(name="diplome")
    private Diplome diplome;

    @ManyToOne
    @JoinColumn(name="filiere")
    private Filiere filiere;

    @ManyToMany
    @JoinTable(
        name = "critere_rech_qualite",
        joinColumns = @JoinColumn(name = "id_critere"),
        inverseJoinColumns = @JoinColumn(name = "id_qualite")
    )
    private Set<Qualite> qualites;

    @ManyToMany
    @JoinTable(
        name = "critere_rech_mission",
        joinColumns = @JoinColumn(name = "id_critere"),
        inverseJoinColumns = @JoinColumn(name = "id_mission")
    )
    private Set<Mission> missions;

    // getters/setters
    public Integer getId() { return id; }
    public Integer getAnneesExperience() { return anneesExperience; }
    public void setAnneesExperience(Integer anneesExperience) { this.anneesExperience = anneesExperience; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Short getGenre() { return genre; }
    public void setGenre(Short genre) { this.genre = genre; }
    public Diplome getDiplome() { return diplome; }
    public void setDiplome(Diplome diplome) { this.diplome = diplome; }
    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }
    public Set<Qualite> getQualites() { return qualites; }
    public void setQualites(Set<Qualite> qualites) { this.qualites = qualites; }
    public Set<Mission> getMissions() { return missions; }
    public void setMissions(Set<Mission> missions) { this.missions = missions; }
}
