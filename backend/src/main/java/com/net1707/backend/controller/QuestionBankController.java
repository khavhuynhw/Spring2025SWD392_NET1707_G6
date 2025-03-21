package com.net1707.backend.controller;

import com.net1707.backend.dto.QuestionBankDTO;
import com.net1707.backend.mapper.QuestionBankMapper;
import com.net1707.backend.model.QuestionBank;
import com.net1707.backend.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionBankController {
    private final QuestionBankService questionService;
    private final QuestionBankMapper questionBankMapper;
    @Autowired
    public QuestionBankController(QuestionBankService questionService, QuestionBankMapper questionBankMapper) {
        this.questionService = questionService;
        this.questionBankMapper = questionBankMapper;
    }

    @GetMapping
    public List<QuestionBank> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public QuestionBank getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public ResponseEntity<QuestionBankDTO> addQuestion(@RequestBody QuestionBankDTO question) {
        QuestionBank questionBank = questionService.addQuestion(question);
        QuestionBankDTO questionBankDTO = questionBankMapper.toDto(questionBank);
        return new ResponseEntity<>(questionBankDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return "Question deleted successfully";
    }
    @PutMapping("/{id}")
    public ResponseEntity<QuestionBankDTO> updateQuestion(@PathVariable Long id,@RequestBody QuestionBankDTO questionBankDTO){
        QuestionBank questionBank = questionService.updateQuestion(id, questionBankDTO);
        return new ResponseEntity<>(questionBankMapper.toDto(questionBank),HttpStatus.CREATED);
    }
}
