package com.net1707.backend.repository;

import com.net1707.backend.model.Product;
import com.net1707.backend.model.SkinConcern;
import com.net1707.backend.model.SkinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
            ":skinType MEMBER OF p.suitableSkinTypes AND " +
            "p.suitableForSensitiveSkin = true"
    )
    List<Product> findProductsBySkinType(SkinType skinType);
    @Query("SELECT p FROM Product p WHERE " +
            ":concern MEMBER OF p.targetsConcerns AND " +
            "p.suitableForSensitiveSkin = true")
    List<Product> findProductsBySkinConcern(SkinConcern concern);
}
