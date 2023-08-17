package com.crking7.datn.web;

import com.crking7.datn.config.Constants;
import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.web.dto.request.CancelOrdersRequest;
import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.crking7.datn.web.dto.request.OrdersRequest;
import com.crking7.datn.web.dto.request.ProductRequest;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.services.OrdersService;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/cart")
public class OrdersRest {
    private final OrdersService ordersService;

    public OrdersRest(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/user/{id}")
    private ResponseEntity<?> getCart(@PathVariable("id") long userId) {
        try {
            OrdersResponse ordersResponse = ordersService.getOrderByType(userId, Constants.CART_TYPE);
            return ResponseEntity.ok(Objects.requireNonNullElse(ordersResponse, "Chưa có sản phẩm nào trong giỏ hàng!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addToCart/{id}")
    public ResponseEntity<?> addItemToOrders(@PathVariable("id") long userId, @RequestBody OrderItemRequest orderItemRequest) {
        try {
            Object ordersResponse = ordersService.addItemToCart(userId, orderItemRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", ordersResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/deleteItemFormCart")
    public ResponseEntity<?> deleteItemFormCart(@RequestParam Long orderItemId) {
        try {
            Object orders = ordersService.deleteItemFromCart(orderItemId);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", orders), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa không thành công! Lỗi " + e.getMessage());
        }
    }

    @DeleteMapping("/delete1Item")
    public ResponseEntity<?> delete1Item(@RequestParam long orderItemId) {
        try {
            Object object = ordersService.delete1Item(orderItemId);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/checkOrder/{id}")
    public ResponseEntity<?> checkCreateOrder(@PathVariable("id") Long orderId) {
        try {
            Object object = ordersService.checkCreateOrder(orderId);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<?> createOrder(@PathVariable("id") Long orderId,
                                         @RequestBody OrdersRequest ordersRequest) {
        try {
            Object object = ordersService.createOrder(orderId, ordersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đặt hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestBody CancelOrdersRequest cancelOrdersRequest) {
        try {
            Object object = ordersService.cancelOrder(cancelOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }
}
