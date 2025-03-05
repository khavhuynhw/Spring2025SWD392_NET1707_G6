package com.net1707.backend.repository;

import com.net1707.backend.model.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank,Long> {
    List<QuestionBank> findByType(QuestionBank.QuestionType type);
}
