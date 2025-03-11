package com.net1707.backend.service;

import com.net1707.backend.dto.exception.ResourceNotFoundException;
import com.net1707.backend.dto.request.RefundRequestDTO;
import com.net1707.backend.mapper.RefundMapper;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.Refund;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.RefundRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.service.Interface.IRefundService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefundService implements IRefundService {
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final StaffRepository staffRepository;
    @Autowired
    public RefundService(RefundRepository refundRepository, OrderRepository orderRepository, StaffRepository staffRepository) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.staffRepository = staffRepository;
    }
    @Transactional
    @Override
    public Refund initiateRefund(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order id cannot found with id:" + orderId));

        if (order.getStatus() != Order.OrderStatus.RETURNED){
            throw new IllegalStateException("Order is not in refundable state");
        }
        Optional<Refund> existingRefund  = refundRepository.findById(orderId);
        if (existingRefund .isPresent()){
            return existingRefund.get();
        }

        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setStatus(Refund.RefundStatus.REQUESTED);
        refund.setAmount(order.getTotalAmount());
        refund.setRefundRequestTime(LocalDateTime.now());

        order.setStatus(Order.OrderStatus.PENDING);
        order.setRefund(refund);

        orderRepository.save(order);
        return refundRepository.save(refund);
    }

    @Transactional
    @Override
    public Refund processRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));

        if (refund.getStatus() != Refund.RefundStatus.REQUESTED) {
            throw new IllegalStateException("Refund is not in REQUESTED state");
        }
        refund.setStatus(Refund.RefundStatus.COMPLETED);
        refund.setRefundCompletionTime(LocalDateTime.now());


       return refundRepository.save(refund);
    }
    @Transactional
    @Override
    public Refund verifyRefund(RefundRequestDTO request) {
        Refund refund = refundRepository.findById(request.getRefundId())
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        if (refund.getStatus() != Refund.RefundStatus.COMPLETED) {
            throw new IllegalStateException("Refund is not in COMPLETED state for verification");
        }
        refund.setStatus(Refund.RefundStatus.VERIFIED);
        refund.setVerifiedByEmployee(staff);
        refund.setProofDocumentUrl(request.getProofDocument());


        Order order = refund.getOrder();
        order.setStatus(Order.OrderStatus.REFUNDED);
        orderRepository.save(order);

        return refundRepository.save(refund);
    }


}
