package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> createOrder(
            @PathVariable("userId") Long userId,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam(value = "billingAddress", required = false) String billingAddress) {
        try {
            OrderDTO order = orderService.createOrder(userId, shippingAddress, billingAddress);
            if (order != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(order);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable("userId") Long userId) {
        List<OrderDTO> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("status") String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            OrderDTO order = orderService.updateOrderStatus(orderId, orderStatus);
            if (order != null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable("orderId") Long orderId) {
        OrderDTO order = orderService.cancelOrder(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
