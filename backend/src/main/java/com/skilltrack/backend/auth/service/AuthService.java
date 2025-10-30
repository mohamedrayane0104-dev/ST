package com.skilltrack.backend.auth.service;

import com.skilltrack.backend.auth.dto.LoginRequest;
import com.skilltrack.backend.auth.dto.RegisterRequest;
import com.skilltrack.backend.auth.jwt.JwtService;
import com.skilltrack.backend.model.Utilisateur;
import com.skilltrack.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        Utilisateur user = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .mot_de_passe(passwordEncoder.encode(request.getMotDePasse()))
                .build();

        utilisateurRepository.save(user);
        return "Utilisateur enregistré avec succès.";
    }

    public String login(LoginRequest request) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) throw new RuntimeException("Utilisateur non trouvé");

        Utilisateur user = userOpt.get();
        if (!passwordEncoder.matches(request.getMotDePasse(), user.getMot_de_passe()))
            throw new RuntimeException("Mot de passe incorrect");

        return jwtService.generateToken(user.getEmail());
    }
}
