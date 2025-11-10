package com.skilltrack.backend.user.controller;

import com.skilltrack.backend.auth.jwt.JwtService;
import com.skilltrack.backend.model.Utilisateur;
import com.skilltrack.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final com.skilltrack.backend.service.MailService mailService;


    // ==================== GET PROFILE ====================
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

    // ==================== UPDATE PROFILE ====================
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> updates
    ) {
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
        if (utilisateurOpt.isEmpty()) {
            return buildErrorResponse("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (updates.containsKey("nom")) utilisateur.setNom((String) updates.get("nom"));
        if (updates.containsKey("prenom")) utilisateur.setPrenom((String) updates.get("prenom"));
        // ⚠️ tu peux ajouter d’autres champs mais évite mot_de_passe ici sauf si tu as un flux dédié

        utilisateurRepository.save(utilisateur);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "timestamp", LocalDateTime.now(),
                "message", "Profil mis à jour avec succès"
        ));
    }

    // ==================== DELETE REQUEST ====================
    @PostMapping("/delete-request")
    public ResponseEntity<?> requestDeleteAccount(@RequestHeader("Authorization") String authHeader) {
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
        if (utilisateurOpt.isEmpty()) {
            return buildErrorResponse("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        }

        Utilisateur utilisateur = utilisateurOpt.get();
        String deleteToken = java.util.UUID.randomUUID().toString();

        utilisateur.setDeleteToken(deleteToken);
        utilisateur.setDeleteTokenExpiration(LocalDateTime.now().plusHours(1));
        utilisateurRepository.save(utilisateur);

        mailService.sendDeleteConfirmationEmail(utilisateur.getEmail(), utilisateur.getPrenom(), deleteToken);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "timestamp", LocalDateTime.now(),
                "message", "📧 Un e-mail de confirmation a été envoyé pour supprimer votre compte."
        ));
    }

    // ==================== CONFIRM DELETE ====================
    @GetMapping("/confirm-delete")
    public ResponseEntity<String> confirmDelete(@RequestParam("token") String token) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByDeleteToken(token);

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("<h3>❌ Lien invalide ou déjà utilisé.</h3>");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (utilisateur.getDeleteTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("<h3>⏰ Le lien de suppression a expiré.</h3>");
        }

        utilisateurRepository.delete(utilisateur);

        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body("<html><body style='font-family:sans-serif;text-align:center;margin-top:50px;'>"
                        + "<h2 style='color:#d9534f;'>Compte supprimé avec succès 💀</h2>"
                        + "<p>Nous sommes tristes de vous voir partir.</p>"
                        + "<a href='http://localhost:3000/register' style='color:#1976d2;text-decoration:none;font-weight:bold;'>Créer un nouveau compte</a>"
                        + "</body></html>");
    }

    // ==================== UTIL ====================
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }
}
