package com.net1707.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String password;

}
