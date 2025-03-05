package com.net1707.backend.controller;

import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.QuestionBank;
import com.net1707.backend.model.QuizResult;
import com.net1707.backend.service.Interface.CustomerService;
import com.net1707.backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final CustomerService customerService;

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionBank>> getAllQuestions() {
        return ResponseEntity.ok(quizService.getAllQuizQuestions());
    }
    @PostMapping("/submit/{customerId}")
    public ResponseEntity<?> submitQuiz(
            @PathVariable Long customerId,
            @RequestBody Map<String, String> responses
    ) {
        // Fetch customer
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Process quiz responses
        QuizResult quizResult = quizService.processQuizResponses(customer, responses);

        // Recommend products
        List<Product> recommendedProducts = quizService.recommendProducts(quizResult);

        return ResponseEntity.ok(Map.of(
                "quizResult", quizResult,
                "recommendedProducts", recommendedProducts
        ));
    }
}

