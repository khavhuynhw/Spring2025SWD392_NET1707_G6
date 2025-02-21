package com.net1707.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}


