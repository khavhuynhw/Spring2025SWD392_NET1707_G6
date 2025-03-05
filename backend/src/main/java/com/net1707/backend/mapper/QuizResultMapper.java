package com.net1707.backend.mapper;

import com.net1707.backend.dto.QuizResultDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.QuizResult;
import com.net1707.backend.repository.CustomerRepository;  // Import CustomerRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QuizResultMapper extends BaseMapper<QuizResultDTO, QuizResult> {

    private final CustomerRepository customerRepository;

    @Autowired
    public QuizResultMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public QuizResultDTO toDto(QuizResult entity) {
        if (entity == null) {
            return null;
        }

        return QuizResultDTO.builder()
                .quizResultId(entity.getQuizResultId())
                .customerId(entity.getCustomer().getCustomerId())
                .recommendedSkinType(entity.getRecommendedSkinType())
                .recommendedConcerns(entity.getRecommendedConcerns())
                .responses(entity.getResponses())
                .takenAt(entity.getTakenAt())
                .build();
    }

    @Override
    public QuizResult toEntity(QuizResultDTO dto) {
        if (dto == null) {
            return null;
        }

        QuizResult quizResult = QuizResult.builder()
                .quizResultId(dto.getQuizResultId())
                .recommendedSkinType(dto.getRecommendedSkinType())
                .recommendedConcerns(dto.getRecommendedConcerns())
                .responses(dto.getResponses())
                .build();

        // Fetch the Customer entity from the database using customerId
        if (dto.getCustomerId() != null) {
            Optional<Customer> customer = customerRepository.findById(dto.getCustomerId());
            customer.ifPresent(quizResult::setCustomer); // Set the customer if found
        }

        return quizResult;
    }
}
