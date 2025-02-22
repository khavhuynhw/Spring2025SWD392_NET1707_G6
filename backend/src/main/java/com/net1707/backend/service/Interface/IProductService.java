package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import java.util.List;

public interface IProductService {
    public Product addProduct(AddProductDTO productDTO);
    public Product updateProduct(int id, UpdateProductDTO productDTO);
    void deleteProduct(int productId);
    Product getProductById(int productId);
    List<Product> getAllProduct();
    void updateProductStock(int productId, int stock);
}
