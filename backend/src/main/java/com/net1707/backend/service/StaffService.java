package com.net1707.backend.service;

import com.net1707.backend.dto.StaffDTO;
import com.net1707.backend.dto.exception.ResourceNotFoundException;
import com.net1707.backend.mapper.StaffMapper;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.service.Interface.IStaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService implements IStaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StaffService(StaffRepository staffRepository, StaffMapper staffMapper, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDTO> getAllStaff() {
        logger.debug("Fetching all staff members");
        return staffRepository.findAll().stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffDTO> getAllStaffPaginated(Pageable pageable) {
        logger.debug("Fetching paginated staff members with page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<Staff> staffPage = staffRepository.findAll(pageable);
        List<StaffDTO> staffDtos = staffPage.getContent().stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(staffDtos, pageable, staffPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDTO getStaffById(Long staffId) {
        logger.debug("Fetching staff member with ID: {}", staffId);
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
        return staffMapper.toDto(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StaffDTO> findStaffById(Long staffId) {
        logger.debug("Finding staff member with ID: {}", staffId);
        return staffRepository.findById(staffId)
                .map(staffMapper::toDto);
    }

    @Override
    @Transactional
    public StaffDTO createStaff(StaffDTO staffDto, String password) {
        logger.info("Saving staff member: {}", staffDto.getEmail());

        Staff staff = staffMapper.toEntity(staffDto);

        // Encode password before saving
        if (password != null && !password.isEmpty()) {
            staff.setPassword(passwordEncoder.encode(password));
        }

        Staff savedStaff = staffRepository.save(staff);
        return staffMapper.toDto(savedStaff);
    }

    @Override
    @Transactional
    public StaffDTO updateStaff(Long staffId, StaffDTO staffDto) {
        logger.info("Updating staff member with ID: {}", staffId);

        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        existingStaff.setFullname(staffDto.getFullname());
        existingStaff.setEmail(staffDto.getEmail());
        existingStaff.setPhone(staffDto.getPhone());
        existingStaff.setRole(staffDto.getRole());

        Staff updatedStaff = staffRepository.save(existingStaff);
        return staffMapper.toDto(updatedStaff);
    }

    @Override
    @Transactional
    public void deleteStaff(Long staffId) {
        logger.info("Deleting staff member with ID: {}", staffId);

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        staffRepository.delete(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDTO findByEmail(String email) {
        logger.debug("Finding staff member by email: {}", email);
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + email));
        return staffMapper.toDto(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StaffDTO> findOptionalByEmail(String email) {
        logger.debug("Finding optional staff member by email: {}", email);
        return staffRepository.findByEmail(email)
                .map(staffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        logger.debug("Checking if staff exists with email: {}", email);
        return staffRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDTO> findByRole(String role) {
        logger.debug("Finding staff members by role: {}", role);
        return staffRepository.findByRole(role).stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toList());
    }

}