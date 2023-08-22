package com.crking7.datn.services;

import com.crking7.datn.models.Product;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.web.dto.request.SaleRequest;
import com.crking7.datn.web.dto.response.SaleResponse;

import java.util.List;

public interface SaleService {
    SaleResponse getSale(Long id);
    SaleResponse create(SaleRequest saleRequest);
    SaleResponse update(long id, SaleRequest saleRequest);
    List<SaleResponse> getAll(int pageNo, int pageSize, String sortBy);
    void delete(long id);
    void addProductsToSale(Long id, List<Long> productIds);

    void removeProductsFromSale (Long id, List<Long> productIds);

}
