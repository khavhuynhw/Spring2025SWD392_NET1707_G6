package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staffs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Staff {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    private String fullName;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role; // Vai trò của staff, ví dụ: ADMIN, MANAGER
    private String password;

}
