package com.net1707.backend.service;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.IProductService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;


    //add new product
    @Override
    @Transactional
    public Product addProduct(AddProductDTO productDTO) {
        Product newProduct = new Product();
        newProduct.setProductName(productDTO.getProductName());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setCategory(productDTO.getCategory());
        newProduct.setSkinTypeCompatibility(productDTO.getSkinTypeCompatibility());

        return productRepository.save(newProduct);
    }

    //update details product
    @Override
    @Transactional
    public Product updateProduct(int id, UpdateProductDTO productDTO) {
        if (id != productDTO.getProductID()) {
            throw new IllegalArgumentException("Product ID mismatch");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

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

        if(productDTO.getImageURL() != null){
            existingProduct.setImageURL(productDTO.getImageURL());
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
