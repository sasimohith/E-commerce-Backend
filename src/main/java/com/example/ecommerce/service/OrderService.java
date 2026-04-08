package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    public OrderDTO createOrder(Long userId, String shippingAddress, String billingAddress) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        
        if (user.isEmpty() || cart.isEmpty()) {
            return null;
        }
        
        // Get cart items separately to avoid LAZY loading issues
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.get().getId());
        if (cartItems.isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setUser(user.get());
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setTotalAmount(BigDecimal.ZERO); // Initialize to avoid null constraint violation

        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Save order first to get ID
        order = orderRepository.save(order);

        // Create order items from cart items
        for (CartItem cartItem : cartItems) {
            // Check stock availability
            if (cartItem.getProduct().getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + cartItem.getProduct().getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            
            orderItemRepository.save(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());

            // Update product stock
            productService.updateStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        // Clear the cart
        cartItemRepository.deleteByCartId(cart.get().getId());

        return convertToDTO(order);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(this::convertToDTO).orElse(null);
    }

    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            order = orderRepository.save(order);
            return convertToDTO(order);
        }
        return null;
    }

    public OrderDTO cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == Order.OrderStatus.PENDING || 
                order.getStatus() == Order.OrderStatus.CONFIRMED) {
                
                // Restore stock for cancelled orders
                for (OrderItem item : order.getOrderItems()) {
                    Product product = item.getProduct();
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                }
                
                order.setStatus(Order.OrderStatus.CANCELLED);
                order = orderRepository.save(order);
                return convertToDTO(order);
            }
        }
        return null;
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setShippingAmount(order.getShippingAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setCreatedAt(order.getCreatedAt());
        
        // Handle null or empty orderItems for LAZY loading issues
        List<OrderItemDTO> itemDTOs;
        if (order.getOrderItems() != null) {
            itemDTOs = order.getOrderItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
        } else {
            itemDTOs = List.of(); // Empty list if orderItems is null
        }
        dto.setOrderItems(itemDTOs);
        
        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
}
