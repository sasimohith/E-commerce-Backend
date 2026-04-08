package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Order;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime createdAt;
}
