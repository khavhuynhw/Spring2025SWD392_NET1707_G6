package com.net1707.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    public enum RefundStatus {
        REQUESTED, PROCESSING, COMPLETED, VERIFIED, REJECTED
    }

    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime refundRequestTime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime refundCompletionTime;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff verifiedByEmployee;

    private String proofDocumentUrl;


}
