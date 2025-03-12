package com.net1707.backend.dto;

import com.net1707.backend.model.SkinConcern;
import com.net1707.backend.model.SkinType;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long customerId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Integer ageRange;
    private SkinType primarySkinType;
    private Set<SkinConcern> skinConcerns;
    private Boolean isSensitive;
    private String climateZone;
    private boolean isVisible;
}
