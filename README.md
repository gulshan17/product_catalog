# Product Catalog Application

A complete e-commerce product catalog system with both REST API and web UI, featuring hierarchical categories, brand filtering, search functionality, and modern responsive design.

## 🚀 Features

### Web UI (Thymeleaf)
- **Product Catalog Page** (`/products`) with advanced filtering and pagination
- **Product Detail Pages** (`/products/{slug}`) with breadcrumbs and complete product information
- **Category & Brand Filters** with real-time search functionality
- **Pagination Controls** with Bootstrap styling and filter preservation
- **Breadcrumb Navigation** showing category hierarchy and brand information
- **Responsive Design** with Bootstrap 5 and modern UI components
- **Admin Interface** for product management (available at `/admin`)

### REST API
- **Product Search API** with filtering by category and brand
- **Product Detail API** with complete product information
- **CRUD Operations** for product management
- **Hierarchical Categories** with unlimited nesting levels
- **RESTful Design** with proper error handling and status codes

### Technical Features
- **Redis Caching** for performance optimization
- **Data Seeding** with realistic product data and proper brand-category associations
- **Image Management** with multiple images per product
- **Slug-based URLs** for SEO-friendly product pages

## 🏗️ Architecture

- **Backend**: Spring Boot 3.5.4 with Java 24
- **Database**: MySQL 8.0 with JPA/Hibernate
- **Caching**: Redis 7 with configurable TTL
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

## 📊 Database Schema

### Core Tables
- **brands**: Brand information (id, name, description)
- **categories**: Hierarchical category structure with parent_id self-referencing
- **products**: Product details (id, title, slug, description, price, brand_id)
- **product_images**: Product images with ordered positioning
- **product_category**: Many-to-many relationship between products and categories

### Key Features
- Categories support unlimited nested levels
- Products can belong to multiple categories
- Each product can have multiple images with ordered positions
- Breadcrumb data is derivable from the category tree

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 24 (for local development)
- Maven (for local development)

### Running with Docker (Recommended)
```bash
# Clone the repository
git clone <repository-url>
cd product_catalog

# Build and start all services
docker-compose up --build

# The API will be available at http://localhost:8080
```

### Local Development
```bash
# Build the project
mvn clean package -DskipTests

# Start MySQL and Redis with Docker
docker-compose up db redis -d

# Run the Spring Boot application
mvn spring-boot:run
```

## 🌐 Web UI Pages

### Customer-Facing Pages
- **Product Catalog** - `http://localhost:8080/products`
  - Advanced search and filtering by category/brand
  - Pagination with Bootstrap controls
  - Responsive product grid with cards
  - Filter preservation across pages

- **Product Detail** - `http://localhost:8080/products/{slug}`
  - Complete product information with images
  - Breadcrumb navigation (Products → Category → Brand → Product)
  - Related products suggestions
  - Add to cart and wishlist functionality

### Admin Interface
- **Admin Dashboard** - `http://localhost:8080/admin`
- **Product Management** - `http://localhost:8080/admin/products`
- **Create Product** - `http://localhost:8080/admin/products/new`

## 📡 REST API Endpoints

### Product Search
```http
GET /api/products/search?q={keyword}&categoryId={id}&brandId={id}&page={page}&limit={limit}
```

### Product Detail
```http
GET /api/products/{slug}
```

### Categories & Brands
```http
GET /api/products/categories
GET /api/products/brands
```

### Breadcrumbs
```http
GET /api/products/breadcrumbs/{slug}
```

### Admin CRUD Operations
```http
POST   /api/admin/products     # Create product
PUT    /api/admin/products/{id} # Update product
DELETE /api/admin/products/{id} # Delete product
GET    /api/admin/products/{id} # Get product by ID
```

## 📝 API Response Examples

### Search Response
```json
{
  "products": [
    {
      "id": 1,
      "title": "Sleek Wooden Computer",
      "slug": "sleek-wooden-computer-0",
      "description": "Product description...",
      "price": 37.46,
      "brand": {"id": 5, "name": "Sony"},
      "categories": [{"id": 4, "name": "Phones"}],
      "images": [{"id": 1, "imageUrl": "...", "altText": "...", "position": 1}]
    }
  ],
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
  "description": "Detailed product description",
  "price": 99.99,
  "brand": {"id": 1, "name": "Brand Name"},
  "categories": [{"id": 1, "name": "Category Name"}],
  "images": [
    {"id": 1, "imageUrl": "https://...", "altText": "Product image", "position": 1}
  ],
  "breadcrumbs": [
    {"id": 1, "name": "Electronics"},
    {"id": 2, "name": "Phones"}
  ]
}
```

## ✅ Implementation Status

### Completed Features
- ✅ **Database Schema**: Complete relational schema with brands, categories, products, images
- ✅ **REST API**: All core endpoints implemented and tested
- ✅ **Web UI**: Full Thymeleaf-based interface with Bootstrap 5 styling
- ✅ **Product Catalog Page**: Advanced filtering, pagination, search functionality
- ✅ **Product Detail Page**: Complete product information with breadcrumbs
- ✅ **Admin Interface**: Product management dashboard and CRUD operations
- ✅ **Data Seeding**: Realistic product data with proper brand-category associations
- ✅ **Pagination**: Bootstrap-styled pagination with filter preservation
- ✅ **Breadcrumbs**: Hierarchical navigation including brand information
- ✅ **Responsive Design**: Mobile-friendly UI with modern styling
- ✅ **Error Handling**: Proper 404/500 error pages and null-safe templates

### Current Status
- 🟢 **Application**: Fully functional and stable
- 🟢 **Web UI**: All pages working with minimal templates
- 🟢 **API Endpoints**: All endpoints returning proper data
- 🟡 **Caching**: Redis annotations present but temporarily disabled
- 🟡 **Layout System**: Using minimal templates, full layout system available for restoration

## 🗄️ Test Data

The application automatically seeds the database with:
- **5 Brands**: Apple, Samsung, Nike, Adidas, Sony
- **Hierarchical Categories**: 
  - Electronics → Phones, Laptops, Tablets
  - Clothing → Men's, Women's
  - Sports
- **25+ Products** with realistic titles, descriptions, and prices
- **Multiple Images** per product with placeholder URLs

## ⚡ Caching Strategy

- **Product Details**: 30 minutes TTL
- **Filter Metadata**: 60 minutes TTL
- **Cache Invalidation**: Automatic on product/category/brand updates
- **Cache Keys**: Optimized for search parameters and product slugs

## 🔧 Configuration

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:mysql://db:3306/catalog_db
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
```

### Redis Configuration
```yaml
spring:
  redis:
    host: redis
    port: 6379
    timeout: 2000ms
```

## 🧪 Testing

### API Testing Examples
```bash
# Search products
curl "http://localhost:8080/api/products/search"

# Search with filters
curl "http://localhost:8080/api/products/search?q=wooden&brandId=5&categoryId=4"

# Get product details
curl "http://localhost:8080/api/products/sleek-wooden-computer-0"

# Get categories
curl "http://localhost:8080/api/products/categories"

# Get brands
curl "http://localhost:8080/api/products/brands"
```

### Testing Guidelines
- Use Postman, Swagger, or browser dev tools to test endpoints
- Validate category tree traversal and breadcrumb output
- Test Redis cache layer for PDP and filter responses
- Confirm cache invalidation on product updates
- Verify unique slug generation for new products

## 🐳 Docker Services

### Services Overview
- **app**: Spring Boot application (port 8080)
- **db**: MySQL 8.0 database (port 3306)
- **redis**: Redis cache server (port 6379)

### Health Checks
All services include health checks to ensure proper startup order and availability.

## 📁 Project Structure

```
src/main/java/com/example/gulshan/product_catalog/
├── config/          # Cache and application configuration
├── controller/      # REST API controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities (Brand, Category, Product, ProductImage)
├── repository/     # JPA repositories with custom queries
├── service/        # Business logic with caching annotations
└── seeder/         # Database seeding with JavaFaker
```

## 🔒 Security & Validation

- Input validation with Bean Validation annotations
- Proper error handling and HTTP status codes
- SQL injection prevention through JPA/Hibernate
- XSS protection through proper data sanitization

## 📈 Performance Features

- **Database Indexing**: Optimized queries with proper indexes
- **Connection Pooling**: HikariCP for efficient database connections
- **Lazy Loading**: JPA lazy loading for related entities
- **Pagination**: Efficient pagination for large result sets
- **Caching**: Redis caching for frequently accessed data

## 🚀 Production Readiness

- Docker containerization for easy deployment
- Environment-specific configuration
- Comprehensive logging with configurable levels
- Health check endpoints
- Graceful shutdown handling
- Connection pooling and timeout configuration

## 📚 Additional Resources

- [API Testing Guide](API_TESTING_GUIDE.md) - Comprehensive testing documentation
- [Docker Compose Configuration](docker-compose.yml) - Service orchestration
- [Application Configuration](application.yml) - Spring Boot settings

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
