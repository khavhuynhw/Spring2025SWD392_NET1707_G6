package com.net1707.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReponseDTO {

    private String token;

    public LoginReponseDTO(String token) {
        this.token = token;
    }


}
