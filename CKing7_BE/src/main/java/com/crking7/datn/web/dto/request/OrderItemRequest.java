package com.crking7.datn.web.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private long sizeId;
    private int quantity;
    private int sellPrice;
}
