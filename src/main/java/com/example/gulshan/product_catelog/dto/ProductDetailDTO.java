package com.example.gulshan.product_catalog.dto;

public class ProductDetailDTO {
    // Add fields as needed
    // Example:
    private Long id;
    private String title;
    private String description;
    private Double price;
    // Add getters/setters/constructors
    public ProductDetailDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
