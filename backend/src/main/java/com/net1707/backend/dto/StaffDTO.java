package com.net1707.backend.dto;

import com.net1707.backend.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDTO {
    private Long staffId;
    private String fullname;
    private String email;
    private String phone;
    private Role role; // Role enum (e.g., ADMIN, MANAGER)
}
