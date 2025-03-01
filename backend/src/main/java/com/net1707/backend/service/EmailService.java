package com.net1707.backend.service;

import com.net1707.backend.dto.request.EmailRequest;
import com.net1707.backend.service.Interface.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("matlovedx96@gmail.com");
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        emailSender.send(message);
    }

    @Override
    public void sendHtmlMessage(EmailRequest request) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("matlovedx96@gmail.com");
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            String htmlContent = generateEmailContent(request.getUsername());
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            e.getCause();
        }
        emailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String attachmentPath) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("matlovedx96@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(file.getFilename(), file);
        } catch (MessagingException e) {
            e.getCause();
        }
        emailSender.send(message);
    }

    private String generateEmailContent(String username) {
        return "<html>"
                + "<body>"
                + "<h1>Welcome, " + username + "!</h1>"
                + "<p>Thank you for registering with our service.</p>"
                + "<p>If you have any questions, please contact our support team.</p>"
                + "</body>"
                + "</html>";
    }
}
