package com.jobportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }
    }

    // Application submit hone pe jobseeker ko email
    public void sendApplicationConfirmation(String to,
                                            String name, String jobTitle) {
        String subject = "Application Submitted — " + jobTitle;
        String body = "Hi " + name + ",\n\n" +
                "Your application for '" + jobTitle + "' has been submitted.\n" +
                "You can track your status in your dashboard.\n\n" +
                "Best of luck!\nJob Portal Team";
        sendEmail(to, subject, body);
    }

    // Status update hone pe jobseeker ko email
    public void sendStatusUpdateEmail(String to, String name,
                                      String jobTitle, String status) {
        String subject = "Application Update — " + jobTitle;
        String body = "Hi " + name + ",\n\n" +
                "Your application for '" + jobTitle + "' has been updated.\n" +
                "New Status: " + status + "\n\n" +
                (status.equals("SHORTLISTED") ?
                        "Congratulations! The recruiter will contact you soon.\n\n" :
                        "") +
                "Job Portal Team";
        sendEmail(to, subject, body);
    }
}