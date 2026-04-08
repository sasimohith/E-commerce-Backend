package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable("userId") Long userId) {
        CartDTO cart = cartService.getOrCreateCart(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable("userId") Long userId,
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity) {
        CartDTO cart = cartService.addToCart(userId, productId, quantity);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDTO> updateCartItem(
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") Integer quantity) {
        CartDTO cart = cartService.updateCartItem(userId, productId, quantity);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId) {
        CartDTO cart = cartService.removeFromCart(userId, productId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable("userId") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
