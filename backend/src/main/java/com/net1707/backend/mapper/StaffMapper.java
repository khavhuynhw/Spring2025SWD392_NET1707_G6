package com.net1707.backend.mapper;

import com.net1707.backend.dto.StaffDTO;
import com.net1707.backend.model.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper extends BaseMapper<StaffDTO, Staff> {

    @Override
    public StaffDTO toDto(Staff entity) {
        if (entity == null) {
            return null;
        }
        return StaffDTO.builder()
                .staffId(entity.getStaffId())
                .fullname(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .password(entity.getPassword() != null ? entity.getPassword() : null)
                .role(entity.getRole())
                .build();
    }

    @Override
    public Staff toEntity(StaffDTO dto) {
        if (dto == null) {
            return null;
        }
        return Staff.builder()
                .staffId(dto.getStaffId())
                .fullName(dto.getFullname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(dto.getPassword() != null ? dto.getPassword() : null)
                .role(dto.getRole())
                .build();
    }
}
