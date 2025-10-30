package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorique;

    private String action;
    private LocalDateTime dateAction = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_objectif")
    private Objectif objectif;

    @ManyToOne
    @JoinColumn(name = "id_tache")
    private Tache tache;

    // Getters & Setters
}
