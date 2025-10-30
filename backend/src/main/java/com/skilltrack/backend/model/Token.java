package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToken;

    private String type;
    private String tokenHash;
    private LocalDateTime expiration;
    private Boolean used = false;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utilisateur utilisateur;

    // Getters et Setters
}
