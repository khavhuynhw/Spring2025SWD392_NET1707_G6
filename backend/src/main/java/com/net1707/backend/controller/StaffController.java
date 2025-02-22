package com.net1707.backend.controller;

import com.net1707.backend.dto.UpdateInfoUserDTO;
import com.net1707.backend.model.Staff;
import com.net1707.backend.service.Interface.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "*")
public class StaffController {
    private final IUserService iUserService;

    public StaffController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PutMapping("/update")
    public ResponseEntity<Staff> updateStaff(@RequestParam String email, @RequestBody UpdateInfoUserDTO updatedStaff) {
        return ResponseEntity.ok(iUserService.updateStaff(email, updatedStaff));
    }

}
