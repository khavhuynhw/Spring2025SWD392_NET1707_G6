package com.net1707.backend.controller;

import com.net1707.backend.dto.LoginResponseDTO;
import com.net1707.backend.dto.RegisterRequestDTO;
import com.net1707.backend.dto.StaffRegisterDTO;
import com.net1707.backend.service.Interface.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO("Invalid email or password"));
        }
    }

    @PostMapping("/register-staff")
    public ResponseEntity<String> registerStaff(@Valid @RequestBody StaffRegisterDTO staffRegisterDTO) {
        try {
            String result = authService.registerStaff(staffRegisterDTO);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
