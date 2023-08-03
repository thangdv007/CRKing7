package com.crking7.datn.web;

import com.crking7.datn.config.Constants;
import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.services.OrdersService;
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
    private ResponseEntity<?> getCart(@PathVariable("id") long userId){
        try{
            OrdersResponse ordersResponse = ordersService.getOrderByType(userId, Constants.CART_TYPE);
            return ResponseEntity.ok(Objects.requireNonNullElse(ordersResponse,"Chưa có sản phẩm nào trong giỏ hàng!"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi", HttpStatus.BAD_REQUEST);
        }
    }
     @PostMapping("/addToCart/{id}")
    public ResponseEntity<?> addItemToOrders(@PathVariable("id") long userId, @RequestBody OrderItemRequest orderItemRequest){
        try{
            OrdersResponse ordersResponse = ordersService.addItemToCart(userId,orderItemRequest);
            return ResponseEntity.ok(Objects.requireNonNullElse(ordersResponse,"Thêm vào giỏ hàng thất bại"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi", HttpStatus.BAD_REQUEST);
        }

    }
}
