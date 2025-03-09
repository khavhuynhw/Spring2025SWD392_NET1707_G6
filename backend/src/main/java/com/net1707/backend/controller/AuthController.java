package com.net1707.backend.controller;

import com.net1707.backend.dto.request.ChangePasswordRequestDTO;
import com.net1707.backend.dto.request.LoginRequestDTO;
import com.net1707.backend.dto.request.RegisterRequestDTO;
import com.net1707.backend.dto.request.StaffRegisterDTO;
import com.net1707.backend.dto.response.LoginResponseDTO;
import com.net1707.backend.service.Interface.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        if(!authService.changePassword(request)){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestChangePassword(@RequestBody Map<String, String> request){
        String email = request.get("email");
        boolean isSent = authService.requestResetPassword(email);
        if (isSent) {
            return ResponseEntity.ok("Verification code sent to email.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send verification code.");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        boolean isValid = authService.validateCode(email, code);
        if (isValid) {
            return ResponseEntity.ok("Code verified. You can reset your password.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code.");
        }
    }
}
