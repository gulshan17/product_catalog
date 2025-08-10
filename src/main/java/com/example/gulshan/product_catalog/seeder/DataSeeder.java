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
        // Create realistic product data with proper brand-category associations
        createElectronicsProducts(brands, categories);
        createClothingProducts(brands, categories);
        createSportsProducts(brands, categories);
    }
    
    private void createElectronicsProducts(List<Brand> brands, List<Category> categories) {
        // Find electronics categories
        Category phonesCategory = categories.stream().filter(c -> c.getName().equals("Phones")).findFirst().orElse(null);
        Category laptopsCategory = categories.stream().filter(c -> c.getName().equals("Laptops")).findFirst().orElse(null);
        Category tabletsCategory = categories.stream().filter(c -> c.getName().equals("Tablets")).findFirst().orElse(null);
        
        // Find tech brands
        Brand apple = brands.stream().filter(b -> b.getName().equals("Apple")).findFirst().orElse(null);
        Brand samsung = brands.stream().filter(b -> b.getName().equals("Samsung")).findFirst().orElse(null);
        Brand sony = brands.stream().filter(b -> b.getName().equals("Sony")).findFirst().orElse(null);
        
        // Apple products
        if (apple != null && phonesCategory != null) {
            createProduct("iPhone 15 Pro", "Latest flagship smartphone with titanium design", "1299.99", apple, Set.of(phonesCategory));
            createProduct("iPhone 15", "Advanced dual-camera system smartphone", "999.99", apple, Set.of(phonesCategory));
        }
        if (apple != null && laptopsCategory != null) {
            createProduct("MacBook Pro 16-inch", "Professional laptop with M3 Pro chip", "2499.99", apple, Set.of(laptopsCategory));
            createProduct("MacBook Air 15-inch", "Lightweight laptop with M2 chip", "1299.99", apple, Set.of(laptopsCategory));
        }
        if (apple != null && tabletsCategory != null) {
            createProduct("iPad Pro 12.9-inch", "Professional tablet with M2 chip", "1099.99", apple, Set.of(tabletsCategory));
        }
        
        // Samsung products
        if (samsung != null && phonesCategory != null) {
            createProduct("Galaxy S24 Ultra", "Premium Android smartphone with S Pen", "1199.99", samsung, Set.of(phonesCategory));
            createProduct("Galaxy S24", "Flagship Android smartphone", "899.99", samsung, Set.of(phonesCategory));
        }
        if (samsung != null && tabletsCategory != null) {
            createProduct("Galaxy Tab S9", "Premium Android tablet", "799.99", samsung, Set.of(tabletsCategory));
        }
        
        // Sony products
        if (sony != null && phonesCategory != null) {
            createProduct("Xperia 1 V", "Professional camera smartphone", "1399.99", sony, Set.of(phonesCategory));
        }
    }
    
    private void createClothingProducts(List<Brand> brands, List<Category> categories) {
        // Find clothing categories
        Category mensCategory = categories.stream().filter(c -> c.getName().equals("Men's")).findFirst().orElse(null);
        Category womensCategory = categories.stream().filter(c -> c.getName().equals("Women's")).findFirst().orElse(null);
        
        // Find clothing brands
        Brand nike = brands.stream().filter(b -> b.getName().equals("Nike")).findFirst().orElse(null);
        Brand adidas = brands.stream().filter(b -> b.getName().equals("Adidas")).findFirst().orElse(null);
        
        // Nike products
        if (nike != null && mensCategory != null) {
            createProduct("Nike Air Max 270", "Comfortable lifestyle sneakers for men", "149.99", nike, Set.of(mensCategory));
            createProduct("Nike Dri-FIT T-Shirt", "Moisture-wicking athletic t-shirt", "29.99", nike, Set.of(mensCategory));
        }
        if (nike != null && womensCategory != null) {
            createProduct("Nike Air Force 1", "Classic women's sneakers", "129.99", nike, Set.of(womensCategory));
            createProduct("Nike Sports Bra", "High-support athletic sports bra", "49.99", nike, Set.of(womensCategory));
        }
        
        // Adidas products
        if (adidas != null && mensCategory != null) {
            createProduct("Adidas Ultraboost 22", "Premium running shoes for men", "179.99", adidas, Set.of(mensCategory));
            createProduct("Adidas Essentials Hoodie", "Comfortable cotton hoodie", "59.99", adidas, Set.of(mensCategory));
        }
        if (adidas != null && womensCategory != null) {
            createProduct("Adidas Stan Smith", "Iconic white sneakers for women", "89.99", adidas, Set.of(womensCategory));
        }
    }
    
    private void createSportsProducts(List<Brand> brands, List<Category> categories) {
        // Find sports category
        Category sportsCategory = categories.stream().filter(c -> c.getName().equals("Sports")).findFirst().orElse(null);
        
        // Find sports brands
        Brand nike = brands.stream().filter(b -> b.getName().equals("Nike")).findFirst().orElse(null);
        Brand adidas = brands.stream().filter(b -> b.getName().equals("Adidas")).findFirst().orElse(null);
        
        if (nike != null && sportsCategory != null) {
            createProduct("Nike Football", "Official size football for training", "24.99", nike, Set.of(sportsCategory));
            createProduct("Nike Gym Bag", "Durable sports equipment bag", "39.99", nike, Set.of(sportsCategory));
        }
        
        if (adidas != null && sportsCategory != null) {
            createProduct("Adidas Soccer Ball", "FIFA approved soccer ball", "29.99", adidas, Set.of(sportsCategory));
        }
    }
    
    private void createProduct(String title, String description, String price, Brand brand, Set<Category> categories) {
        Product product = new Product();
        product.setTitle(title);
        product.setSlug(generateSlug(title, (int)(Math.random() * 1000)));
        product.setDescription(description);
        product.setPrice(new BigDecimal(price));
        product.setBrand(brand);
        product.setCategories(categories);
        
        Product savedProduct = productRepository.save(product);
        createProductImages(savedProduct);
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
