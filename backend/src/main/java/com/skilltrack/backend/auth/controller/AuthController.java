package com.skilltrack.backend.auth.controller;

import com.skilltrack.backend.auth.dto.LoginRequest;
import com.skilltrack.backend.auth.dto.RegisterRequest;
import com.skilltrack.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
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
}
