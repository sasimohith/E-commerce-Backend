# E-commerce Backend API

A comprehensive RESTful e-commerce backend built with Spring Boot, featuring complete user authentication, product management, shopping cart functionality, and order processing.

## 🏗️ Architecture & Design

### **System Architecture**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend API   │    │   Database      │
│   (React)       │◄──►│  (Spring Boot)  │◄──►│    (MySQL)      │
│  Port: 3000     │    │   Port: 8080    │    │   Port: 3306    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### **Layered Architecture**
```
┌─────────────────────────────────────────┐
│              Controllers                │ ← REST API Endpoints
├─────────────────────────────────────────┤
│               Services                  │ ← Business Logic
├─────────────────────────────────────────┤
│              Repositories               │ ← Data Access Layer
├─────────────────────────────────────────┤
│               Entities                  │ ← JPA Domain Models
└─────────────────────────────────────────┘
```

## 🚀 Tech Stack

### **Core Technologies**
- **Java**: 17 (LTS)
- **Spring Boot**: 3.2.5
- **Maven**: 3.x (Build & Dependency Management)

### **Frameworks & Libraries**
- **Spring Security**: JWT-based authentication & authorization
- **Spring Data JPA**: Database ORM and repository abstraction  
- **Spring Web**: RESTful API development
- **Spring Boot Actuator**: Health monitoring & metrics
- **Hibernate**: JPA implementation with MySQL dialect

### **Database & Storage**
- **MySQL**: 8.x (Primary database)
- **HikariCP**: Connection pooling (default in Spring Boot)

### **Authentication & Security**
- **JWT (JSON Web Tokens)**: Stateless authentication
- **BCrypt**: Password hashing
- **CORS**: Cross-origin resource sharing configuration

### **Development Tools**
- **Lombok**: Reduces boilerplate code
- **Jackson**: JSON serialization/deserialization
- **Maven Surefire**: Testing framework integration
- **Mockito**: Unit testing & mocking

## 🏢 Business Modules

### **1. User Management Module**
- **Authentication**: Login/Logout with JWT tokens
- **Registration**: New user account creation
- **Profile Management**: Update personal information
- **Password Security**: BCrypt encryption

### **2. Product Catalog Module**
- **Product CRUD**: Create, read, update, delete products
- **Category Management**: Product categorization
- **Search & Filtering**: Keyword-based product search
- **Stock Management**: Real-time inventory tracking
- **Product Activation**: Enable/disable products

### **3. Shopping Cart Module**
- **Cart Operations**: Add, update, remove items
- **Session Management**: User-specific cart persistence
- **Price Calculations**: Dynamic total computation
- **Cart-to-Order**: Seamless checkout conversion

### **4. Order Management Module**
- **Order Processing**: Convert cart to orders
- **Order Tracking**: 7-state order lifecycle
- **Stock Integration**: Automatic inventory updates
- **Order Cancellation**: Restore stock on cancellation
- **Order History**: User purchase history

### **5. Category Management Module**
- **Category CRUD**: Manage product categories
- **Product Association**: Link products to categories
- **Public Access**: No authentication required

## 📊 Database Schema

### **Entity Relationships**
```
┌─────────┐       ┌─────────────┐       ┌─────────┐
│  User   │◄─────►│    Cart     │◄─────►│ Product │
└────┬────┘       └─────────────┘       └────┬────┘
     │                                       │
     │            ┌─────────────┐            │
     └───────────►│    Order    │◄───────────┘
                  └─────────────┘
                         │
                  ┌─────────────┐
                  │  Category   │
                  └─────────────┘
```

### **Key Tables**
- **users**: User account information and profiles
- **products**: Product catalog with pricing and inventory
- **categories**: Product categorization system
- **carts**: Shopping cart management
- **cart_items**: Items within shopping carts
- **orders**: Order records and status tracking
- **order_items**: Individual products within orders

## 🔐 Security Features

### **Authentication Flow**
1. User registers/logs in via `/api/users/register` or `/api/users/login`
2. Backend validates credentials and returns JWT token
3. Client includes token in `Authorization: Bearer <token>` header
4. JWT filter validates token on protected endpoints
5. User context extracted and available throughout request

### **Authorization Levels**
- **Public Endpoints**: Products, categories (no authentication)
- **Protected Endpoints**: Cart operations, orders, user profile
- **CORS Enabled**: Frontend integration on localhost:3000

### **Security Configurations**
- **Password Encryption**: BCrypt with salt
- **JWT Expiration**: 24 hours (86400000ms)
- **Stateless Sessions**: No server-side session storage
- **CORS**: Configured for localhost:3000 frontend

## 🛠️ API Functionality

### **Authentication APIs**
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User authentication
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile

### **Product Management APIs**  
- `GET /api/products` - Get all active products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search?keyword=` - Search products
- `GET /api/products/category/{categoryId}` - Get products by category
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Soft delete product

### **Category Management APIs**
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### **Shopping Cart APIs**
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/{userId}/items?productId=&quantity=` - Add to cart
- `PUT /api/cart/{userId}/items/{productId}?quantity=` - Update cart item
- `DELETE /api/cart/{userId}/items/{productId}` - Remove from cart
- `DELETE /api/cart/{userId}` - Clear entire cart

### **Order Management APIs**
- `POST /api/orders/{userId}?shippingAddress=&billingAddress=` - Create order
- `GET /api/orders/user/{userId}` - Get user's orders
- `GET /api/orders/{orderId}` - Get order details
- `PUT /api/orders/{orderId}/status?status=` - Update order status
- `PUT /api/orders/{orderId}/cancel` - Cancel order

## 🗂️ Project Structure

```
src/main/java/com/example/ecommerce/
├── EcommerceApplication.java           # Spring Boot main class
├── controller/                        # REST API Controllers
│   ├── UserController.java           # Authentication & user management
│   ├── ProductController.java        # Product catalog operations
│   ├── CategoryController.java       # Category management
│   ├── CartController.java           # Shopping cart operations
│   └── OrderController.java          # Order processing
├── service/                          # Business Logic Services
│   ├── UserService.java             # User business logic
│   ├── ProductService.java          # Product business logic
│   ├── CategoryService.java         # Category business logic
│   ├── CartService.java             # Cart business logic
│   └── OrderService.java            # Order business logic
├── repository/                       # Data Access Layer
│   ├── UserRepository.java          # User data operations
│   ├── ProductRepository.java       # Product data operations
│   ├── CategoryRepository.java      # Category data operations
│   ├── CartRepository.java          # Cart data operations
│   ├── CartItemRepository.java      # Cart item operations
│   ├── OrderRepository.java         # Order data operations
│   └── OrderItemRepository.java     # Order item operations
├── entity/                          # JPA Domain Models
│   ├── User.java                   # User entity
│   ├── Product.java                # Product entity
│   ├── Category.java               # Category entity
│   ├── Cart.java                   # Shopping cart entity
│   ├── CartItem.java               # Cart item entity
│   ├── Order.java                  # Order entity
│   └── OrderItem.java              # Order item entity
├── dto/                            # Data Transfer Objects
│   ├── UserDTO.java                # User data transfer
│   ├── ProductDTO.java             # Product data transfer
│   ├── CategoryDTO.java            # Category data transfer
│   ├── CartDTO.java                # Cart data transfer
│   ├── CartItemDTO.java            # Cart item transfer
│   ├── OrderDTO.java               # Order data transfer
│   └── OrderItemDTO.java           # Order item transfer
└── security/                       # Security Configuration
    ├── SecurityConfig.java         # Spring Security setup
    ├── JwtUtil.java               # JWT token utilities
    ├── JwtFilter.java             # JWT authentication filter
    └── CustomUserDetailsService.java # User details service
```

## 🔄 Business Workflows

### **Complete E-commerce Flow**
```
1. User Registration → Account Creation
2. User Login → JWT Token Generation
3. Browse Products → View Catalog & Categories  
4. Add to Cart → Cart Management
5. Checkout → Order Creation & Stock Update
6. Order Processing → Status Tracking
7. Order History → View Past Orders
```

### **Order Status Lifecycle**
```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
    ↓         ↓
CANCELLED ←──┘ (Stock Restored)
    ↓
REFUNDED
```

## ⚙️ Configuration

### **Database Configuration** (`application.yml`)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommercedb
    username: root
    password: [configured]
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

### **Server Configuration**
```yaml
server:
  port: 8080
spring:
  application:
    name: ecommerce-backend
```

## 🚀 Getting Started

### **Prerequisites**
- Java 17 or higher
- Maven 3.6+
- MySQL 8.x
- Git

### **Installation & Setup**
```bash
# Clone the repository
git clone <repository-url>
cd ecommerce-backend

# Configure database
mysql -u root -p
CREATE DATABASE ecommercedb;

# Update application.yml with your MySQL credentials

# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Application will start on http://localhost:8080
```

### **Database Initialization**
The application includes:
- **Schema Auto-creation**: JPA creates tables automatically
- **Sample Data**: Pre-loaded categories, products, and test users
- **Database Indexes**: Optimized for performance

### **Testing the API**
```bash
# Health check
curl http://localhost:8080/actuator/health

# Get all products (public)
curl http://localhost:8080/api/products

# Register a new user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'

# Login user
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

## 📋 Testing Resources

### **API Testing Collections**
- **Postman Collection**: `postman_collection.json`
- **Insomnia Collection**: `insomnia_collection.json`
- **Testing Guide**: `INSOMNIA_TESTING_GUIDE.md`
- **API Documentation**: `API_DOCUMENTATION.md`

### **Sample Data**
- **Categories**: Electronics, Books, Clothing, Home & Garden, Sports
- **Products**: 8 sample products with proper categorization
- **Test Users**: Pre-configured with encrypted passwords

## 🎯 Key Features & Benefits

### **Production-Ready Features**
- ✅ **Comprehensive Authentication**: JWT-based with proper security
- ✅ **CORS Configuration**: Ready for frontend integration
- ✅ **Transaction Management**: Ensures data consistency
- ✅ **Error Handling**: Global exception handling
- ✅ **Stock Management**: Real-time inventory tracking
- ✅ **Order Processing**: Complete order lifecycle management
- ✅ **Database Optimization**: Proper indexing and relationships

### **Developer-Friendly**
- ✅ **Clean Architecture**: Layered design with clear separation
- ✅ **DTO Pattern**: Safe data transfer between layers
- ✅ **Comprehensive Documentation**: API docs and testing guides
- ✅ **Sample Data**: Pre-loaded for immediate testing
- ✅ **Modular Design**: Easy to extend and maintain

### **Scalability & Performance**
- ✅ **Stateless Design**: Horizontal scaling friendly
- ✅ **Database Indexing**: Optimized query performance
- ✅ **Connection Pooling**: Efficient database connections
- ✅ **Lazy Loading**: JPA performance optimization

## 🔮 Future Enhancements

### **Potential Extensions**
- **Admin Dashboard**: Product & order management interface
- **Payment Integration**: Stripe/PayPal gateway integration
- **Email Notifications**: Order confirmations and updates
- **Product Reviews**: Rating and review system
- **Wishlist Feature**: Save products for later
- **Advanced Search**: Filters, sorting, price ranges
- **Inventory Alerts**: Low stock notifications
- **Analytics**: Sales and user behavior tracking

## 📞 API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| **Authentication** |
| POST | `/api/users/register` | Register new user | No |
| POST | `/api/users/login` | User login | No |
| GET | `/api/users/{id}` | Get user profile | Yes |
| PUT | `/api/users/{id}` | Update profile | Yes |
| **Products** |
| GET | `/api/products` | Get all products | No |
| GET | `/api/products/{id}` | Get product details | No |
| GET | `/api/products/search` | Search products | No |
| **Categories** |
| GET | `/api/categories` | Get all categories | No |
| GET | `/api/categories/{id}` | Get category | No |
| **Shopping Cart** |
| GET | `/api/cart/{userId}` | Get user cart | Yes |
| POST | `/api/cart/{userId}/items` | Add to cart | Yes |
| PUT | `/api/cart/{userId}/items/{productId}` | Update cart item | Yes |
| DELETE | `/api/cart/{userId}/items/{productId}` | Remove from cart | Yes |
| **Orders** |
| POST | `/api/orders/{userId}` | Create order | Yes |
| GET | `/api/orders/user/{userId}` | Get user orders | Yes |
| GET | `/api/orders/{orderId}` | Get order details | Yes |
| PUT | `/api/orders/{orderId}/status` | Update order status | Yes |
| PUT | `/api/orders/{orderId}/cancel` | Cancel order | Yes |

---

## 🤝 Contributing

This project follows standard Spring Boot conventions and clean code principles. When contributing:

1. Follow the existing layered architecture pattern
2. Implement proper error handling and validation
3. Write comprehensive tests for new features
4. Update documentation for API changes
5. Ensure database migrations are handled properly

## 📄 License

This project is available for educational and commercial use. Please ensure you update security configurations and database credentials before production deployment.

---

**Built with ❤️ using Spring Boot 3.2.5 & Java 17**