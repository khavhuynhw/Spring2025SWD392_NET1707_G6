package com.net1707.backend.mapper;

import com.net1707.backend.dto.ProductBatchDTO;
import com.net1707.backend.dto.request.ProductBatchRequestDTO;
import com.net1707.backend.model.ProductBatch;
import org.springframework.stereotype.Component;

@Component
public class ProductBatchMapper extends BaseMapper<ProductBatchDTO, ProductBatch> {
    @Override
    public ProductBatchDTO toDto(ProductBatch entity) {
        return ProductBatchDTO.builder()
                .batchId(entity.getBatchId())
                .productId(entity.getProduct().getProductID()) // Chỉ lấy ID của Product
                .quantity(entity.getQuantity())
                .importDate(entity.getImportDate())
                .expireDate(entity.getExpireDate())
                .build();
    }

    @Override
    public ProductBatch toEntity(ProductBatchDTO dto) {
        return null;
    }

    public ProductBatch toEntity(ProductBatchRequestDTO dto) {
        ProductBatch batch = new ProductBatch();
        batch.setQuantity(dto.getQuantity());
        batch.setImportDate(dto.getImportDate());
        batch.setExpireDate(dto.getExpireDate());
        return batch;
    }
}
