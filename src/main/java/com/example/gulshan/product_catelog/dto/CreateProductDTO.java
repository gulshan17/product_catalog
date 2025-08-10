package com.example.gulshan.product_catalog.dto;

public class CreateProductDTO {
    private String title;
    private String description;
    private Double price;
    // Add other fields as needed
    public CreateProductDTO() {}
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
