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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
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
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= VERIFY EMAIL =================
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String resultMessage;
        try {
            resultMessage = authService.verifyEmail(token);
        } catch (RuntimeException e) {
            resultMessage = e.getMessage();
        }

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
        try {
            authService.logout(token);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "timestamp", LocalDateTime.now(),
                    "message", "Déconnexion réussie"
            ));
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return buildErrorResponse("Token manquant ou mal formé", HttpStatus.BAD_REQUEST);
        }

        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (Exception e) {
            return buildErrorResponse("Token invalide", HttpStatus.UNAUTHORIZED);
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        if (utilisateurOpt.isPresent()) {
            return ResponseEntity.ok(utilisateurOpt.get());
        } else {
            return buildErrorResponse("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        }
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
