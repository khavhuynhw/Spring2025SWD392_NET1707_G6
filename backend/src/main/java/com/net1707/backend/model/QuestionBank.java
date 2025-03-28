package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "question_bank")
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ElementCollection
    private List<String> options;

    public enum QuestionType {
        OILINESS,
        DRYNESS,
        SENSITIVITY,
        ACNE,
        AGING,
        HYPERPIGMENTATION,
        REDNESS,
    }
}