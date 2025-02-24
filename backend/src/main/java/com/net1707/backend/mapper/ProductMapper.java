package com.net1707.backend.mapper;

import com.net1707.backend.dto.ProductDTO;
import com.net1707.backend.dto.request.ProductRequestDTO;
import com.net1707.backend.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends BaseMapper<ProductDTO, Product> {
    @Override
    public ProductDTO toDto(Product entity) {
        return ProductDTO.builder()
                .productID(entity.getProductID())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .skinTypeCompatibility(entity.getSkinTypeCompatibility())
                .stockQuantity(entity.getStockQuantity())
                .imageURL(entity.getImageURL())
                .build();
    }


    public Product toEntity(ProductRequestDTO dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .skinTypeCompatibility(dto.getSkinTypeCompatibility())
                .imageURL(dto.getImageURL())
                .build();
    }


    @Override
    public Product toEntity(ProductDTO dto) {
        return Product.builder()
                .productID(dto.getProductID())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .skinTypeCompatibility(dto.getSkinTypeCompatibility())
                .stockQuantity(dto.getStockQuantity())
                .imageURL(dto.getImageURL())
                .build();
    }

}
