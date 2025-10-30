package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecompense;

    private String nom;
    private String description;
    private Integer points;
    private String condition;

    @OneToMany(mappedBy = "recompense", cascade = CascadeType.ALL)
    private List<RecompenseAssignation> assignations;

    // Getters & Setters
}