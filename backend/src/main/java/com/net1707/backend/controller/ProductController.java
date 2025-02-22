package com.net1707.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.service.Interface.IProductService;;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    //add new product
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody AddProductDTO productDTO) {
        Product newProduct = productService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    //update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateProductDTO productDTO) {
        if(id != productDTO.getProductID()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Product updatedProduct = productService.updateProduct(productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    //delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") @Positive(message = "ID must be positive") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Product with ID " + productId + " has been removed.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID " + productId);
        }

    }

    //get product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id")
                                                      @Positive(message = "ID must be positive") Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

    }

    //get all product
    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
    }
}
