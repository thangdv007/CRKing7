package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.ProductRequest;
import com.crking7.datn.web.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    /**
     *
     */
    List<ProductResponse> getProducts(int pageNo, int pageSize, String sortBy);
    ProductResponse getProduct(long productId);
    ProductResponse getProductBySize(long sizeId);
    List<ProductResponse> getProductsByCategory(long categoryId, int pageNo, int pageSize, String sortBy);
    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct (long id,ProductRequest productRequest);

    ProductResponse hideProduct (long id);

    void deleteProduct (long id);

}
