package com.example.gulshan.product_catalog.entity;
import jakarta.persistence.*;
import com.example.gulshan.product_catalog.entity.Product;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String imageUrl;
    
    private String altText;
    
    @Column(nullable = false)
    private Integer position;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    // Constructors, getters, setters
    public ProductImage() {}
    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
