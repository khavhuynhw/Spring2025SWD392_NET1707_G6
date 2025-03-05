package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    private String productName;
    private String description;
    private BigDecimal price;
    private String category;
    private String skinTypeCompatibility;
    private int stockQuantity;
    private String imageURL;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<SkinType> suitableSkinTypes;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<SkinConcern> targetsConcerns;

    private Boolean suitableForSensitiveSkin;
    private Integer minimumAgeRecommended;
    private Integer maximumAgeRecommended;

    @ElementCollection
    private Set<String> suitableClimateZones;

    private Integer matchScore;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

}
