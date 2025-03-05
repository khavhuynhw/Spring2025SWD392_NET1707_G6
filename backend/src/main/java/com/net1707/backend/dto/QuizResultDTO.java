package com.net1707.backend.dto;

import com.net1707.backend.model.SkinConcern;
import com.net1707.backend.model.SkinType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResultDTO {
    private Long quizResultId;
    private Long customerId;  // Changed to Long
    private SkinType recommendedSkinType;
    private Set<SkinConcern> recommendedConcerns;
    private Map<String, String> responses;
    private LocalDateTime takenAt;
}
