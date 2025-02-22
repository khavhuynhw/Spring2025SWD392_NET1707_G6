package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.AddProductBatchDTO;
import com.net1707.backend.model.ProductBatch;

import java.util.Optional;

public interface IProductBatchService {
    ProductBatch addProductBatch(AddProductBatchDTO productBatch);
//    Optional<ProductBatch> getProductBatchById(Integer productBatchId);
}
