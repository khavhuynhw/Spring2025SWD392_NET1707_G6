package com.net1707.backend.mapper;

import com.net1707.backend.dto.QuestionBankDTO;
import com.net1707.backend.model.QuestionBank;
import org.springframework.stereotype.Component;

@Component
public class QuestionBankMapper extends BaseMapper<QuestionBankDTO,QuestionBank>{
    @Override
    public QuestionBankDTO toDto(QuestionBank questionBank) {
        return QuestionBankDTO.builder()
                .questionId(questionBank.getQuestionId())
                .question(questionBank.getQuestion())
                .type(questionBank.getType())
                .build();
    }
    @Override
    public QuestionBank toEntity(QuestionBankDTO dto) {
        return QuestionBank.builder()
                .questionId(dto.getQuestionId())
                .question(dto.getQuestion())
                .type(dto.getType())
                .build();
    }
}
