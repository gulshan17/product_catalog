package com.example.gulshan.product_catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gulshan.product_catalog.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    // Custom queries if needed
}
