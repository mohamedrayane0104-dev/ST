package com.skilltrack.backend.repository;

import com.skilltrack.backend.model.Token;
import com.skilltrack.backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    void deleteByUtilisateur(Utilisateur utilisateur);
}
