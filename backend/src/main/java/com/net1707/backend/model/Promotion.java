package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;
    private String promotionName;
    private String description;
    private BigDecimal discountPercentage;
    private String startDate;
    private String endDate;
    private BigDecimal minimumAmount;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<Order> orders;
}
