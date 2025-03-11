package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.StaffDTO;
import com.net1707.backend.model.Role;
import com.net1707.backend.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IStaffService {
    List<StaffDTO> getAllStaff();

    Page<StaffDTO> getAllStaffPaginated(Pageable pageable);

    StaffDTO getStaffById(Long staffId);

    Optional<StaffDTO> findStaffById(Long staffId);

    StaffDTO createStaff(StaffDTO staffDto, String password);

    StaffDTO updateStaff(Long staffId, StaffDTO staffDto);

    void deleteStaff(Long staffId);

    StaffDTO findByEmail(String email);

    Optional<StaffDTO> findOptionalByEmail(String email);

    boolean existsByEmail(String email);

    List<StaffDTO> findByRole(String role);


}