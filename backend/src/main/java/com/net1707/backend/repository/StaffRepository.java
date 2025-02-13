package com.net1707.backend.repository;

import com.net1707.backend.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff,Integer>{
    Optional<Staff> findByEmail(String email);
}
