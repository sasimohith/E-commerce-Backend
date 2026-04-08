package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public CartDTO getOrCreateCart(Long userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        Cart cart;
        
        if (cartOpt.isPresent()) {
            cart = cartOpt.get();
        } else {
            cart = new Cart();
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                cart.setUser(user.get());
                cart = cartRepository.save(cart);
            } else {
                return null;
            }
        }
        
        return convertToDTO(cart);
    }

    public CartDTO addToCart(Long userId, Long productId, Integer quantity) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Product> product = productRepository.findById(productId);
        
        if (user.isEmpty() || product.isEmpty()) {
            return null;
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user.get());
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product.get());
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product.get());
            newItem.setQuantity(quantity);
            newItem.setPrice(product.get().getPrice());
            cartItemRepository.save(newItem);
        }

        return convertToDTO(cartRepository.findById(cart.getId()).get());
    }

    public CartDTO updateCartItem(Long userId, Long productId, Integer quantity) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        Optional<Product> product = productRepository.findById(productId);
        
        if (cartOpt.isEmpty() || product.isEmpty()) {
            return null;
        }

        Cart cart = cartOpt.get();
        Optional<CartItem> itemOpt = cartItemRepository.findByCartAndProduct(cart, product.get());
        
        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            if (quantity > 0) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            } else {
                cartItemRepository.delete(item);
            }
        }

        return convertToDTO(cartRepository.findById(cart.getId()).get());
    }

    public CartDTO removeFromCart(Long userId, Long productId) {
        return updateCartItem(userId, productId, 0);
    }

    public void clearCart(Long userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            cartItemRepository.deleteByCartId(cartOpt.get().getId());
        }
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        
        // Handle null or empty cartItems for newly created carts
        List<CartItemDTO> itemDTOs;
        if (cart.getCartItems() != null) {
            itemDTOs = cart.getCartItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
        } else {
            itemDTOs = List.of(); // Empty list for new carts
        }
        dto.setCartItems(itemDTOs);
        
        BigDecimal totalAmount = itemDTOs.stream()
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalAmount(totalAmount);
        
        Integer totalItems = itemDTOs.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
        dto.setTotalItems(totalItems);
        
        return dto;
    }

    private CartItemDTO convertItemToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setProductImageUrl(item.getProduct().getImageUrl());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }
}
