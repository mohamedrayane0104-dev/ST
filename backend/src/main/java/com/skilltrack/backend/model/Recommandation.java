package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Recommandation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecommandation;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private String type;
    private LocalDateTime dateGeneration = LocalDateTime.now();
    private Boolean genereParIa = true;

    @ManyToOne
    @JoinColumn(name = "id_objectif")
    private Objectif objectif;

    // Getters & Setters
}
