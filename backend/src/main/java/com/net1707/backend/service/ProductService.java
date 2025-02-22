package com.net1707.backend.service;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //add new product
    @Override
    @Transactional
    public Product addProduct(AddProductDTO product) {
        Product newProduct = new Product();
        newProduct.setProductName(product.getProductName());
        newProduct.setDescription(product.getDescription());
        newProduct.setPrice(product.getPrice());
        newProduct.setCategory(product.getCategory());
        newProduct.setSkinTypeCompatibility(product.getSkinTypeCompatibility());
        return productRepository.save(newProduct);
    }

    //update details product
    @Override
    @Transactional
    public Product updateProduct(UpdateProductDTO updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(updatedProduct.getProductID());
        if(existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setSkinTypeCompatibility(updatedProduct.getSkinTypeCompatibility());
            
            return productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with id: " + updatedProduct.getProductID());
        }
    }

    //delete product
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    //get product by id
    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException
                        ("Product not found with id: " + productId));
    }

    //get all product
    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
}
