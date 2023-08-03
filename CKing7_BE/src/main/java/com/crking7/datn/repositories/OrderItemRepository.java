package com.crking7.datn.repositories;

import com.crking7.datn.models.Size;
import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findBySizeAndAndOrders(Size size, Orders orders);
}
