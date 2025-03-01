package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.request.EmailRequest;

public interface IEmailService {
    void sendSimpleMessage(EmailRequest request);
    void sendHtmlMessage(EmailRequest request);
    void sendMessageWithAttachment(String to, String subject, String text, String attachmentPath);
}
