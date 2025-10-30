package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RecompenseAssignation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAssignation;

    private LocalDateTime dateObtention = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_recompense")
    private Recompense recompense;

    @ManyToOne
    @JoinColumn(name = "id_tache")
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "id_objectif")
    private Objectif objectif;

    // Getters & Setters
}
