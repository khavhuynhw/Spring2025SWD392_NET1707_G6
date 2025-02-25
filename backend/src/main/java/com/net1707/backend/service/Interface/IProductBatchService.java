package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.ProductBatchDTO;
import com.net1707.backend.dto.request.ProductBatchRequestDTO;

import java.util.List;


public interface IProductBatchService {
    ProductBatchDTO createBatch(ProductBatchRequestDTO requestDTO);
    ProductBatchDTO updateBatch(Long batchId, ProductBatchRequestDTO requestDTO);
    void deleteBatch(Long batchId);
    ProductBatchDTO getBatchById(Long batchId);
    List<ProductBatchDTO> getAllBatches();
}
