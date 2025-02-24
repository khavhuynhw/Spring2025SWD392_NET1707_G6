package com.net1707.backend.repository;

import com.net1707.backend.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StaffRepository extends JpaRepository<Staff,Long>{
    Optional<Staff> findByEmail(String email);
    List<Staff> findByRole(String role);
}
