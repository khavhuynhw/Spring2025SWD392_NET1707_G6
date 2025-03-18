package com.net1707.backend.mapper;

import com.net1707.backend.dto.RefundDTO;
import com.net1707.backend.dto.exception.ResourceNotFoundException;
import com.net1707.backend.model.Refund;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefundMapper extends BaseMapper<RefundDTO, Refund>{
    private final OrderRepository orderRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public RefundMapper(OrderRepository orderRepository, StaffRepository staffRepository) {
        this.orderRepository = orderRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public RefundDTO toDto(Refund entity) {
        if (entity == null) {
            return null;
        }

        return RefundDTO.builder()
                .id(entity.getId())
                .orderId(entity.getOrder() != null ? entity.getOrder().getOrderId() : null)
                .status(entity.getStatus())
                .amount(entity.getAmount())
                .refundRequestTime(entity.getRefundRequestTime())
                .refundCompletionTime(entity.getRefundCompletionTime())
                .verifiedByEmployeeId(entity.getVerifiedByEmployee() != null ? entity.getVerifiedByEmployee().getStaffId() : null)
                .proofDocumentUrl(entity.getProofDocumentUrl())
                .build();
    }

    @Override
    public Refund toEntity(RefundDTO dto) {
        if (dto == null) {
            return null;
        }

        Refund refund = new Refund();


        if (dto.getOrderId() != null) {
            refund.setOrder(orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order Not Found")));
        }


        if (dto.getVerifiedByEmployeeId() != null) {
            Staff staff = staffRepository.findById(dto.getVerifiedByEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff Not Found"));
            refund.setVerifiedByEmployee(staff);
        }

        refund.setAmount(dto.getAmount());


        if (dto.getStatus() != null) {
            refund.setStatus(dto.getStatus());
        } else {
            throw new IllegalArgumentException("Refund status cannot be null");
        }

        refund.setRefundRequestTime(dto.getRefundRequestTime());
        refund.setRefundCompletionTime(dto.getRefundCompletionTime());
        refund.setProofDocumentUrl(dto.getProofDocumentUrl());

        return refund;
    }

}
