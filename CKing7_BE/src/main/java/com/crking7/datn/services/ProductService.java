package com.crking7.datn.services;

import com.crking7.datn.models.Product;
import com.crking7.datn.web.dto.request.ProductRequest;
import com.crking7.datn.web.dto.request.ProductUDRequest;
import com.crking7.datn.web.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    /**
     *
     */
    List<ProductResponse> getProducts(int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getAllProducts(String keyword, int pageNo, int pageSize, String sortBy);

    ProductResponse getProduct(long productId);

    ProductResponse getProductByName(String name);

    ProductResponse getProductAdmin(long productId);

    ProductResponse getProductBySize(long sizeId);

    List<ProductResponse> getProductBySaleAdmin(String Keyword, Long saleId);

    List<ProductResponse> getProductsByKeyword(String keyword, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getProductsByCategory(long categoryId, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getProductsByValueSize(String valueSize, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getProductsByValueColor(String valueColor, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getProductsByPrice(int minPrice, int maxPrice, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> searchProduct(String valueSize, String valueColor, Integer minPrice, Integer maxPrice, long categoryId, int pageNo, int pageSize, String sortBy);

    //    List<ProductResponse> getProductByColorSizePriceCategory(String valueSize, String valueColor, int minPrice, int maxPrice, long categoryId, int pageNo, int pageSize, String sortBy);
    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductUDRequest productRequest);

    ProductResponse hideProduct(long id);

    ProductResponse showProduct(long id);

    void deleteProduct(long id);

    List<ProductResponse> getProductBySaleId(Long saleId, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getRelatedProducts(Long categoryId, int limit);

    List<ProductResponse> getBestSellerProducts(int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getProductByQuantity(boolean isActive, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getProductNoSale(String keyword, int pageNo, int pageSize, String sortBy);
}
