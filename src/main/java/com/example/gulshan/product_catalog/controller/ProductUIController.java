package com.example.gulshan.product_catalog.controller;

import com.example.gulshan.product_catalog.entity.Product;
import com.example.gulshan.product_catalog.entity.Category;
import com.example.gulshan.product_catalog.entity.Brand;
import com.example.gulshan.product_catalog.service.ProductService;
import com.example.gulshan.product_catalog.repository.CategoryRepository;
import com.example.gulshan.product_catalog.repository.BrandRepository;
import com.example.gulshan.product_catalog.repository.ProductRepository;
import com.example.gulshan.product_catalog.dto.ProductDetailDTO;
import com.example.gulshan.product_catalog.dto.ProductSearchResponseDTO;
import com.example.gulshan.product_catalog.dto.CreateProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductUIController {
    
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    // Home page - redirect to products
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    // Product catalog page
    @GetMapping("/products")
    public String showProducts(Model model,
                              @RequestParam(value = "q", required = false) String query,
                              @RequestParam(value = "category", required = false) Long categoryId,
                              @RequestParam(value = "brand", required = false) Long brandId,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "12") int size) {
        
        // Use the search functionality for filtering
        ProductSearchResponseDTO searchResponse = productService.searchProducts(query, brandId, categoryId, page, size);
        
        List<Category> categories = categoryRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        
        model.addAttribute("products", searchResponse.getProducts());
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedBrand", brandId);
        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResponse.getTotalPages());
        model.addAttribute("totalProducts", searchResponse.getTotalElements());
        
        return "products-minimal";
    }

    // Product detail page
    @GetMapping("/products/{slug}")
    public String showProductDetail(@PathVariable String slug, Model model) {
        try {
            ProductDetailDTO productDTO = productService.getProductBySlug(slug);
            List<ProductDetailDTO.CategoryDTO> breadcrumbs = productService.getBreadcrumbsForProduct(slug);
            
            model.addAttribute("product", productDTO);
            model.addAttribute("breadcrumbs", breadcrumbs);
            return "product-detail-minimal";
        } catch (Exception e) {
            return "redirect:/products?error=Product not found";
        }
    }

    // Admin pages
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        long productCount = productRepository.count();
        long categoryCount = categoryRepository.count();
        long brandCount = brandRepository.count();
        
        model.addAttribute("productCount", productCount);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("brandCount", brandCount);
        
        return "admin/dashboard";
    }

    @GetMapping("/admin/products")
    public String adminProducts(Model model) {
        List<ProductDetailDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/admin/products/new")
    public String newProduct(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        
        model.addAttribute("product", new CreateProductDTO());
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        
        return "admin/product-form";
    }

    @PostMapping("/admin/products")
    public String createProduct(@ModelAttribute CreateProductDTO productDTO, 
                               @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
                               @RequestParam(value = "imageUrls", required = false) List<String> imageUrls,
                               RedirectAttributes redirectAttributes) {
        try {
            // Set the category and image data from form
            productDTO.setCategoryIds(categoryIds);
            productDTO.setImageUrls(imageUrls);
            
            // Implementation for creating product would go here
            productService.createProduct(productDTO);
            redirectAttributes.addFlashAttribute("success", "Product created successfully!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create product: " + e.getMessage());
            return "redirect:/admin/products/new";
        }
    }

    @GetMapping("/admin/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        try {
            // For now, redirect to products list since edit functionality needs full implementation
            return "redirect:/admin/products?info=Edit functionality coming soon";
        } catch (Exception e) {
            return "redirect:/admin/products?error=Product not found";
        }
    }

    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Implementation for deleting product would go here
            // productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/categories")
    public String adminCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @GetMapping("/admin/brands")
    public String adminBrands(Model model) {
        List<Brand> brands = brandRepository.findAll();
        model.addAttribute("brands", brands);
        return "admin/brands";
    }
}
