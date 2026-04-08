# E-commerce End-to-End Testing Checklist

## 🎯 **Current Status**
- ✅ Frontend: Running on `http://localhost:3000`
- ✅ MySQL: Running (2 processes detected)
- 🟡 Backend: Starting on `http://localhost:8080`

## 📋 **End-to-End Testing Workflow**

### **Phase 1: Backend API Testing**
- [ ] **Health Check**: `GET /actuator/health`
- [ ] **User Authentication**:
  - [ ] Register: `POST /api/users/register`
  - [ ] Login: `POST /api/users/login`
- [ ] **Product Management**:
  - [ ] Get all products: `GET /api/products`
  - [ ] Get product by ID: `GET /api/products/{id}`
  - [ ] Search products: `GET /api/products/search?keyword=`
- [ ] **Category Management**:
  - [ ] Get all categories: `GET /api/categories`
- [ ] **Cart Management**:
  - [ ] Add to cart: `POST /api/cart/add`
  - [ ] Get cart: `GET /api/cart`
  - [ ] Update cart: `PUT /api/cart/{id}`
- [ ] **Order Management**:
  - [ ] Create order: `POST /api/orders`
  - [ ] Get orders: `GET /api/orders`

### **Phase 2: Frontend Integration Testing**
- [ ] **Authentication Flow**:
  - [ ] Registration form → API call → Success message
  - [ ] Login form → API call → JWT token storage
  - [ ] Protected route access
- [ ] **Product Browsing**:
  - [ ] Product list loads from API
  - [ ] Product search functionality
  - [ ] Category filtering
  - [ ] Product detail view
- [ ] **Shopping Cart**:
  - [ ] Add to cart button → API call → Cart update
  - [ ] Cart page shows correct items
  - [ ] Quantity updates work
- [ ] **Checkout Process**:
  - [ ] Cart → Checkout form → Order creation
  - [ ] Order confirmation

### **Phase 3: Complete User Journey Testing**
1. **Guest User Journey**:
   ```
   Home → Products → Product Detail → Register → Login → Add to Cart → Checkout → Order
   ```

2. **Returning User Journey**:
   ```
   Login → Products → Add to Cart → View Cart → Checkout → Order History
   ```

## 🔧 **Testing Tools Available**
- **Postman Collection**: `postman_collection.json`
- **Insomnia Collection**: `insomnia_collection.json`
- **Manual Browser Testing**: Frontend interfaces

## 🐛 **Common Issues to Check**
- [ ] CORS configuration
- [ ] JWT token handling
- [ ] Database connectivity
- [ ] API response formats
- [ ] Error handling
- [ ] Loading states

## 📊 **Success Criteria**
- ✅ All API endpoints respond correctly
- ✅ Frontend-backend communication works
- ✅ User can complete full purchase flow
- ✅ Data persists correctly in database
- ✅ Error handling works properly
