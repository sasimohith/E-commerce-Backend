package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String shippingAddress;
    private String billingAddress;
}
