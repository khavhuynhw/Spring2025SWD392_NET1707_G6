package com.net1707.backend.controller;

import com.net1707.backend.dto.RefundDTO;
import com.net1707.backend.model.Refund;
import com.net1707.backend.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RefundController {
    private final RefundService refundService;

    @PostMapping("/request/{orderId}")
    public ResponseEntity<RefundDTO> requestRefund(@PathVariable Long orderId) {
        return ResponseEntity.ok(refundService.requestRefund(orderId));
    }

    @PutMapping("/{refundId}/verify")
    public ResponseEntity<RefundDTO> verifyOrRejectRefund(
            @PathVariable Long refundId,
            @RequestParam Long staffId,
            @RequestParam boolean isAccepted) {
        return ResponseEntity.ok(refundService.verifyOrRejectRefund(refundId, staffId, isAccepted));
    }

    @PutMapping("/{refundId}/delivery-status")
    public ResponseEntity<RefundDTO> updateDeliveryRefundStatus(
            @PathVariable Long refundId,
            @RequestParam Refund.RefundStatus newStatus,
            @RequestParam Long staffId) {
        return ResponseEntity.ok(refundService.updateDeliveryRefundStatus(refundId, newStatus,staffId));
    }

    @PutMapping("/{refundId}/process")
    public ResponseEntity<RefundDTO> processRefund(
            @PathVariable Long refundId,
            @RequestParam Long staffId,
            @RequestParam String proofDocumentUrl) {
        return ResponseEntity.ok(refundService.processRefund(refundId, staffId, proofDocumentUrl));
    }


    @GetMapping
    public ResponseEntity<List<RefundDTO>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }


    @GetMapping("/{refundId}")
    public ResponseEntity<RefundDTO> getRefundById(@PathVariable Long refundId) {
        return ResponseEntity.ok(refundService.getRefundById(refundId));
    }


    @DeleteMapping("/{refundId}")
    public ResponseEntity<Void> deleteRefund(@PathVariable Long refundId) {
        refundService.deleteRefund(refundId);
        return ResponseEntity.noContent().build();
    }
}
