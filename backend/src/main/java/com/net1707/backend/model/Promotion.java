package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String promotionName;
    private String description;
    private float discountPercentage;
    private String startDate;
    private String endDate;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<Order> orders;
}
