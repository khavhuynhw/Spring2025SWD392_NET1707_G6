package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "quiz_results")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizResultId;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @ElementCollection
    @CollectionTable(name = "quiz_responses", joinColumns = @JoinColumn(name = "quiz_result_id"))
    @MapKeyColumn(name = "question")
    @Column(name = "response")
    private Map<String, String> responses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkinType recommendedSkinType;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<SkinConcern> recommendedConcerns;

    private LocalDateTime takenAt;

    @PrePersist
    public void prePersist() {
        this.takenAt = LocalDateTime.now();
    }
}