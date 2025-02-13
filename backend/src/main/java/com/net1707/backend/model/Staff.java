package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "staffs")
@Getter
@Setter
public class Staff {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffId;

    private String fullname;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role; // Vai trò của staff, ví dụ: ADMIN, MANAGER
    private String password;

}
