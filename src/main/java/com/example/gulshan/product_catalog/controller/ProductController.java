package com.example.gulshan.product_catalog.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.example.gulshan.product_catalog.service.ProductService;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;
import com.example.gulshan.product_catalog.dto.ProductSearchResponseDTO;
import com.example.gulshan.product_catalog.repository.CategoryRepository;
import com.example.gulshan.product_catalog.repository.BrandRepository;
import com.example.gulshan.product_catalog.entity.Category;
import com.example.gulshan.product_catalog.entity.Brand;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @Autowired
    private ProductService productService;

    @GetMapping("/{slug}")
    public ResponseEntity<ProductDetailDTO> getProduct(@PathVariable String slug) {
        ProductDetailDTO product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search")
    public ResponseEntity<ProductSearchResponseDTO> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        ProductSearchResponseDTO response = productService
            .searchProducts(q, brandId, categoryId, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/breadcrumbs/{slug}")
    public ResponseEntity<List<ProductDetailDTO.CategoryDTO>> getBreadcrumbs(@PathVariable String slug) {
        List<ProductDetailDTO.CategoryDTO> breadcrumbs = productService.getBreadcrumbsForProduct(slug);
        return ResponseEntity.ok(breadcrumbs);
    }
}
