package com.net1707.backend.dto;

import com.net1707.backend.model.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDTO {
    private Long staffId;
    @NotBlank(message = "Full name cannot be empty")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullname;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(\\+84|0)(3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$",
            message = "Invalid Vietnamese phone number format")
    private String phone;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}
