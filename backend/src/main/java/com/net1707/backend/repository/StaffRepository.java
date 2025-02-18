package com.net1707.backend.repository;

import com.net1707.backend.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Integer>{
    Optional<Staff> findByEmail(String email);
}
