package com.net1707.backend.controller;

import com.net1707.backend.dto.AddProductBatchDTO;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.service.Interface.IProductBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productBatch")
@CrossOrigin(origins = "*")
public class ProductBatchController {

    private final IProductBatchService productBatchService;

    public ProductBatchController(IProductBatchService productBatchService) {
        this.productBatchService = productBatchService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProductBatch> addProductBatch(AddProductBatchDTO productBatchDTO) {
        ProductBatch newProductBatch = productBatchService.addProductBatch(productBatchDTO);
        return ResponseEntity.ok(newProductBatch);
    }
}
