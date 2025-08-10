package com.example.gulshan.product_catalog.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.example.gulshan.product_catalog.service.ProductService;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;
import com.example.gulshan.product_catalog.dto.ProductSearchResponseDTO;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{slug}")
    public ResponseEntity<ProductDetailDTO> getProduct(@PathVariable String slug) {
        ProductDetailDTO product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(product);
    } // Added missing closing brace

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
}
