package com.net1707.backend.repository;

import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {
    List<ProductBatch> findByProduct(Product product);
}
