package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PlanExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlan;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateGeneration = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_objectif")
    private Objectif objectif;

    // Getters & Setters
}