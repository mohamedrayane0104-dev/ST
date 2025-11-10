package com.skilltrack.backend.auth.jwt;

import com.skilltrack.backend.model.Token;
import com.skilltrack.backend.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtService jwtService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Pas de token → laisser Spring gérer (renverra 401)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        // Vérifier en base si token existe
        Optional<Token> storedTokenOpt = tokenRepository.findByToken(jwt);
        if (storedTokenOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non trouvé");
            return;
        }

        Token storedToken = storedTokenOpt.get();

        // Vérifier expiration ou token déjà utilisé
        if (storedToken.isUsed() || storedToken.getExpiration().isBefore(LocalDateTime.now())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expiré ou invalidé");
            return;
        }

        // Vérifier signature JWT
        if (!jwtService.isTokenValid(jwt)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
            return;
        }

        // Authentifier l’utilisateur
        var utilisateur = storedToken.getUtilisateur();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        utilisateur,
                        null,
                        utilisateur.getAuthorities()
                );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
