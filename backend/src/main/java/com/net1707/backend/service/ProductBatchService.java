package com.net1707.backend.service;

import com.net1707.backend.dto.AddProductBatchDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.service.Interface.IProductBatchService;
import com.net1707.backend.service.Interface.IProductService;
import org.springframework.stereotype.Service;


@Service
public class ProductBatchService implements IProductBatchService {

    private final ProductBatchRepository productBatchRepository;

    private final IProductService productService;

    public ProductBatchService(ProductBatchRepository productBatchRepository, IProductService productService) {
        this.productBatchRepository = productBatchRepository;
        this.productService = productService;
    }

    @Override
    public ProductBatch addProductBatch(AddProductBatchDTO productBatchDTO) {
        Product product = productService.getProductById(productBatchDTO.getProductID());
        if (product == null) {
            return null;
        }
        else {
            ProductBatch productBatch = new ProductBatch();
            productBatch.setProduct(product);
            productBatch.setQuantity(productBatchDTO.getQuantity());
            productBatch.setImportDate(productBatchDTO.getImportDate());
            productBatch.setExpireDate(productBatchDTO.getExpireDate());
            productService.updateProductStock(productBatchDTO.getProductID(), productBatchDTO.getQuantity());
            return productBatchRepository.save(productBatch);
        }
    }

//    @Override
//    public Optional<ProductBatch> getProductBatchById(Integer productBatchId) {
//        ProductBatch productBatch = productBatchRepository.getById(productBatchId);
//        //return productBatch;
//        return Optional<ProductBatch> productBatch;
//    }
}
