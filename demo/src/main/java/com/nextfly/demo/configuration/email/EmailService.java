package com.nextfly.demo.configuration.email;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
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
                        <html lang="it">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Verifica il tuo account</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #f4f4f4;
                                    margin: 0;
                                    padding: 0;
                                }
                                .email-container {
                                    max-width: 600px;
                                    margin: 20px auto;
                                    background: #fff;
                                    border: 1px solid #ddd;
                                    border-radius: 8px;
                                    overflow: hidden;
                                    padding: 20px;
                                }
                                .email-header {
                                    background: #4CAF50;
                                    color: white;
                                    text-align: center;
                                    font-size: 24px;
                                    padding: 10px;
                                    border-radius: 8px 8px 0 0;
                                }
                                .email-body {
                                    padding: 20px;
                                    font-size: 16px;
                                    line-height: 1.5;
                                    color: #333;
                                }
                                .verification-code {
                                    display: inline-block;
                                    background: #4CAF50;
                                    color: white;
                                    padding: 10px 15px;
                                    font-size: 18px;
                                    border-radius: 5px;
                                    margin: 10px 0;
                                }
                                .email-footer {
                                    text-align: center;
                                    font-size: 12px;
                                    color: #777;
                                    margin-top: 20px;
                                }
                            </style>
                        </head>
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
        helper.setFrom("f1998357@gmail.com");
        helper.setText(content, true);

        javaMailSender.send(message);

    }
}
