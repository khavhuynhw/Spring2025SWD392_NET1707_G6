package com.net1707.backend.repository;

import com.net1707.backend.model.Customer;
import com.net1707.backend.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult,Long> {
    List<QuizResult> findByCustomerOrderByTakenAtDesc(Customer customer);
    QuizResult findTopByCustomerOrderByTakenAtDesc(Customer customer);
}
