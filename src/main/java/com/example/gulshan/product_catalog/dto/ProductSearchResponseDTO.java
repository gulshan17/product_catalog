package com.example.gulshan.product_catalog.dto;

import java.util.List;
import java.util.ArrayList;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;

public class ProductSearchResponseDTO {
    private List<ProductDetailDTO> products;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private FilterMetadata filters;
    
    public ProductSearchResponseDTO() {}
    
    public List<ProductDetailDTO> getProducts() { return products; }
    public void setProducts(List<ProductDetailDTO> products) { this.products = products; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public FilterMetadata getFilters() { return filters; }
    public void setFilters(FilterMetadata filters) { this.filters = filters; }
    
    public static class FilterMetadata {
        private List<ProductDetailDTO.CategoryDTO> availableCategories = new ArrayList<>();
        private List<ProductDetailDTO.BrandDTO> availableBrands = new ArrayList<>();
        
        public FilterMetadata() {}
        
        public List<ProductDetailDTO.CategoryDTO> getAvailableCategories() { return availableCategories; }
        public void setAvailableCategories(List<ProductDetailDTO.CategoryDTO> availableCategories) { 
            this.availableCategories = availableCategories; 
        }
        public List<ProductDetailDTO.BrandDTO> getAvailableBrands() { return availableBrands; }
        public void setAvailableBrands(List<ProductDetailDTO.BrandDTO> availableBrands) { 
            this.availableBrands = availableBrands; 
        }
    }
}
