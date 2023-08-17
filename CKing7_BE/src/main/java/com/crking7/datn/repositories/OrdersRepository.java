package com.crking7.datn.repositories;

import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import com.crking7.datn.models.Product;
import com.crking7.datn.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByUserAndType(long userId, int type);
    Orders findByUserAndType(User user, int type);
    Orders findByCodeOrders(String codeOrders);
    @Query("SELECT COUNT(o) FROM Orders o WHERE (:status IS NULL OR o.status = :status)")
    Long countOrdersByStatus(@Param("status") Integer status);
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.orders.status = 4")
    Long totalSoldProducts();
    @Query("SELECT o FROM Orders o WHERE (:status IS NULL OR o.status = :status) " +
            "AND (:startDate IS NULL OR o.modifiedDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.modifiedDate <= :endDate)")
    Page<Orders> getAllByStatus(@Param("status") Integer status,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate,
                                Pageable pageable);

    long countByType(int type);

    @Query("SELECT AVG(o.modifiedDate - o.createDate) FROM Orders o WHERE o.status = :status")
    long calculateAverageProcessingTime(@Param("status") int status);
}
