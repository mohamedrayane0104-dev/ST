package com.skilltrack.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String mot_de_passe;

    private Integer niveau = 1;
    private Integer total_points = 0;
    private LocalDateTime date_inscription = LocalDateTime.now();

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

}
