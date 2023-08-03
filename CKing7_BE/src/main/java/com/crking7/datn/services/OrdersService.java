package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.crking7.datn.web.dto.response.OrdersResponse;

public interface OrdersService {
    OrdersResponse getOrderByType(long userId, int type);
    OrdersResponse addItemToCart(long userId, OrderItemRequest orderItemRequest);
}
