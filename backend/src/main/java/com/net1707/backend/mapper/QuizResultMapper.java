package com.net1707.backend.mapper;

import com.net1707.backend.dto.QuizResultDTO;
import com.net1707.backend.model.QuizResult;
import org.springframework.stereotype.Component;

@Component
public class QuizResultMapper extends BaseMapper<QuizResultDTO, QuizResult> {
    @Override
    public QuizResultDTO toDto(QuizResult entity) {
        return QuizResultDTO.builder()
                .quizResultId(entity.getQuizResultId())
                .customerId(entity.getCustomer().getCustomerId())
                .skinType(entity.getSkinType())
                .questionId(entity.getQuestion().getQuestionId())
                .build();
    }

    @Override
    public QuizResult toEntity(QuizResultDTO dto) {
        return QuizResult.builder()
                .quizResultId(dto.getQuizResultId())
                .skinType(dto.getSkinType())
                .build();
    }
}
