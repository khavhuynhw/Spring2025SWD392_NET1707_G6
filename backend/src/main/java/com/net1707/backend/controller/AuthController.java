package com.net1707.backend.controller;

import com.net1707.backend.dto.response.LoginReponseDTO;
import com.net1707.backend.dto.request.LoginRequestDTO;
import com.net1707.backend.dto.request.RegisterRequestDTO;
import com.net1707.backend.dto.request.StaffRegisterDTO;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.security.JwtUtil;
import com.net1707.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));


            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(new LoginReponseDTO(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/register-staff")
    public ResponseEntity<?> registerStaff(@Valid @RequestBody StaffRegisterDTO staffRegisterDTO) {
        try {
            // Gọi StaffService để xử lý đăng ký
            String result = authService.registerStaff(staffRegisterDTO);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



//    @GetMapping("/login")
//    public ResponseEntity<?> testLogin(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
//        }
//
//        return ResponseEntity.ok("Logged in as: " + authentication.getName());
//    }
}
