# E-commerce API Testing Script for Insomnia

## � **Quick Reference - Testing Checklist**
```
□ Phase 1: Create Categories (2-3 categories)
□ Phase 2: Create Products (2-3 products)  
□ Phase 3: Register & Login User (get token!)
□ Phase 4: Shopping Flow (cart operations)
□ Phase 5: Order Management (checkout)
□ Phase 6: Advanced Testing (cancellation, etc.)
```

## 🔑 **Environment Variables Setup**
In Insomnia, create these environment variables:
```json
{
  "baseUrl": "http://localhost:8080",
  "token": "",
  "userId": ""
}
```

## �🚀 **IMPORTANT: Testing Order Matters!**
Follow these steps **exactly in this order** for successful testing.

## 🏷️ **PHASE 1: Setup Categories (Do This First!)**

### **Step 1: Create Electronics Category**
```
📁 Categories → Create Category
POST /api/categories
Content-Type: application/json
```
**Body:**
```json
{
  "name": "Electronics", 
  "description": "Electronic devices and gadgets"
}
```
**Expected Response:** Category created with ID: 1

### **Step 2: Create Books Category**
```
📁 Categories → Create Category
POST /api/categories
Content-Type: application/json
```
**Body:**
```json
{
  "name": "Books", 
  "description": "Books and e-books"
}
```
**Expected Response:** Category created with ID: 2

### **Step 3: Verify Categories**
```
📁 Categories → Get All Categories
GET /api/categories
```
**Expected:** Should see 2 categories in response

## 📦 **PHASE 2: Setup Products (Categories Must Exist First!)**

### **Step 4: Create Gaming Laptop**
```
📦 Products → Create Product
POST /api/products
Content-Type: application/json
```
**Body:**
```json
{
  "name": "Gaming Laptop",
  "description": "High-performance gaming laptop",
  "price": 1299.99,
  "stockQuantity": 10,
  "sku": "LAP-GAMING-001",
  "imageUrl": "https://example.com/laptop.jpg",
  "isActive": true,
  "categoryId": 1
}
```
**Expected Response:** Product created with ID: 1

### **Step 5: Create Smartphone**
```
📦 Products → Create Product
POST /api/products
Content-Type: application/json
```
**Body:**
```json
{
  "name": "Smartphone Pro",
  "description": "Latest smartphone with advanced features",
  "price": 899.99,
  "stockQuantity": 25,
  "sku": "PHONE-PRO-001",
  "imageUrl": "https://example.com/phone.jpg",
  "isActive": true,
  "categoryId": 1
}
```
**Expected Response:** Product created with ID: 2

### **Step 6: Verify Products**
```
📦 Products → Get All Products
GET /api/products
```
**Expected:** Should see 2 products with category information

## 🔐 **PHASE 3: User Registration & Authentication**

### **Step 7: Register New User**
```
🔐 Authentication → Register User
POST /api/users/register
Content-Type: application/json
```
**Body:**
```json
{
  "name": "Test User",
  "email": "testuser@example.com",
  "password": "password123",
  "phone": "+1234567890",
  "address": "123 Test Street",
  "city": "Test City",
  "state": "TS",
  "zipCode": "12345",
  "country": "USA"
}
```
**⚠️ IMPORTANT:** Note the user ID from response (usually 1, 2, 3...)

### **Step 8: Login User**
```
🔐 Authentication → Login User
POST /api/users/login
Content-Type: application/json
```
**Body:**
```json
{
  "email": "testuser@example.com",
  "password": "password123"
}
```
**🚨 CRITICAL:** Copy the token from response and update environment variable!

### **Step 9: Test Authentication**
```
👤 User Management → Get User Profile
GET /api/users/{userId}
Authorization: Bearer {your-token}
```
**Replace {userId} with actual user ID from Step 7**

## 🛒 **PHASE 4: Shopping Flow**

### **Step 10: Search Products**
```
📦 Products → Search Products
GET /api/products/search?keyword=laptop
```
**Expected:** Products matching "laptop"

### **Step 11: Get Empty Cart**
```
🛒 Shopping Cart → Get Cart
GET /api/cart/{userId}
```
**Replace {userId} with your user ID from Step 7**
**Expected:** Empty cart initially

### **Step 12: Add Laptop to Cart**
```
🛒 Shopping Cart → Add to Cart
POST /api/cart/{userId}/items?productId=1&quantity=2
```
**Replace {userId} with your user ID**
**Expected:** Cart with 2 laptops

### **Step 13: Add Phone to Cart**
```
🛒 Shopping Cart → Add to Cart
POST /api/cart/{userId}/items?productId=2&quantity=1
```
**Expected:** Cart with 2 laptops + 1 phone

### **Step 14: View Updated Cart**
```
🛒 Shopping Cart → Get Cart
GET /api/cart/{userId}
```
**Expected:** Cart with 2 items and correct total amount

### **Step 15: Update Cart Item Quantity**
```
🛒 Shopping Cart → Update Cart Item
PUT /api/cart/{userId}/items/1?quantity=1
```
**Expected:** Laptop quantity changed from 2 to 1

## 📋 **PHASE 5: Order Management**

### **Step 16: Create Order from Cart**
```
📋 Orders → Create Order
POST /api/orders/{userId}?shippingAddress=123 Test Street, Test City&billingAddress=123 Test Street, Test City
```
**Replace {userId} with your user ID**
**Expected:** Order created with unique order number

### **Step 17: Verify Cart is Empty**
```
🛒 Shopping Cart → Get Cart
GET /api/cart/{userId}
```
**Expected:** Cart should be empty after order creation

### **Step 18: View User Orders**
```
📋 Orders → Get User Orders
GET /api/orders/user/{userId}
```
**Expected:** List of orders for the user

### **Step 19: Get Specific Order**
```
📋 Orders → Get Order by ID
GET /api/orders/1
```
**Expected:** Order details with items

## ⚡ **PHASE 6: Advanced Testing**

### **Step 20: Check Product Stock**
```
📦 Products → Get All Products
GET /api/products
```
**Expected:** Stock quantities should be reduced after order

### **Step 21: Update Order Status**
```
📋 Orders → Update Order Status
PUT /api/orders/1/status?status=CONFIRMED
```
**Expected:** Order status updated to CONFIRMED

### **Step 22: Test Order Cancellation**
```
📋 Orders → Cancel Order
PUT /api/orders/1/cancel
```
**Expected:** Order cancelled, stock restored

## Expected Results Summary:
- ✅ Categories created successfully
- ✅ Products created with proper categorization
- ✅ User registered and can login
- ✅ JWT token received and works for protected endpoints
- ✅ Products can be added to cart
- ✅ Cart calculations are correct
- ✅ Orders created successfully from cart
- ✅ Stock quantities updated automatically
- ✅ Order status can be managed

## Error Testing:
1. **Try invalid login**: Wrong password should return 401
2. **Try accessing protected endpoints without token**: Should return 401
3. **Try adding non-existent product to cart**: Should return 400
4. **Try creating order with empty cart**: Should return 400

## Tips for Insomnia:
- Use environment variables for baseUrl and token
- Save successful responses for reference
- Use the token from login response in Authorization header
- Check application logs in terminal for detailed error information
