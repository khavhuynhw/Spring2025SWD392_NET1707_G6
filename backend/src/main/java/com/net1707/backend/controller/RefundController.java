package com.net1707.backend.controller;

import com.net1707.backend.dto.RefundDTO;
import com.net1707.backend.dto.request.RefundRequestDTO;
import com.net1707.backend.mapper.RefundMapper;
import com.net1707.backend.model.Refund;
import com.net1707.backend.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RefundController {
    private final RefundService refundService;
    private final RefundMapper refundMapper;
    @PostMapping("/verify")
    public ResponseEntity<RefundDTO> verifyRefund(RefundRequestDTO refundRequestDTO) {

        Refund refund = refundService.verifyRefund(refundRequestDTO);
        return ResponseEntity.ok(refundMapper.toDto(refund));
    }
    @PutMapping("init/{orderId}")
    public ResponseEntity<RefundDTO> initiateRefund(@PathVariable Long orderId) {
        Refund refund = refundService.initiateRefund(orderId);
        return ResponseEntity.ok(refundMapper.toDto(refund));
    }
    @PutMapping("process/{refundId}")
    public ResponseEntity<RefundDTO> processRefund(@PathVariable Long refundId) {
        Refund refund = refundService.processRefund(refundId);
        return ResponseEntity.ok(refundMapper.toDto(refund));
    }
}
