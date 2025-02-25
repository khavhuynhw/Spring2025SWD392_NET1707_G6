package com.net1707.backend.service;


import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.dto.request.ProductRequestDTO;
import com.net1707.backend.mapper.ProductMapper;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductBatchRepository productBatchRepository;
    //add new product
    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toEntity(productRequestDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    //update details product
    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Just update  field not null from requestDTO
        if (requestDTO.getProductName() != null && !requestDTO.getProductName().isEmpty()) {
            product.setProductName(requestDTO.getProductName());
        }
        if (requestDTO.getDescription() != null && !requestDTO.getDescription().isEmpty()) {
            product.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getPrice() != null) {
            product.setPrice(requestDTO.getPrice());
        }
        if (requestDTO.getCategory() != null && !requestDTO.getCategory().isEmpty()) {
            product.setCategory(requestDTO.getCategory());
        }
        if (requestDTO.getSkinTypeCompatibility() != null && !requestDTO.getSkinTypeCompatibility().isEmpty()) {
            product.setSkinTypeCompatibility(requestDTO.getSkinTypeCompatibility());
        }
        if(requestDTO.getImageURL() != null && !requestDTO.getImageURL().isEmpty()){
            product.setImageURL(requestDTO.getImageURL());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }


    //delete product
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // check exist productBatch
        List<ProductBatch> batches = productBatchRepository.findByProduct(product);
        if (!batches.isEmpty()) {
            throw new RuntimeException("Cannot delete product with existing batches");
        }

        productRepository.delete(product);
    }

    //get product by id
    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }



    //get all product
    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
