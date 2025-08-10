package com.example.gulshan.product_catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.example.gulshan.product_catalog.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();
    
    @Query("SELECT c FROM Category c WHERE c.id IN " +
           "(SELECT DISTINCT cat.id FROM Product p JOIN p.categories cat " +
           "WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:brandId IS NULL OR p.brand.id = :brandId))")
    List<Category> findCategoriesInSearchResults(@Param("keyword") String keyword,
                                               @Param("brandId") Long brandId);
}
