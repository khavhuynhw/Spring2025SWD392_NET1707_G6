package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.dto.request.ProductRequestDTO;


import java.util.List;

public interface IProductService {
    ProductDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long id);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
}
