package com.findify.util;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    public static void sendOTP(String recipientEmail, String otp) {

        final String senderEmail = "harshbudhwani2006@gmail.com";
        final String appPassword = "mlrgkmuxedpwgpps";

        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                senderEmail,
                                appPassword
                        );
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );

            message.setSubject("Findify Password Reset OTP");

            message.setText(
                    "Hello,\n\n"
                    + "Your Findify password reset OTP is:\n\n"
                    + otp
                    + "\n\n"
                    + "This OTP is valid for 5 minutes.\n\n"
                    + "If you did not request a password reset, please ignore this email.\n\n"
                    + "Regards,\n"
                    + "Findify Team"
            );

            Transport.send(message);

            System.out.println("OTP sent successfully to " + recipientEmail);

        } catch (Exception e) {

            System.out.println("EMAIL ERROR:");
            e.printStackTrace();
        }
    }
}