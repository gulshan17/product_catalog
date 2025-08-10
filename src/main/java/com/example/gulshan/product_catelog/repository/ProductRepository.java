package com.example.gulshan.product_catalog.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import com.example.gulshan.product_catalog.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.brand " +
           "LEFT JOIN FETCH p.categories " +
           "LEFT JOIN FETCH p.images " +
           "WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
           "AND (:categoryId IS NULL OR :categoryId IN (SELECT c.id FROM p.categories c))")
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                @Param("brandId") Long brandId,
                                @Param("categoryId") Long categoryId,
                                Pageable pageable);
}
