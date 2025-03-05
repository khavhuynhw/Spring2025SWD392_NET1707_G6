package com.net1707.backend.service;

import com.net1707.backend.dto.ProductBatchDTO;
import com.net1707.backend.dto.request.ProductBatchRequestDTO;
import com.net1707.backend.mapper.ProductBatchMapper;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.IProductBatchService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ProductBatchService implements IProductBatchService {

    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchMapper productBatchMapper;
    private final ProductRepository productRepository ;

    public ProductBatchService(ProductBatchRepository productBatchRepository, ProductBatchMapper productBatchMapper, ProductRepository productRepository) {
        this.productBatchRepository = productBatchRepository;
        this.productBatchMapper = productBatchMapper;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductBatchDTO createBatch(ProductBatchRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductBatch batch = productBatchMapper.toEntity(requestDTO);
        batch.setProduct(product);


        product.setStockQuantity(product.getStockQuantity() + requestDTO.getQuantity());
        productRepository.save(product);

        ProductBatch savedBatch = productBatchRepository.save(batch);

        return productBatchMapper.toDto(savedBatch);
    }

    @Override
    @Transactional
    public ProductBatchDTO updateBatch(Long batchId, ProductBatchRequestDTO requestDTO) {
        ProductBatch batch = productBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        Product product = batch.getProduct(); // get product from batch

        //  Calculate the quantity difference
        int quantityDifference = requestDTO.getQuantity() - batch.getQuantity();

        // Just update value change
        if (!Objects.equals(requestDTO.getQuantity(), batch.getQuantity())) {
            batch.setQuantity(requestDTO.getQuantity());
        }
        if (!Objects.equals(requestDTO.getImportDate(), batch.getImportDate())) {
            batch.setImportDate(requestDTO.getImportDate());
        }
        if (!Objects.equals(requestDTO.getExpireDate(), batch.getExpireDate())) {
            batch.setExpireDate(requestDTO.getExpireDate());
        }

        // update stockQuantity of Product
        product.setStockQuantity(product.getStockQuantity() + quantityDifference);
        productRepository.save(product);

        // save batch when update
        ProductBatch updatedBatch = productBatchRepository.save(batch);
        return productBatchMapper.toDto(updatedBatch);
    }

    @Override
    @Transactional
    public void deleteBatch(Long batchId) {
        ProductBatch batch = productBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        Product product = batch.getProduct(); // get product from batch

        // reduce stockQuantity when delete batch
        product.setStockQuantity(product.getStockQuantity() - batch.getQuantity());
        productRepository.save(product);

        // delete batch
        productBatchRepository.deleteById(batchId);
    }

    @Override
    public ProductBatchDTO getBatchById(Long batchId) {
        ProductBatch batch = productBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        return productBatchMapper.toDto(batch);
    }

    @Override
    public List<ProductBatchDTO> getAllBatches() {
        return productBatchRepository.findAll().stream()
                .map(productBatchMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProductBatchDTO> getBatchesByProductId(Long productId) {
        List<ProductBatch> batches = productBatchRepository.findByProduct_ProductID(productId);
        return batches.stream()
                .map(productBatchMapper::toDto) // convert entity to DTO
                .collect(Collectors.toList());
    }



}
