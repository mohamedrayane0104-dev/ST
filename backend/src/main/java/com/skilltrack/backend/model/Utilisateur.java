package com.skilltrack.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String mot_de_passe;
    @Builder.Default
    private Integer niveau = 1;
    @Builder.Default
    private Integer total_points = 0;
    @Builder.Default
    private LocalDateTime date_inscription = LocalDateTime.now();
    @Builder.Default
    private Boolean emailVerified = false;
    @Column(name = "delete_token")
    private String deleteToken;

    @Column(name = "delete_token_expiration")
    private LocalDateTime deleteTokenExpiration;


    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Objectif> objectifs;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Historique> historiques;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<RecompenseAssignation> recompenses;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Token> tokens;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<EmailLog> emailsEnvoyes;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<LoginHistory> loginHistory;
    @PrePersist
    public void prePersist() {
        if (niveau == null) niveau = 1;
        if (total_points == null) total_points = 0;
        if (date_inscription == null) date_inscription = LocalDateTime.now();
        if (emailVerified == null) emailVerified = false;
    }
    // === Implémentations de UserDetails ===
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return mot_de_passe;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
