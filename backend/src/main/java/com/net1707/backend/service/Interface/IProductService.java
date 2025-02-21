package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product addProduct(String productJson, MultipartFile file) throws IOException;
    Product updateProduct(int id, String productJson, MultipartFile file) throws IOException;
    void deleteProduct(int productId);
    Product getProductById(int productId);
    List<Product> getAllProduct();
    void updateProductStock(int productId, int stock);
}
