package com.net1707.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResultDTO {
    private Long quizResultId;
    private Long customerId;  // Reference to Customer ID
    private String skinType;
    private Long questionId;  // Reference to QuestionBank ID
}
