package com.example.gulshan.product_catalog.seeder;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

import com.example.gulshan.product_catalog.entity.Brand;
import com.example.gulshan.product_catalog.entity.Category;
import com.example.gulshan.product_catalog.entity.Product;
import com.example.gulshan.product_catalog.entity.ProductImage;
import com.example.gulshan.product_catalog.repository.BrandRepository;
import com.example.gulshan.product_catalog.repository.CategoryRepository;
import com.example.gulshan.product_catalog.repository.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private final Faker faker = new Faker();
    
    @Override
    public void run(String... args) throws Exception {
        if (brandRepository.count() == 0) {
            seedData();
        }
    }
    
    private void seedData() {
        // Create brands
        List<Brand> brands = createBrands();
        
        // Create category hierarchy
        List<Category> categories = createCategories();
        
        // Create products
        createProducts(brands, categories);
    }
    
    private List<Brand> createBrands() {
        List<Brand> brands = Arrays.asList(
            new Brand("Apple", "Premium technology products"),
            new Brand("Samsung", "Innovative electronics"),
            new Brand("Nike", "Athletic footwear and apparel"),
            new Brand("Adidas", "Sports equipment and clothing"),
            new Brand("Sony", "Consumer electronics")
        );
        
        return brandRepository.saveAll(brands);
    }
    
    private List<Category> createCategories() {
        // Root categories
        Category electronics = new Category("Electronics", "electronics");
        Category clothing = new Category("Clothing", "clothing");
        Category sports = new Category("Sports", "sports");
        
        categoryRepository.saveAll(Arrays.asList(electronics, clothing, sports));
        
        // Sub categories for Electronics
        Category phones = new Category("Phones", "phones", electronics);
        Category laptops = new Category("Laptops", "laptops", electronics);
        Category tablets = new Category("Tablets", "tablets", electronics);
        
        // Sub categories for Clothing
        Category mens = new Category("Men's", "mens", clothing);
        Category womens = new Category("Women's", "womens", clothing);
        
        return categoryRepository.saveAll(Arrays.asList(phones, laptops, tablets, mens, womens));
    }
    
    private void createProducts(List<Brand> brands, List<Category> categories) {

        Random random = new Random();
        
        for (int i = 0; i < 25; i++) {
            Product product = new Product();
            product.setTitle(faker.commerce().productName());
            product.setSlug(generateSlug(product.getTitle(), i));
            product.setDescription(faker.lorem().paragraph());
            product.setPrice(new BigDecimal(faker.commerce().price().replace(",", "")));
            
            // Random brand
            product.setBrand(brands.get(random.nextInt(brands.size())));
            
            // Random categories (1-3)
            Set<Category> productCategories = new HashSet<>();
            int numCategories = random.nextInt(3) + 1;
            for (int j = 0; j < numCategories; j++) {
                productCategories.add(categories.get(random.nextInt(categories.size())));
            }
            product.setCategories(productCategories);
            
            Product savedProduct = productRepository.save(product);
            
            // Add images
            createProductImages(savedProduct);
        }
    }
    private String generateSlug(String title, int index) {
        String baseSlug = title.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");
        return baseSlug + "-" + index;
    }
    
    private void createProductImages(Product product) {
        for (int i = 0; i < 3; i++) {
            ProductImage image = new ProductImage();
            image.setImageUrl("https://via.placeholder.com/400x400?text=Product" + product.getId() + "Image" + (i+1));
            image.setAltText(product.getTitle() + " image " + (i+1));
            image.setPosition(i + 1);
            image.setProduct(product);
            
            product.getImages().add(image);
        }
        productRepository.save(product);
    }
}
