package com.net1707.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import com.net1707.backend.service.Interface.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    //add new product
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestPart("product") String productJson,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Product newProduct = productService.addProduct(productJson, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to save product"));
        }
    }

    //update product
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable int id,
                                           @RequestPart("product") String productJson,
                                           @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Product updatedProduct = productService.updateProduct(id, productJson, file);
            return ResponseEntity.ok(updatedProduct);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Product not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid product ID"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to update product"));
        }
    }

    //delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") @Positive(message = "ID must be positive") int productId) {
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
                                                      @Positive(message = "ID must be positive") int productId) {
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
