package com.example.gulshan.product_catalog.dto;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProductDetailDTO {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private Double price;
    private String testField = "test-value";
    private BrandDTO brand;
    private List<CategoryDTO> categories = new ArrayList<>();
    private List<ImageDTO> images = new ArrayList<>();
    private List<CategoryDTO> breadcrumbs = new ArrayList<>();

    public ProductDetailDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public BrandDTO getBrand() { return brand; }
    public void setBrand(BrandDTO brand) { this.brand = brand; }
    public List<CategoryDTO> getCategories() { return categories; }
    public void setCategories(List<CategoryDTO> categories) { this.categories = categories; }
    public List<ImageDTO> getImages() { return images; }
    public void setImages(List<ImageDTO> images) { this.images = images; }
    public List<CategoryDTO> getBreadcrumbs() { return breadcrumbs; }
    public void setBreadcrumbs(List<CategoryDTO> breadcrumbs) { this.breadcrumbs = breadcrumbs; }
    public String getTestField() { return testField; }
    public void setTestField(String testField) { this.testField = testField; }

    public static class BrandDTO {
        private Long id;
        private String name;
        public BrandDTO() {}
        public BrandDTO(Long id, String name) { this.id = id; this.name = name; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class CategoryDTO {
        private Long id;
        private String name;
        public CategoryDTO() {}
        public CategoryDTO(Long id, String name) { this.id = id; this.name = name; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class ImageDTO {
        private Long id;
        private String imageUrl;
        private String altText;
        private Integer position;
        
        public ImageDTO() {}
        public ImageDTO(Long id, String imageUrl, String altText, Integer position) {
            this.id = id;
            this.imageUrl = imageUrl;
            this.altText = altText;
            this.position = position;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getAltText() { return altText; }
        public void setAltText(String altText) { this.altText = altText; }
        public Integer getPosition() { return position; }
        public void setPosition(Integer position) { this.position = position; }
    }
}
