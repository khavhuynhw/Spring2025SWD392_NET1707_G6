    package com.net1707.backend.model;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.List;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "products")
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int productID;

        private String productName;
        @Column(columnDefinition = "TEXT")
        private String description;
        private float price;
        private String category;
        private String skinTypeCompatibility;
        private int stockQuantity;
        private String imageURL;

        @JsonIgnore
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Review> reviews;

        @JsonIgnore
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderDetail> orderDetails;

        @JsonIgnore
        @OneToMany(mappedBy = "product")
        private List<ProductBatch> productBatches;


    }
