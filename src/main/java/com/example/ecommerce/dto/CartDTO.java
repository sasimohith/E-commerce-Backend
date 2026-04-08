package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> cartItems;
    private BigDecimal totalAmount;
    private Integer totalItems;
}
