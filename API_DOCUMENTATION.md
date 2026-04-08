# E-commerce Backend API Documentation

## Overview
This is a comprehensive e-commerce backend built with Spring Boot that includes user authentication, product management, shopping cart functionality, and order management.

## Authentication
All protected endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## API Endpoints

### User Management

#### Login
- **POST** `/api/users/login`
- **Body**: `{ "email": "user@example.com", "password": "password123" }`
- **Response**: `{ "token": "jwt_token_here" }`

#### Register
- **POST** `/api/users/register`
- **Body**: `{ "name": "John Doe", "email": "user@example.com", "password": "password123" }`
- **Response**: User object

#### Get User Profile
- **GET** `/api/users/{id}`
- **Response**: User profile (without password)

#### Update User Profile
- **PUT** `/api/users/{id}`
- **Body**: `{ "name": "John Doe", "email": "john@example.com", "phone": "+1234567890", "address": "123 Main St", "city": "New York", "state": "NY", "zipCode": "10001", "country": "USA" }`
- **Response**: Updated user profile

### Category Management

#### Get All Categories
- **GET** `/api/categories`
- **Response**: Array of categories

#### Get Category by ID
- **GET** `/api/categories/{id}`
- **Response**: Category details

#### Create Category
- **POST** `/api/categories`
- **Body**: `{ "name": "Electronics", "description": "Electronic items and gadgets" }`
- **Response**: Created category

#### Update Category
- **PUT** `/api/categories/{id}`
- **Body**: `{ "name": "Updated Electronics", "description": "Updated description" }`
- **Response**: Updated category

#### Delete Category
- **DELETE** `/api/categories/{id}`
- **Response**: 204 No Content

### Product Management

#### Get All Products
- **GET** `/api/products`
- **Response**: Array of active products

#### Get Product by ID
- **GET** `/api/products/{id}`
- **Response**: Product details

#### Get Products by Category
- **GET** `/api/products/category/{categoryId}`
- **Response**: Array of products in the category

#### Search Products
- **GET** `/api/products/search?keyword=laptop`
- **Response**: Array of products matching the keyword

#### Create Product
- **POST** `/api/products`
- **Body**: 
```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "sku": "LAP001",
  "imageUrl": "https://example.com/laptop.jpg",
  "isActive": true,
  "categoryId": 1
}
```
- **Response**: Created product

#### Update Product
- **PUT** `/api/products/{id}`
- **Body**: Product data to update
- **Response**: Updated product

#### Delete Product (Soft Delete)
- **DELETE** `/api/products/{id}`
- **Response**: 204 No Content

#### Update Stock
- **PUT** `/api/products/{id}/stock?quantity=10`
- **Response**: 200 OK or 400 Bad Request

### Shopping Cart

#### Get User's Cart
- **GET** `/api/cart/{userId}`
- **Response**: Cart with items and totals

#### Add Item to Cart
- **POST** `/api/cart/{userId}/items?productId=1&quantity=2`
- **Response**: Updated cart

#### Update Cart Item Quantity
- **PUT** `/api/cart/{userId}/items/{productId}?quantity=3`
- **Response**: Updated cart

#### Remove Item from Cart
- **DELETE** `/api/cart/{userId}/items/{productId}`
- **Response**: Updated cart

#### Clear Cart
- **DELETE** `/api/cart/{userId}`
- **Response**: 204 No Content

### Order Management

#### Create Order from Cart
- **POST** `/api/orders/{userId}?shippingAddress=123 Main St&billingAddress=123 Main St`
- **Response**: Created order with order number

#### Get User's Orders
- **GET** `/api/orders/user/{userId}`
- **Response**: Array of user's orders (newest first)

#### Get Order by ID
- **GET** `/api/orders/{orderId}`
- **Response**: Order details with items

#### Update Order Status
- **PUT** `/api/orders/{orderId}/status?status=CONFIRMED`
- **Possible Status Values**: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED
- **Response**: Updated order

#### Cancel Order
- **PUT** `/api/orders/{orderId}/cancel`
- **Response**: Cancelled order (stock is restored)

## Data Models

### User
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "address": "123 Main St",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA"
}
```

### Category
```json
{
  "id": 1,
  "name": "Electronics",
  "description": "Electronic items and gadgets"
}
```

### Product
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "sku": "LAP001",
  "imageUrl": "https://example.com/laptop.jpg",
  "isActive": true,
  "categoryId": 1,
  "categoryName": "Electronics"
}
```

### Cart
```json
{
  "id": 1,
  "userId": 1,
  "cartItems": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "productPrice": 999.99,
      "productImageUrl": "https://example.com/laptop.jpg",
      "quantity": 2,
      "totalPrice": 1999.98
    }
  ],
  "totalAmount": 1999.98,
  "totalItems": 2
}
```

### Order
```json
{
  "id": 1,
  "orderNumber": "ORD-1234567890-ABC123DE",
  "status": "PENDING",
  "totalAmount": 1999.98,
  "taxAmount": 0.00,
  "shippingAmount": 0.00,
  "shippingAddress": "123 Main St",
  "billingAddress": "123 Main St",
  "orderItems": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "price": 999.99,
      "totalPrice": 1999.98
    }
  ],
  "createdAt": "2025-07-24T10:30:00"
}
```

## Error Handling
The API returns appropriate HTTP status codes and error messages:
- 200: Success
- 201: Created
- 204: No Content
- 400: Bad Request
- 401: Unauthorized
- 404: Not Found
- 500: Internal Server Error

Error responses include a message:
```json
{
  "error": "Error description here"
}
```

## Getting Started

1. Make sure you have Java 17+ and Maven installed
2. Configure your database connection in `application.yml`
3. Run the application: `mvn spring-boot:run`
4. The API will be available at `http://localhost:8080`

## Testing the API

You can test the API using tools like Postman, curl, or any HTTP client. Start by:

1. Creating categories
2. Creating products
3. Registering a user
4. Logging in to get a token
5. Adding products to cart
6. Creating orders
