package com.net1707.backend.service;

import com.net1707.backend.dto.request.EmailRequest;
import com.net1707.backend.model.Customer;
import com.net1707.backend.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender emailSender;

    public EmailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void sendSimpleMessage(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("matlovedx96@gmail.com");
        message.setTo(request.getTo());
        message.setSubject("Password set verification code");
        message.setText("Your verification code is:" + request.getCode());
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

    @Override
    public void sendVeificationCode(String email, String code) {
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Send verification code error"));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("matlovedx96@gmail.com");
        message.setTo(customer.getEmail());
        message.setSubject("Password set verification code");
        message.setText("Your verification code is:" + code + "This code expire in 5 minutes");
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
