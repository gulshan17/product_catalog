package com.example.gulshan.product_catalog.entity;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import com.example.gulshan.product_catalog.entity.Product;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String slug;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();
    
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
    
        public Category() {}
        public Category(String name, String slug) {
            this.name = name;
            this.slug = slug;
        }
        public Category(String name, String slug, Category parent) {
            this.name = name;
            this.slug = slug;
            this.parent = parent;
        }
        public Long getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public Category getParent() { return parent; }
        public void setParent(Category parent) { this.parent = parent; }
        public List<Category> getChildren() { return children; }
        public void setChildren(List<Category> children) { this.children = children; }
        public Set<Product> getProducts() { return products; }
        public void setProducts(Set<Product> products) { this.products = products; }
    // Constructors, getters, setters
}
