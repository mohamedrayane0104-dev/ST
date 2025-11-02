package com.skilltrack.backend.auth.controller;

import com.skilltrack.backend.auth.dto.LoginRequest;
import com.skilltrack.backend.auth.dto.RegisterRequest;
import com.skilltrack.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.skilltrack.backend.auth.jwt.JwtService;
import com.skilltrack.backend.model.Utilisateur;
import com.skilltrack.backend.repository.UtilisateurRepository;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        // ✅ La méthode verifyEmail() renvoie un message texte maintenant
        String resultMessage = authService.verifyEmail(token);

        // Page HTML de retour conviviale
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body("<!DOCTYPE html><html><body style='font-family:sans-serif;text-align:center;margin-top:50px;'>"
                        + "<h2 style='color:#1976d2;'>" + resultMessage + "</h2>"
                        + "<br><a href='http://localhost:3000' "
                        + "style='color:#1976d2;text-decoration:none;font-weight:bold;'>Revenir à SkillTrack</a>"
                        + "</body></html>");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token manquant ou mal formé");
        }

        String token = authHeader.substring(7); // retire "Bearer "
        authService.logout(token);

        return ResponseEntity.ok("Déconnexion réussie");
    }

    // Route protégée pour tester
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token manquant ou mal formé");
        }

        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token invalide");
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        if (utilisateurOpt.isPresent()) {
            return ResponseEntity.ok(utilisateurOpt.get());
        } else {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }
    }



}
