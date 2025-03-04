package com.net1707.backend.controller;

import com.net1707.backend.dto.request.EmailRequest;
import com.net1707.backend.dto.response.EmailResponse;
import com.net1707.backend.service.Interface.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/emails")
public class EmailController {
    @Autowired
    private IEmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendSimpleMessage(
                    request
            );

            EmailResponse response = new EmailResponse();
            response.setSuccess(true);
            response.setMessage("Email sent successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EmailResponse response = new EmailResponse();
            response.setSuccess(false);
            response.setMessage("Error sending email: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-html-email")
    public ResponseEntity<EmailResponse> sendHtmlEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendHtmlMessage(
                request
            );

            EmailResponse response = new EmailResponse();
            response.setSuccess(true);
            response.setMessage("HTML email sent successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EmailResponse response = new EmailResponse();
            response.setSuccess(false);
            response.setMessage("Error sending HTML email: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
