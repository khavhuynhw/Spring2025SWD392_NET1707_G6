package com.net1707.backend.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class AddProductBatchDTO {
    @Positive(message = "Product ID must be greater than zero")
    private Long ProductID;

    @Positive(message = "Quantity must be greater than zero")
    private Integer Quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ImportDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ExpireDate;
}
