package com.crking7.datn.mapper;

import com.crking7.datn.models.Orders;
import com.crking7.datn.web.dto.response.OrdersResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = OrderItemMapper.class)
public interface OrdersMapper {
    @Mapping(target = "items", source = "orderItems")
    OrdersResponse mapToResponse(Orders orders);
}
