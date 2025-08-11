# Product Catalog API - Testing Guide

## Overview
This is a comprehensive Product Catalog API built with Spring Boot, MySQL, and Redis caching.

## API Endpoints

### 1. Product Search
```bash
# Basic search
curl "http://localhost:8080/api/products/search"

# Search with keyword
curl "http://localhost:8080/api/products/search?q=wooden"

# Search with filters
curl "http://localhost:8080/api/products/search?q=wooden&brandId=5&categoryId=4&page=0&limit=10"
```

### 2. Product Detail Page (PDP)
```bash
# Get product by slug
curl "http://localhost:8080/api/products/sleek-wooden-computer-0"
```

### 3. Categories and Brands
```bash
# Get all categories
curl "http://localhost:8080/api/products/categories"

# Get all brands
curl "http://localhost:8080/api/products/brands"
```

### 4. Breadcrumbs
```bash
# Get breadcrumbs for a product
curl "http://localhost:8080/api/products/breadcrumbs/sleek-wooden-computer-0"
```

### 5. Admin CRUD Operations (Optional)
```bash
# Create product
curl -X POST "http://localhost:8080/api/admin/products" \
  -H "Content-Type: application/json" \
  -d '{"title":"New Product","description":"Test product","price":99.99,"brandId":1,"categoryIds":[1,2]}'

# Update product
curl -X PUT "http://localhost:8080/api/admin/products/1" \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Product","description":"Updated description","price":149.99}'

# Delete product
curl -X DELETE "http://localhost:8080/api/admin/products/1"
```

## Expected Response Structure

### Search Response
```json
{
  "products": [...],
  "totalElements": 25,
  "totalPages": 2,
  "currentPage": 0,
  "filters": {
    "availableCategories": [...],
    "availableBrands": [...]
  }
}
```

### Product Detail Response
```json
{
  "id": 1,
  "title": "Product Name",
  "slug": "product-name-0",
  "description": "Product description",
  "price": 99.99,
  "brand": {"id": 1, "name": "Brand Name"},
  "categories": [{"id": 1, "name": "Category Name"}],
  "images": [{"id": 1, "imageUrl": "...", "altText": "...", "position": 1}],
  "breadcrumbs": [{"id": 1, "name": "Root"}, {"id": 2, "name": "Category"}]
}
```

## Database Schema
- **brands**: id, name, description
- **categories**: id, name, slug, parent_id (self-referencing)
- **products**: id, title, slug, description, price, brand_id
- **product_images**: id, product_id, image_url, alt_text, position
- **product_category**: product_id, category_id (many-to-many)

## Caching
- Product details: 30 minutes TTL
- Filter metadata: 60 minutes TTL
- Cache invalidation on product/category/brand updates

## Test Data
- 5 brands (Apple, Samsung, Nike, Adidas, Sony)
- Hierarchical categories (Electronics > Phones/Laptops/Tablets, Clothing > Men's/Women's, Sports)
- 25 products with realistic data
- 3 images per product
