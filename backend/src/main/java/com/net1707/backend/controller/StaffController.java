package com.net1707.backend.controller;

import com.net1707.backend.dto.StaffDTO;
import com.net1707.backend.dto.UpdateInfoUserDTO;
import com.net1707.backend.model.Staff;
import com.net1707.backend.service.Interface.IStaffService;
import com.net1707.backend.service.Interface.IUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "*")
public class StaffController {
    private final IUserService iUserService;
    private final IStaffService staffService;
    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);
    @Autowired
    public StaffController(IUserService iUserService, IStaffService staffService) {
        this.iUserService = iUserService;
        this.staffService = staffService;
    }

    @PutMapping("/update")
    public ResponseEntity<Staff> updateStaff(@RequestParam String email, @RequestBody UpdateInfoUserDTO updatedStaff) {
        return ResponseEntity.ok(iUserService.updateStaff(email, updatedStaff));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        logger.info("REST request to get all staff members");
        List<StaffDTO> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<StaffDTO>> getAllStaffPaginated(Pageable pageable) {
        logger.info("REST request to get paginated staff members with page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<StaffDTO> staffPage = staffService.getAllStaffPaginated(pageable);
        return ResponseEntity.ok(staffPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable("id") Long id) {
        logger.info("REST request to get staff member with ID: {}", id);
        StaffDTO staffDTO = staffService.getStaffById(id);
        return ResponseEntity.ok(staffDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StaffDTO> createStaff(@RequestBody StaffDTO staffDTO,
                                                @RequestParam(required = false) String password) {
        logger.info("REST request to create a new staff member: {}", staffDTO.getEmail());
        StaffDTO createdStaff = staffService.createStaff(staffDTO, password);
        return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
    }
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
//    @PutMapping("/{id}")
//    public ResponseEntity<StaffDTO> updateStaff(@PathVariable("id") Long id,
//                                                @Valid @RequestBody StaffDTO staffDTO) {
//        logger.info("REST request to update staff member with ID: {}", id);
//        StaffDTO updatedStaff = staffService.updateStaff(id, staffDTO);
//        return ResponseEntity.ok(updatedStaff);
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable("id") Long id) {
        logger.info("REST request to delete staff member with ID: {}", id);
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-email")
    public ResponseEntity<StaffDTO> getStaffByEmail(@RequestParam String email) {
        logger.info("REST request to get staff member by email: {}", email);
        StaffDTO staffDTO = staffService.findByEmail(email);
        return ResponseEntity.ok(staffDTO);
    }

    @GetMapping("/exists-by-email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        logger.info("REST request to check if staff exists with email: {}", email);
        boolean exists = staffService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/by-role")
    public ResponseEntity<List<StaffDTO>> getStaffByRole(@RequestParam String role) {
        logger.info("REST request to get staff members by role: {}", role);
        List<StaffDTO> staffList = staffService.findByRole(role);
        return ResponseEntity.ok(staffList);
    }

}
