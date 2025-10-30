package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTache;

    private String titre;
    private String description;
    private String statut;
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_objectif")
    private Objectif objectif;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL)
    private List<Historique> historiques;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL)
    private List<RecompenseAssignation> recompenses;

    // Getters & Setters
}
