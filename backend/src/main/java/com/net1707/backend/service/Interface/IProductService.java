package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductDTO product);
    Product updateProduct(UpdateProductDTO productDTO);
    void deleteProduct(int productId);
    Product getProductById(int productId);
    List<Product> getAllProduct();
}
