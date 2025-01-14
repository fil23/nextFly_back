package com.nextfly.demo.configuration.email;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    private final JavaMailSender javaMailSender;
    private final String CARATTERI = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    private Random random = new Random();

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String generaCod() {
        String cod = "";
        for (int i = 0; i < 5; i++) {
            cod += CARATTERI.charAt(random.nextInt(CARATTERI.length()));
        }

        return cod;
    }

    public void sendMail(String to, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String content = String.format(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>...</head>
                        <body>
                        <div class="email-container">
                            <div class="email-header">Verifica il tuo account</div>
                            <div class="email-body">
                                <p>Ciao,</p>
                                <p>Grazie per esserti registrato alla nostra applicazione. Per completare la registrazione, usa il codice di verifica qui sotto:</p>
                                <div class="verification-code">%s</div>
                                <p>Se non hai richiesto questo codice, ignora questa email.</p>
                                <p>Grazie,<br>Il Team di NextFly</p>
                            </div>
                            <div class="email-footer">&copy; 2025 NextFly. Tutti i diritti riservati.</div>
                        </div>
                        </body>
                        </html>
                        """,
                code);

        helper.setTo(to);
        helper.setSubject("Verification code NextFly");
        helper.setText(content);

        javaMailSender.send(message);

    }
}
