package com.crking7.datn.web.admin;

import com.crking7.datn.config.Constants;
import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.services.OrdersService;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/order")
public class AOrdersRest {
    private final OrdersService ordersService;

    public AOrdersRest(OrdersService ordersService) {
        this.ordersService = ordersService;
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

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.confirmOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/shipping")
    public ResponseEntity<?> shipOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.shipOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/success")
    public ResponseEntity<?> successOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.successOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/empcancel")
    public ResponseEntity<?> cancelOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.cancelOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(
            @RequestBody UDOrdersRequestAdmin udOrdersRequest) {
        try {
            String s = ordersService.updateOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/countOrders")
    public ResponseEntity<?> countOrders(@RequestParam(required = false) Integer status) {
        try {
            Long countOrders = ordersService.countOrders(status);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", countOrders), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/totalSold")
    public ResponseEntity<?> getTotalSoldProducts() {
        try {
            Long t = ordersService.getTotalSoldProducts();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", t), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getOrder(@Param(value = "status") Integer status,
                                      @Param(value = "startDate") String startDate,
                                      @Param(value = "endDate") String endDate,
                                      @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                      @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {

        try {
            List<OrdersResponse> ordersResponses = ordersService.getOrder(status, startDate, endDate, pageNo, pageSize, sortBy, sortDirection.equals("desc"));
            if (ordersResponses != null) {
                int total = ordersResponses.size();
                List<Object> data = new ArrayList<>(ordersResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                int total = 0;
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }
    @GetMapping("/conversionRate")
    public ResponseEntity<?> conversionRate() {
        try {
            double d = ordersService.conversionRateType();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", d), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }
    @GetMapping("/averageProcessingTime")
    public ResponseEntity<?> conversionRate(@RequestParam(name = "status") int status ) {
        try {
            long l = ordersService.averageProcessingTime(status);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", l), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }
}
