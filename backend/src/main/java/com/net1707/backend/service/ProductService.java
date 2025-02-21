package com.net1707.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.IFileStorageService;
import com.net1707.backend.service.Interface.IProductService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final IFileStorageService iFileStorageService;

    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper, IFileStorageService iFileStorageService) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.iFileStorageService = iFileStorageService;
    }

    //add new product
    @Override
    @Transactional
    public Product addProduct(String productJson, MultipartFile file) throws IOException {
        AddProductDTO productDTO = objectMapper.readValue(productJson, AddProductDTO.class);
        productDTO.setImageURL(iFileStorageService.saveFile(file));

        Product newProduct = new Product(); // change DTO to Entity
        newProduct.setProductName(productDTO.getProductName());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setCategory(productDTO.getCategory());
        newProduct.setSkinTypeCompatibility(productDTO.getSkinTypeCompatibility());
        newProduct.setImageURL(productDTO.getImageURL());
        return productRepository.save(newProduct);
    }

    //update details product
    @Override
    @Transactional
    public Product updateProduct(int id, String productJson, MultipartFile file) throws IOException {
        UpdateProductDTO productDTO = objectMapper.readValue(productJson, UpdateProductDTO.class);
        if (id != productDTO.getProductID()) {
            throw new IllegalArgumentException("Product ID mismatch");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        if (file != null && !file.isEmpty()) {
            iFileStorageService.deleteFile(existingProduct.getImageURL()); // delete old image
            existingProduct.setImageURL(iFileStorageService.saveFile(file));    // save new image
        }

        if (productDTO.getProductName() != null) {
            existingProduct.setProductName(productDTO.getProductName());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getCategory() != null) {
            existingProduct.setCategory(productDTO.getCategory());
        }
        if (productDTO.getSkinTypeCompatibility() != null) {
            existingProduct.setSkinTypeCompatibility(productDTO.getSkinTypeCompatibility());
        }

        return productRepository.save(existingProduct);
    }

    //delete product
    @Override
    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

    //get product by id
    @Override
    public Product getProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException
                        ("Product not found with id: " + productId));
    }

    //get all product
    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public void updateProductStock(int productId, int stock) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID "
                        + productId+ " not found"));
        existingProduct.setStockQuantity(existingProduct.getStockQuantity() + stock);
        productRepository.save(existingProduct);
    }
}
