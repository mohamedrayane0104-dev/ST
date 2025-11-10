package com.skilltrack.backend.repository;

import com.skilltrack.backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByDeleteToken(String deleteToken);
}
