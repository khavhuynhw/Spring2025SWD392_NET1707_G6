package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.RefundDTO;
import com.net1707.backend.dto.request.RefundRequestDTO;
import com.net1707.backend.model.Refund;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRefundService {

    RefundDTO requestRefund(Long orderId);
    RefundDTO updateDeliveryRefundStatus(Long refundId, Refund.RefundStatus newStatus, Long staffId);
    RefundDTO processRefund(Long refundId, Long staffId, String proofDocumentUrl);
    RefundDTO verifyOrRejectRefund(Long refundId, Long staffId, boolean isAccepted);
    void deleteRefund(Long refundId);
    RefundDTO getRefundById(Long refundId);
    List<RefundDTO> getAllRefunds();
    void assignDeliveryStaff(Long refundId, Long staffId);
    List<RefundDTO> getRefundsByStaff(Long staffId);
    RefundDTO autoVerifyRefund(Long refundId);
}
