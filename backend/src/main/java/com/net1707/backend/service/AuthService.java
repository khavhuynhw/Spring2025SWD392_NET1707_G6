package com.net1707.backend.service;

import com.net1707.backend.dto.RegisterRequestDTO;
import com.net1707.backend.dto.StaffRegisterDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.security.JwtUtil;
import com.net1707.backend.service.Interface.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, StaffRepository staffRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String register(RegisterRequestDTO request) {
        if(customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);
        return "Customer registered successfully!";
    }

    @Override
    public String registerStaff(StaffRegisterDTO staffRegisterDTO) {
        // Check email exist
        if (staffRepository.findByEmail(staffRegisterDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Create Staff
        Staff staff = new Staff();
        staff.setFullname(staffRegisterDTO.getFullname());
        staff.setEmail(staffRegisterDTO.getEmail());
        staff.setPhone(staffRegisterDTO.getPhone());
        staff.setRole(staffRegisterDTO.getRole()); // Use Role Enum
        staff.setPassword(passwordEncoder.encode(staffRegisterDTO.getPassword())); // Encrypt Password

        // Save Staff DB
        staffRepository.save(staff);

        return "Staff registered successfully";
    }

    @Override
    public String login(LoginRequestDTO loginRequestDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            return jwtUtil.generateToken(loginRequestDTO.getEmail());

        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }


}
