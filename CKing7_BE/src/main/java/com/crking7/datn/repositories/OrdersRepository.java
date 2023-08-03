package com.crking7.datn.repositories;

import com.crking7.datn.models.Orders;
import com.crking7.datn.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByUserAndType(long userId, int type);
    Orders findByUserAndType(User user, int type);
}
