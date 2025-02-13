package com.net1707.backend.service;

import com.net1707.backend.dto.RegisterRequestDTO;
import com.net1707.backend.dto.StaffRegisterDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
    }

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

    public String registerStaff(StaffRegisterDTO staffRegisterDTO) {
        // Kiểm tra email Staff có tồn tại không
        if (staffRepository.findByEmail(staffRegisterDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Tạo đối tượng Staff mới với dữ liệu từ DTO
        Staff staff = new Staff();
        staff.setFullname(staffRegisterDTO.getFullname());
        staff.setEmail(staffRegisterDTO.getEmail());
        staff.setPhone(staffRegisterDTO.getPhone());
        staff.setRole(staffRegisterDTO.getRole()); // Dùng trực tiếp Enum Role
        staff.setPassword(passwordEncoder.encode(staffRegisterDTO.getPassword())); // Mã hóa mật khẩu

        // Lưu Staff vào cơ sở dữ liệu
        staffRepository.save(staff);

        return "Staff registered successfully";
    }




//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public String register(RegisterRequestDTO request) {
//        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
//            throw new RuntimeException("Username already taken");
//        }
//
//        // Mã hóa mật khẩu trước khi lưu vào DB
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(request.getRole());
//
//        userRepository.save(user);
//        return "User registered successfully!";
//    }


}
