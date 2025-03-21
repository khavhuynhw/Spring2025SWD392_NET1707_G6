package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.QuestionBankDTO;
import com.net1707.backend.model.QuestionBank;

import java.util.List;

public interface IQuestionBankService {
    List<QuestionBank> getAllQuestions();
    QuestionBank getQuestionById(Long id);
    QuestionBank addQuestion(QuestionBankDTO question);
    void deleteQuestion(Long id);

    QuestionBank updateQuestion(Long id,QuestionBankDTO questionBankDTO);
}