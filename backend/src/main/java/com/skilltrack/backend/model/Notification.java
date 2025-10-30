package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotification;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateEnvoi = LocalDateTime.now();
    private String statut;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    // Getters & Setters
}