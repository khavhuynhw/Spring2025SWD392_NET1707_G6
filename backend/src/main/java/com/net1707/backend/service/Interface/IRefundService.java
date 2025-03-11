package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.RefundDTO;
import com.net1707.backend.dto.request.RefundRequestDTO;
import com.net1707.backend.model.Refund;
import org.springframework.web.multipart.MultipartFile;

public interface IRefundService {
    Refund initiateRefund(Long orderId);

    Refund  processRefund(Long refundId);
    Refund verifyRefund(RefundRequestDTO refundRequestDTO);
}
