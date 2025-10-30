package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmail;

    private String destinataire;
    private String sujet;
    @Column(columnDefinition = "TEXT")
    private String contenu;
    private String statut;
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    // Getters et Setters
}
