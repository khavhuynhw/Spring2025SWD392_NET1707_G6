package com.net1707.backend.service;

import com.net1707.backend.dto.QuestionBankDTO;
import com.net1707.backend.mapper.QuestionBankMapper;
import com.net1707.backend.model.QuestionBank;
import com.net1707.backend.repository.QuestionBankRepository;

import com.net1707.backend.service.Interface.IQuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionBankService implements IQuestionBankService {
    private final QuestionBankRepository questionBankRepository;
    private final QuestionBankMapper questionBankMapper;
    @Autowired
    public QuestionBankService(QuestionBankRepository questionBankRepository, QuestionBankMapper questionBankMapper) {
        this.questionBankRepository = questionBankRepository;
        this.questionBankMapper = questionBankMapper;
    }

    @Override
    public List<QuestionBank> getAllQuestions() {
        return questionBankRepository.findAll();
    }

    @Override
    public QuestionBank getQuestionById(Long id) {
        return questionBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @Override
    public QuestionBank addQuestion(QuestionBankDTO question) {
        QuestionBank questionBank = questionBankMapper.toEntity(question);
        return questionBankRepository.save(questionBank);
    }

    @Override
    public void deleteQuestion(Long id) {
        if (!questionBankRepository.existsById(id)) {
            throw new RuntimeException("Question not found");
        }
        questionBankRepository.deleteById(id);
    }

    @Override
    public QuestionBank updateQuestion(Long id, QuestionBankDTO questionBankDTO) {
        QuestionBank questionBank = questionBankRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot found question with id "+ id));
        questionBank.setQuestion(questionBankDTO.getQuestion());
        questionBank.setType(QuestionBank.QuestionType.valueOf(questionBankDTO.getType()));
        questionBankDTO.setOptions(questionBankDTO.getOptions());
        questionBank.setLabel(questionBankDTO.getLabel());
        return  questionBankRepository.save(questionBank);
    }
}
