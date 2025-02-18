package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @Column(nullable = false, length = 255)
    private String paymentMethod;

    @Column(nullable = false)
    private Float amount;

    @Column(nullable = false)
    private Date paymentDate;

    @Column(nullable = false, length = 255)
    private String paymentStatus;

}
