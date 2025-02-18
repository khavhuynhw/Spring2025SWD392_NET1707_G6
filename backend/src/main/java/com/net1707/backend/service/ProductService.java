package com.net1707.backend.service;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.repository.ProductRepository;
import com.net1707.backend.service.Interface.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public Product updateProduct(UpdateProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productDTO.getProductID())
                .orElseThrow(() -> new RuntimeException("Product with ID "
                        + productDTO.getProductID() + " not found"));

        // update information
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setSkinTypeCompatibility(productDTO.getSkinTypeCompatibility());

        // save updateproduct
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
}
