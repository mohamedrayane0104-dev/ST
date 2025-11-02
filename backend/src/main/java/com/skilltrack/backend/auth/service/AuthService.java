package com.skilltrack.backend.auth.service;

import com.skilltrack.backend.auth.dto.LoginRequest;
import com.skilltrack.backend.auth.dto.RegisterRequest;
import com.skilltrack.backend.auth.jwt.JwtService;
import com.skilltrack.backend.model.Token;
import com.skilltrack.backend.model.Utilisateur;
import com.skilltrack.backend.repository.TokenRepository;
import com.skilltrack.backend.repository.UtilisateurRepository;
import com.skilltrack.backend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final MailService mailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 🔹 Inscription + génération du token + envoi du mail HTML
     */
    public String register(RegisterRequest request) {
        // Vérifie si l’email est déjà utilisé
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("❌ Cet e-mail est déjà utilisé. Veuillez en choisir un autre.");
        }

        // Création de l’utilisateur
        Utilisateur user = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .mot_de_passe(passwordEncoder.encode(request.getMotDePasse()))
                .emailVerified(false)
                .build();

        utilisateurRepository.save(user);

        // Création du token de vérification valable 24h
        String tokenValue = generateVerificationToken();
        Token token = Token.builder()
                .token(tokenValue)
                .expiration(LocalDateTime.now().plusHours(24))
                .used(false)
                .utilisateur(user)
                .build();

        tokenRepository.save(token);

        // Envoi du mail HTML avec le lien de vérification
        mailService.sendVerificationEmail(user.getEmail(), user.getPrenom(), tokenValue);

        return "✅ Inscription réussie. Veuillez vérifier votre e-mail pour activer votre compte.";
    }

    /**
     * 🔹 Vérifie le token reçu dans le lien de vérification
     */
    public String verifyEmail(String tokenValue) {
        Token token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("❌ Lien de vérification invalide."));

        if (token.isUsed()) {
            throw new RuntimeException("⚠️ Ce lien a déjà été utilisé.");
        }

        if (token.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("⏰ Le lien de vérification a expiré. Veuillez demander un nouveau lien.");
        }

        Utilisateur user = token.getUtilisateur();
        user.setEmailVerified(true);
        utilisateurRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);

        return "✅ Votre compte a été vérifié avec succès.";
    }

    /**
     * 🔹 Connexion utilisateur (autorisé uniquement si email vérifié)
     */
    public String login(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("❌ Aucun utilisateur trouvé avec cet e-mail."));

        if (!passwordEncoder.matches(request.getMotDePasse(), user.getMot_de_passe())) {
            throw new RuntimeException("❌ Mot de passe incorrect.");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("⚠️ Votre compte n’est pas encore vérifié. Veuillez consulter votre e-mail.");
        }

        // 🔹 1. Génération du JWT
        String jwt = jwtService.generateToken(user.getEmail());

        // 🔹 2. (Optionnel) Invalider les anciens tokens de cet utilisateur
        tokenRepository.deleteByUtilisateur(user);

        // 🔹 3. Sauvegarder le nouveau token JWT dans la table
        Token token = Token.builder()
                .token(jwt)
                .expiration(LocalDateTime.now().plusHours(24))
                .used(false)
                .utilisateur(user)
                .build();

        tokenRepository.save(token);

        // 🔹 4. Retourner le JWT au frontend
        return jwt;
    }


    /**
     * 🔹 Génération d’un token de vérification sécurisé (base64)
     */
    private String generateVerificationToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    // Méthode logout pour déconnexion
    public void logout(String jwtToken) {
        tokenRepository.findByToken(jwtToken).ifPresent(token -> {
            token.setUsed(true); // ou tokenRepository.delete(token);
            tokenRepository.save(token);
        });
    }
}
