package com.net1707.backend.service;

import com.net1707.backend.model.Product;
import com.net1707.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    //add new product
    public Product addProduct(Product product) {
        if (product.getProductID() != 0) { // If ID exists, it's a detached entity; handle gracefully
            Optional<Product> existingOpt = productRepository.findById(product.getProductID());
            if (existingOpt.isPresent()) {
                throw new RuntimeException("Cannot add. Product with ID " + product.getProductID() + " already exists.");
            }
        }
        return productRepository.save(product);
    }

    //update details product
    @Transactional
    public Product updateProduct(int productId,Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);
        if(existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setSkinTypeCompatibility(updatedProduct.getSkinTypeCompatibility());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            return productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    //delete product
    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

    //get product by id
    public Product getProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException
                        ("Product not found with id: " + productId));
    }

    //get all product
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
}
