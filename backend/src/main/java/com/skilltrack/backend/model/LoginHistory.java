package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogin;

    private String ip;
    private String userAgent;
    private LocalDateTime dateConnexion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    // Getters & Setters
}
