package com.net1707.backend.controller;

import com.net1707.backend.dto.ProductBatchDTO;
import com.net1707.backend.dto.request.ProductBatchRequestDTO;
import com.net1707.backend.service.Interface.IProductBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/productBatch")
@CrossOrigin(origins = "*")
public class ProductBatchController {

    private final IProductBatchService productBatchService;

    public ProductBatchController(IProductBatchService productBatchService) {
        this.productBatchService = productBatchService;
    }

    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody ProductBatchRequestDTO requestDTO) {
        try {
            ProductBatchDTO createdBatch = productBatchService.createBatch(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBatch);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the batch.");
        }
    }

    // Update batch
    @PutMapping("/{batchId}")
    public ResponseEntity<?> updateBatch(@PathVariable Long batchId, @RequestBody ProductBatchRequestDTO requestDTO) {
        try {
            ProductBatchDTO updatedBatch = productBatchService.updateBatch(batchId, requestDTO);
            return ResponseEntity.ok(updatedBatch);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the batch.");
        }
    }

    // Delete batch
    @DeleteMapping("/{batchId}")
    public ResponseEntity<?> deleteBatch(@PathVariable Long batchId) {
        try {
            productBatchService.deleteBatch(batchId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the batch.");
        }
    }

    // Get batch by ID
    @GetMapping("/{batchId}")
    public ResponseEntity<?> getBatchById(@PathVariable Long batchId) {
        try {
            ProductBatchDTO batch = productBatchService.getBatchById(batchId);
            return ResponseEntity.ok(batch);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the batch.");
        }
    }

    // Get all batches
    @GetMapping
    public ResponseEntity<List<ProductBatchDTO>> getAllBatches() {
        List<ProductBatchDTO> batches = productBatchService.getAllBatches();
        if (batches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(batches);
    }
    //get all batch by productId
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductBatchDTO>> getBatchesByProductId(@PathVariable Long productId) {
        List<ProductBatchDTO> batches = productBatchService.getBatchesByProductId(productId);
        return ResponseEntity.ok(batches);
    }
}
