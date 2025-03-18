package com.net1707.backend.service;

import com.net1707.backend.dto.RefundDTO;
import com.net1707.backend.dto.exception.ResourceNotFoundException;
import com.net1707.backend.mapper.RefundMapper;
import com.net1707.backend.model.*;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.repository.RefundRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.service.Interface.IRefundService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefundService implements IRefundService {
    private static final Logger logger = LoggerFactory.getLogger(RefundService.class);

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final StaffRepository staffRepository;
    private final RefundMapper refundMapper;
    private final ProductBatchRepository productBatchRepository;
    @Autowired
    public RefundService(RefundRepository refundRepository, OrderRepository orderRepository, StaffRepository staffRepository,RefundMapper refundMapper, ProductBatchRepository productBatchRepository) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.staffRepository = staffRepository;
        this.refundMapper = refundMapper;
        this.productBatchRepository = productBatchRepository;
    }

    @Override
    @Transactional
    public RefundDTO requestRefund(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order is not in DELIVERED state, cannot request refund.");
        }

        if (refundRepository.findByOrder(order).isPresent()) {
            throw new IllegalStateException("A refund request already exists for this order.");
        }

        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setStatus(Refund.RefundStatus.REQUESTED);
        refund.setAmount(order.getTotalAmount());
        refund.setRefundRequestTime(LocalDateTime.now());

        refund = refundRepository.save(refund);

        logger.info("Customer requested refund for Order ID: {}, Amount: {}, Request Time: {}",
                orderId, refund.getAmount(), refund.getRefundRequestTime());

        return refundMapper.toDto(refund);
    }


    @Transactional
    @Override
    public RefundDTO verifyOrRejectRefund(Long refundId, Long staffId, boolean isAccepted) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));

        if (refund.getStatus() != Refund.RefundStatus.REQUESTED) {
            throw new IllegalStateException("Refund is not in REQUESTED state, cannot verify or reject.");
        }

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + staffId));

        if (staff.getRole() != Role.CUSTOMER_STAFF) {
            throw new IllegalStateException("Only staff with role CUSTOMER_STAFF can verified or rejected refund.");
        }

        refund.setVerifiedByEmployee(staff);
        refund.setStatus(isAccepted ? Refund.RefundStatus.VERIFIED : Refund.RefundStatus.REJECTED);

        return refundMapper.toDto(refundRepository.save(refund));
    }


    @Transactional
    @Override
    public RefundDTO updateDeliveryRefundStatus(Long refundId, Refund.RefundStatus newStatus, Long staffId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + staffId));

        if (staff.getRole() != Role.DELIVERY_STAFF) {
            throw new IllegalStateException("Only staff with role DELIVERY_STAFF can update refund status.");
        }

        if (!(newStatus == Refund.RefundStatus.PICKED_UP || newStatus == Refund.RefundStatus.RETURNED_TO_WAREHOUSE)) {
            throw new IllegalStateException("Invalid status update. Only PICKED_UP or RETURNED_TO_WAREHOUSE allowed.");
        }

        if (refund.getStatus() != Refund.RefundStatus.VERIFIED && newStatus == Refund.RefundStatus.PICKED_UP) {
            throw new IllegalStateException("Refund must be VERIFIED before being picked up.");
        }

        if (refund.getStatus() != Refund.RefundStatus.PICKED_UP && newStatus == Refund.RefundStatus.RETURNED_TO_WAREHOUSE) {
            throw new IllegalStateException("Refund must be PICKED_UP before returning to warehouse.");
        }

        refund.setStatus(newStatus);
        return refundMapper.toDto(refundRepository.save(refund));
    }

    @Transactional
    @Override
    public RefundDTO processRefund(Long refundId, Long staffId, String proofDocumentUrl) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));

        if (refund.getStatus() != Refund.RefundStatus.RETURNED_TO_WAREHOUSE) {
            throw new IllegalStateException("Refund must be RETURNED_TO_WAREHOUSE before processing refund.");
        }

        if (proofDocumentUrl == null || proofDocumentUrl.isEmpty()) {
            throw new IllegalArgumentException("Proof document is required for refunding.");
        }

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + staffId));

        refund.setStatus(Refund.RefundStatus.REFUNDED);
        refund.setRefundCompletionTime(LocalDateTime.now());
        refund.setVerifiedByEmployee(staff);
        refund.setProofDocumentUrl(proofDocumentUrl);


        List<ProductBatch> updatedBatches = new ArrayList<>();

        Order order = refund.getOrder();
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            ProductBatch productBatch = orderDetail.getProductBatch();
            if (productBatch != null) {
                productBatch.setQuantity(productBatch.getQuantity() + orderDetail.getQuantity());
                updatedBatches.add(productBatch);
            }
        }

        productBatchRepository.saveAll(updatedBatches);
        order.setStatus(Order.OrderStatus.REFUNDED);
        orderRepository.save(order);
        return refundMapper.toDto(refundRepository.save(refund));
    }


    @Override
    public List<RefundDTO> getAllRefunds() {
        List<Refund> refunds = refundRepository.findAll();
        return refunds.stream().map(refundMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public RefundDTO getRefundById(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));
        return refundMapper.toDto(refund);
    }


    @Override
    @Transactional
    public void deleteRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));

        if (refund.getStatus() != Refund.RefundStatus.REQUESTED && refund.getStatus() != Refund.RefundStatus.REJECTED) {
            throw new IllegalStateException("Only refunds in REQUESTED or REJECTED status can be deleted.");
        }

        refundRepository.delete(refund);
    }



}
