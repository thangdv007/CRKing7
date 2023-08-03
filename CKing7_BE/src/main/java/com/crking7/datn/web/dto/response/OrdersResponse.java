package com.crking7.datn.web.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrdersResponse {
    private long id;
    private int type;
    private int status;
    private Date createDate;
    private Date modifiedDate;
    private UserResponse userResponse;
    private AddressResponse addressResponse;
    private List<OrderItemResponse> items;
}
