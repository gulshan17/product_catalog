package com.example.gulshan.product_catalog.service;

import com.example.gulshan.product_catalog.entity.Product;
import com.example.gulshan.product_catalog.entity.Category;
import com.example.gulshan.product_catalog.repository.ProductRepository;
import com.example.gulshan.product_catalog.repository.CategoryRepository;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;
import com.example.gulshan.product_catalog.dto.ProductSearchResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.example.gulshan.product_catalog.dto.ProductSearchResponseDTO;
import com.example.gulshan.product_catalog.dto.CreateProductDTO;
import com.example.gulshan.product_catalog.dto.UpdateProductDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ProductService {
    @Transactional(readOnly = true)
    public List<ProductDetailDTO.CategoryDTO> getBreadcrumbsForProduct(String slug) {
        Product product = productRepository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + slug));
        // For simplicity, use the first category (if multiple)
        Category category = product.getCategories().stream().findFirst().orElse(null);
        List<ProductDetailDTO.CategoryDTO> breadcrumbs = new ArrayList<>();
        while (category != null) {
            breadcrumbs.add(0, new ProductDetailDTO.CategoryDTO(category.getId(), category.getName()));
            category = category.getParent();
        }
        return breadcrumbs;
    }
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDetailDTO> getAllProducts() {
        List<Product> products = productRepository.findAllWithBrandAndCategories();
        return products.stream()
            .map(this::convertToDetailDTO)
            .collect(Collectors.toList());
    }
    
    // @Cacheable(value = "products", key = "#slug") // Temporarily disabled for debugging
    public ProductDetailDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + slug));
        
        return convertToDetailDTO(product);
    }
    
    public ProductSearchResponseDTO searchProducts(String keyword, Long brandId, 
                                                  Long categoryId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> products = productRepository.searchProducts(keyword, brandId, categoryId, pageable);
        
        ProductSearchResponseDTO response = new ProductSearchResponseDTO();
        response.setProducts(products.getContent().stream()
            .map(this::convertToSearchDTO)
            .collect(Collectors.toList()));
        response.setTotalElements(products.getTotalElements());
        response.setTotalPages(products.getTotalPages());
        response.setCurrentPage(page);
        
        // Build filter metadata from search results
        ProductSearchResponseDTO.FilterMetadata filters = new ProductSearchResponseDTO.FilterMetadata();
        
        // Get unique categories from search results
        List<ProductDetailDTO.CategoryDTO> availableCategories = products.getContent().stream()
            .flatMap(product -> product.getCategories() != null ? product.getCategories().stream() : java.util.stream.Stream.empty())
            .map(cat -> new ProductDetailDTO.CategoryDTO(cat.getId(), cat.getName()))
            .distinct()
            .collect(Collectors.toList());
        filters.setAvailableCategories(availableCategories);
        
        // Get unique brands from search results
        List<ProductDetailDTO.BrandDTO> availableBrands = products.getContent().stream()
            .filter(product -> product.getBrand() != null)
            .map(product -> new ProductDetailDTO.BrandDTO(product.getBrand().getId(), product.getBrand().getName()))
            .distinct()
            .collect(Collectors.toList());
        filters.setAvailableBrands(availableBrands);
        
        response.setFilters(filters);
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
    
    private List<ProductDetailDTO.CategoryDTO> buildBreadcrumbDTOs(Category category) {
        List<ProductDetailDTO.CategoryDTO> breadcrumbs = new ArrayList<>();
        Category current = category;
        
        while (current != null) {
            breadcrumbs.add(0, new ProductDetailDTO.CategoryDTO(current.getId(), current.getName()));
            current = current.getParent();
        }
        
        return breadcrumbs;
    }
    
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
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : null);
        
        // Set brand
        if (product.getBrand() != null) {
            dto.setBrand(new ProductDetailDTO.BrandDTO(product.getBrand().getId(), product.getBrand().getName()));
        }
        
        // Set categories
        if (product.getCategories() != null) {
            List<ProductDetailDTO.CategoryDTO> catDTOs = product.getCategories().stream()
                .map(cat -> new ProductDetailDTO.CategoryDTO(cat.getId(), cat.getName()))
                .toList();
            dto.setCategories(catDTOs);
        }
        
        // Set images
        if (product.getImages() != null) {
            List<ProductDetailDTO.ImageDTO> imageDTOs = product.getImages().stream()
                .map(img -> new ProductDetailDTO.ImageDTO(img.getId(), img.getImageUrl(), img.getAltText(), img.getPosition()))
                .toList();
            dto.setImages(imageDTOs);
        } else {
            dto.setImages(java.util.Collections.emptyList());
        }
        
        // Set breadcrumbs - use first category for breadcrumb path
        if (product.getCategories() != null && !product.getCategories().isEmpty()) {
            Category category = product.getCategories().stream().findFirst().orElse(null);
            dto.setBreadcrumbs(buildBreadcrumbDTOs(category));
        }
        
        return dto;
    }

    private ProductDetailDTO convertToSearchDTO(Product product) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : null);
        
        // Set brand
        if (product.getBrand() != null) {
            dto.setBrand(new ProductDetailDTO.BrandDTO(product.getBrand().getId(), product.getBrand().getName()));
        }
        
        // Set categories
        if (product.getCategories() != null) {
            List<ProductDetailDTO.CategoryDTO> catDTOs = product.getCategories().stream()
                .map(cat -> new ProductDetailDTO.CategoryDTO(cat.getId(), cat.getName()))
                .toList();
            dto.setCategories(catDTOs);
        }
        
        // Set first image for search results
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            var firstImage = product.getImages().get(0);
            List<ProductDetailDTO.ImageDTO> imageDTOs = List.of(
                new ProductDetailDTO.ImageDTO(firstImage.getId(), firstImage.getImageUrl(), 
                                            firstImage.getAltText(), firstImage.getPosition())
            );
            dto.setImages(imageDTOs);
        } else {
            dto.setImages(java.util.Collections.emptyList());
        }
        
        return dto;
    }
}
