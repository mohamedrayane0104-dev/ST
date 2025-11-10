package com.skilltrack.backend.auth.controller;

import com.skilltrack.backend.auth.dto.LoginRequest;
import com.skilltrack.backend.auth.dto.RegisterRequest;
import com.skilltrack.backend.auth.service.AuthService;
import com.skilltrack.backend.auth.jwt.JwtService;
import com.skilltrack.backend.model.Utilisateur;
import com.skilltrack.backend.model.Token;
import com.skilltrack.backend.repository.UtilisateurRepository;
import com.skilltrack.backend.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final TokenRepository tokenRepository;
    private final AuthService authService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String message = authService.register(request);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "timestamp", LocalDateTime.now(),
                    "message", message
            ));
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "timestamp", LocalDateTime.now(),
                    "token", token
            ));
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // ================= VERIFY EMAIL =================
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String resultMessage;
        try {
            resultMessage = authService.verifyEmail(token); // vérifie et active l'utilisateur
        } catch (RuntimeException e) {
            resultMessage = e.getMessage();
        }

        // Retour HTML simple avec un lien vers React login
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body("<!DOCTYPE html><html><body style='font-family:sans-serif;text-align:center;margin-top:50px;'>"
                        + "<h2 style='color:#1976d2;'>" + resultMessage + "</h2>"
                        + "<br><a href='http://localhost:3000/login' "
                        + "style='color:#1976d2;text-decoration:none;font-weight:bold;'>Revenir à SkillTrack</a>"
                        + "</body></html>");
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return buildErrorResponse("Token manquant ou mal formé", HttpStatus.BAD_REQUEST);
        }

        String token = authHeader.substring(7);

        // Vérifier si le token existe et est valide
        Optional<Token> storedTokenOpt = tokenRepository.findByToken(token);
        if (storedTokenOpt.isEmpty() || storedTokenOpt.get().isUsed()
                || storedTokenOpt.get().getExpiration().isBefore(LocalDateTime.now())) {
            return buildErrorResponse("Token invalide ou expiré", HttpStatus.UNAUTHORIZED);
        }

        // Invalider le token
        authService.logout(token); // doit marquer le token comme utilisé dans DB

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "timestamp", LocalDateTime.now(),
                "message", "Déconnexion réussie"
        ));
    }

    // ================= UTIL =================
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }
}
