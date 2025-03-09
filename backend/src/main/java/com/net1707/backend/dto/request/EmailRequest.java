package com.net1707.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String text;
    private String htmlContent;
    private String username;
    private String email;
    private String code;
}
