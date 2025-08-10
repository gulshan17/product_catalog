package com.example.gulshan.product_catalog.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.example.gulshan.product_catalog.repository.ProductRepository;
import com.example.gulshan.product_catalog.repository.CategoryRepository;
import com.example.gulshan.product_catalog.entity.Product;
import com.example.gulshan.product_catalog.entity.Category;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;
import com.example.gulshan.product_catalog.dto.ProductSearchResponseDTO;
import com.example.gulshan.product_catalog.dto.CreateProductDTO;
import com.example.gulshan.product_catalog.dto.UpdateProductDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Cacheable(value = "products", key = "#slug")
    public ProductDetailDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + slug));
        
        return convertToDetailDTO(product);
    }
    
    public ProductSearchResponseDTO searchProducts(String keyword, Long brandId, 
                                                  Long categoryId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> products = productRepository.searchProducts(keyword, brandId, categoryId, pageable);
        
    // List<Category> availableCategories = categoryRepository.findCategoriesInSearchResults(keyword, brandId);
        ProductSearchResponseDTO response = new ProductSearchResponseDTO();
        response.setProducts(products.getContent().stream()
            .map(this::convertToSearchDTO)
            .collect(Collectors.toList()));
        response.setTotalElements(products.getTotalElements());
        response.setTotalPages(products.getTotalPages());
        response.setCurrentPage(page);
        // If you want to add availableCategories, add a setter in ProductSearchResponseDTO
        return response;
    }
    
    private List<String> buildBreadcrumb(Category category) {
        List<String> breadcrumb = new ArrayList<>();
        Category current = category;
        
        while (current != null) {
            breadcrumb.add(0, current.getName());
            current = current.getParent();
        }
        
        return breadcrumb;
    }
    
    @CacheEvict(value = {"products", "filters"}, allEntries = true)
    @Transactional
    public Product createProduct(CreateProductDTO dto) {
        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setSlug(generateUniqueSlug(dto.getTitle()));
        product.setDescription(dto.getDescription());
    product.setPrice(java.math.BigDecimal.valueOf(dto.getPrice()));
        // Set brand and categories...
        return productRepository.save(product);
    }

    
    private String generateUniqueSlug(String title) {
        String baseSlug = title.toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "-");
        
        String slug = baseSlug;
        int counter = 1;
        
        while (productRepository.findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + counter++;
        }
        
        return slug;
    }
    
    @Transactional
    public Product updateProduct(Long id, UpdateProductDTO dto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
    product.setPrice(java.math.BigDecimal.valueOf(dto.getPrice()));
        // Set brand and categories if needed
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDetailDTO convertToDetailDTO(Product product) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice().doubleValue());
        // Add more fields as needed
        return dto;
    }

    private ProductDetailDTO convertToSearchDTO(Product product) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice().doubleValue());
        // Add more fields as needed
        return dto;
    }
    }
