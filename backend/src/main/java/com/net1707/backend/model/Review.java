package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "orderDetailId", nullable = false, unique = true)
    private OrderDetail orderDetail;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 255)
    private String comment;
}
