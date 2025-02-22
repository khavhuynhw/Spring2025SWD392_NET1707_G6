package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.AddProductDTO;
import com.net1707.backend.dto.UpdateProductDTO;
import com.net1707.backend.model.Product;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductDTO product);
   Product updateProduct(UpdateProductDTO productDTO);
    void deleteProduct(Long productId);
    Product getProductById(Long productId);

    //update details product
    List<Product> getAllProduct();
}
