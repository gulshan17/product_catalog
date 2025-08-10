package com.example.gulshan.product_catalog.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
import com.example.gulshan.product_catalog.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"brand", "categories", "images"})
    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN p.brand " +
           "LEFT JOIN p.categories " +
           "LEFT JOIN p.images " +
           "WHERE p.slug = :slug")
    @Transactional(readOnly = true)
    Optional<Product> findBySlug(@Param("slug") String slug);
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.brand " +
           "LEFT JOIN FETCH p.categories " +
           "LEFT JOIN FETCH p.images")
    List<Product> findAllWithBrandAndCategories();
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN p.brand " +
           "LEFT JOIN p.categories " +
           "LEFT JOIN p.images " +
           "WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
           "AND (:categoryId IS NULL OR :categoryId IN (SELECT c.id FROM p.categories c))")
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                @Param("brandId") Long brandId,
                                @Param("categoryId") Long categoryId,
                                Pageable pageable);
}
