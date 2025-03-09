package com.net1707.backend.service;


import com.google.common.base.Preconditions;
import com.net1707.backend.dto.request.ChangePasswordRequestDTO;
import com.net1707.backend.dto.request.LoginRequestDTO;
import com.net1707.backend.dto.request.RegisterRequestDTO;
import com.net1707.backend.dto.request.StaffRegisterDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.security.JwtUtil;
import com.net1707.backend.security.UserDetailsImpl;
import com.net1707.backend.service.Interface.CustomerService;
import com.net1707.backend.service.Interface.IAuthService;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService implements IAuthService {

    private final ExpiringMap<String, String> codeStorage = ExpiringMap.builder()
            .expiration(5, TimeUnit.MINUTES) // Code expires after 5 minutes
            .build();
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final EmailService emailService;
    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, StaffRepository staffRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomerService customerService, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerService = customerService;
        this.emailService = emailService;
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

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getRole();
            Long userId = userDetails.getId();

            return jwtUtil.generateToken(userDetails.getUsername(), role, userId);

        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Override
    public boolean changePassword(ChangePasswordRequestDTO dto) {
        Customer user = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Preconditions.checkState(dto.getOldPassword().strip().equals(dto.getOldPassword()), "Password must not contain spaces");
        Preconditions.checkState(dto.getNewPassword().strip().equals(dto.getNewPassword()), "Password must not contain spaces");
        Preconditions.checkState(dto.getNewPassword().length() >= 8, "Password must be at least 8 characters long");
        Preconditions.checkState(dto.getNewPassword().length() <= 30, "Password must not be longer than 30 characters");
        Preconditions.checkState(dto.getNewPassword().equals(dto.getConfirmPassword()), "New password and confirm password must match");

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        customerRepository.save(user);
        return true;
    }

    @Override
    public boolean requestResetPassword(String email) {
        Customer account = customerRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Account not found with email:" + email)
        );
        if(account == null){
            return false;
        }
        String verificationCode = String.valueOf(new Random().nextInt(900000)+100000);
        codeStorage.put(account.getEmail(),verificationCode);
        emailService.sendVeificationCode(email,verificationCode);
        return true;
    }

    @Override
    public boolean validateCode(String email, String code) {
        return code.equals(codeStorage.get(email));
    }

}
