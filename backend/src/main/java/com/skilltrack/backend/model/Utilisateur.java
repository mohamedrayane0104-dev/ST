package com.skilltrack.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
    private String motDePasse;
    private Integer niveau = 1;
    private Integer totalPoints = 0;
    private LocalDateTime dateInscription = LocalDateTime.now();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Objectif> objectifs;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Historique> historiques;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<RecompenseAssignation> recompenses;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Token> tokens;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<EmailLog> emailsEnvoyes;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<LoginHistory> loginHistory;

    // Getters et Setters
}
