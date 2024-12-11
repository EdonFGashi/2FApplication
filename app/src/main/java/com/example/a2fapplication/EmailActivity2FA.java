package com.example.a2fapplication;

import android.util.Log;
import android.widget.Toast;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailActivity2FA {
    private static final String USERNAME = ""; // Sender's email
    private static final String PASSWORD = ""; // Sender's email password
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String SUBJECT = "Your 2FA code: ";

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void sendVerificationCode(String recipientEmail, String code, String SUBJECT, String TEXT) {
        executorService.submit(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_SERVER);
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            try {
                Log.i("EmailActivity2FA", "Creating email message...");
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(USERNAME));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(SUBJECT);
                message.setText(TEXT + " " + code);

                Log.i("EmailActivity2FA", "Sending email...");
                Transport.send(message);  // This is where the email is sent

                Log.d("EmailActivity2FA", "Verification email sent");

            } catch (MessagingException e) {
                Log.e("EmailActivity2FA", "MessagingException occurred", e);
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("EmailActivity2FA", "General exception occurred", e);
                e.printStackTrace();
            }
        });
    }
}
