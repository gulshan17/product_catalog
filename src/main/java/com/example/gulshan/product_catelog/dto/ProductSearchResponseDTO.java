package com.example.gulshan.product_catalog.dto;

import java.util.List;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;

public class ProductSearchResponseDTO {
    private List<ProductDetailDTO> products;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    // Add getters/setters/constructors
    public ProductSearchResponseDTO() {}
    public List<ProductDetailDTO> getProducts() { return products; }
    public void setProducts(List<ProductDetailDTO> products) { this.products = products; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
}
