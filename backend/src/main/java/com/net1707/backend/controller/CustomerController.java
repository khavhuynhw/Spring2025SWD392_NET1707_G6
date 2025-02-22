package com.net1707.backend.controller;

import com.net1707.backend.dto.UpdateInfoUserDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.service.Interface.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    private final IUserService iUserService;


    public CustomerController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PutMapping("/update")
    public ResponseEntity<Customer> updateCustomer(@RequestParam String email, @RequestBody UpdateInfoUserDTO updatedCustomer) {
        return ResponseEntity.ok(iUserService.updateCustomer(email, updatedCustomer));
    }
}
