package com.skilltrack.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoie un email HTML avec un lien de vérification (sans fichier externe)
     */
    public void sendVerificationEmail(String to, String prenom, String token) {
        try {
            String verifyUrl = "http://localhost:8080/auth/verify-email?token=" + token;

            // ✅ Template HTML directement intégré dans le code
            String htmlContent = """
                <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                color: #333;
                                text-align: center;
                                padding: 40px;
                            }
                            .container {
                                background-color: #fff;
                                border-radius: 10px;
                                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                                display: inline-block;
                                padding: 30px;
                                max-width: 500px;
                            }
                            .btn {
                                display: inline-block;
                                background-color: #4CAF50;
                                color: white;
                                padding: 12px 24px;
                                text-decoration: none;
                                border-radius: 5px;
                                margin-top: 20px;
                                font-weight: bold;
                            }
                            .footer {
                                margin-top: 30px;
                                font-size: 12px;
                                color: #777;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h2>Bienvenue sur <span style='color:#4CAF50;'>SkillTrack</span> 🎯</h2>
                            <p>Bonjour <strong>%s</strong>,</p>
                            <p>Merci de vous être inscrit sur notre plateforme !</p>
                            <p>Cliquez sur le bouton ci-dessous pour vérifier votre adresse e-mail :</p>
                            <a href="%s" class="btn">Vérifier mon compte</a>
                            <p class="footer">Ce lien expirera dans 24 heures.<br/>Si vous n'êtes pas à l'origine de cette demande, ignorez simplement cet e-mail.</p>
                        </div>
                    </body>
                </html>
                """.formatted(prenom, verifyUrl);

            // Création du message HTML
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@skilltrack.com");
            helper.setTo(to);
            helper.setSubject("Vérification de votre compte SkillTrack");
            helper.setText(htmlContent, true); // true => HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
