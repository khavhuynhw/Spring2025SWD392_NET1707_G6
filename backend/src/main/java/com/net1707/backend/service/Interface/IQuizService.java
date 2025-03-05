package com.net1707.backend.service.Interface;

import com.net1707.backend.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IQuizService {
    QuizResult processQuizResponses(Customer customer, Map<String, String> responses);
    SkinType determineSkinType(Map<String, String> responses);
    Set<SkinConcern> determineSkinConcerns(Map<String, String> responses);
    void updateCustomerProfile(
            Customer customer,
            SkinType skinType,
            Set<SkinConcern> skinConcerns
    );
    List<Product> recommendProducts(QuizResult quizResult);
    List<QuestionBank> getAllQuizQuestions();
}
