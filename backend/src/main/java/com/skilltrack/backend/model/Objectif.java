package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Objectif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObjectif;

    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateLimite;
    private String statut;
    private Float progression = 0f;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "objectif", cascade = CascadeType.ALL)
    private List<Tache> taches;

    @OneToMany(mappedBy = "objectif", cascade = CascadeType.ALL)
    private List<PlanExecution> plans;

    @OneToMany(mappedBy = "objectif", cascade = CascadeType.ALL)
    private List<Recommandation> recommandations;

    @OneToMany(mappedBy = "objectif", cascade = CascadeType.ALL)
    private List<Historique> historiques;

    @OneToMany(mappedBy = "objectif", cascade = CascadeType.ALL)
    private List<RecompenseAssignation> recompenses;

    // Getters & Setters
}
