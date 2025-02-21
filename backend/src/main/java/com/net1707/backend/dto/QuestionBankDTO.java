package com.net1707.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankDTO {

    private Long questionId;

    @NotBlank(message = "Question cannot be blank")
    @Size(max = 1000, message = "Question must be less than 1000 characters")
    private String question;

    @NotBlank(message = "Type cannot be blank")
    @Size(max = 50, message = "Type must be less than 50 characters")
    private String type;
}