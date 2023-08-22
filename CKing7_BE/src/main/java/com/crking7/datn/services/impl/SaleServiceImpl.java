package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.mapper.ArticleMapper;
import com.crking7.datn.mapper.SaleMapper;
import com.crking7.datn.models.*;
import com.crking7.datn.repositories.*;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.services.SaleService;
import com.crking7.datn.web.dto.request.ArticleImageRequest;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.SaleRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.SaleResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;

    private final ProductRepository productRepository;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository,
                           ProductRepository productRepository,
                           SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.saleMapper = saleMapper;
    }

    @Override
    public SaleResponse getSale(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow();
        return saleMapper.mapModelToResponse(sale);
    }

    @Override
    public SaleResponse create(SaleRequest saleRequest) {
        Sale sale = saleRepository.findByName(saleRequest.getName());
        if (sale == null) {
            Sale newSale = saleMapper.mapRequestToModel(saleRequest);
            Date currentDate = new Date();
            newSale.setIsActive(1);
            newSale.setCreatedDate(currentDate);
            newSale.setModifiedDate(currentDate);
            Sale saleResp = saleRepository.save(newSale);
            return saleMapper.mapModelToResponse(saleResp);
        } else {
            return null;
        }
    }

    @Override
    public SaleResponse update(long id, SaleRequest saleRequest) {
        Sale sale = saleRepository.findById(id).orElseThrow();
        if (sale == null) {
            return null;
        } else {
            saleMapper.updateModel(sale, saleRequest);
            Date date = new Date();
            sale.setModifiedDate(date);
            saleRepository.save(sale);
            return saleMapper.mapModelToResponse(sale);
        }
    }

    @Override
    public List<SaleResponse> getAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Sale> sales = saleRepository.findAll(pageable);

        return sales.getContent().stream()
                .map(saleMapper::mapModelToResponse)
                .toList();
    }

    @Override
    public void delete(long id) {
        Sale sale = saleRepository.findById(id).orElseThrow();
        if (sale != null) {
            sale.setProducts(null);
            saleRepository.save(sale);
        }
        saleRepository.delete(sale);
    }

    @Override
    public void addProductsToSale(Long id, List<Long> productIds) {
        Sale sale = saleRepository.findById(id).orElseThrow();

        if (sale != null && productIds != null && !productIds.isEmpty()) {
            List<Product> products = productRepository.findAllById(productIds);

            if (products != null && !products.isEmpty()) {
                List<Product> products1 = productRepository.findBySaleId(sale.getId());
                // Kiểm tra trạng thái (status) của Sale
                if (sale.getIsActive() == 1) {
                    for (Product product : products) {
                        int salePrice = product.getPrice() - (product.getPrice() * sale.getDiscount() / 100);

                        if (products1 != null && products1.contains(product)) {
                            // Cập nhật giá salePrice của sản phẩm khi tồn tại trong products1
                            product.setSalePrice(salePrice);
                        } else {
                            // Cập nhật giá salePrice của sản phẩm khi không tồn tại trong products1
                            product.setSale(sale);
                            product.setSalePrice(salePrice);
                        }
                    }
                }
                // Lưu thay đổi vào cơ sở dữ liệu
                saleRepository.save(sale);
                productRepository.saveAll(products);
            }
        }
    }

    @Override
    @Transactional
    public void removeProductsFromSale(Long saleId, List<Long> productIds) {
        Sale sale = saleRepository.findById(saleId).orElseThrow();
        if (sale != null && productIds != null && !productIds.isEmpty()) {
            List<Product> products = sale.getProducts();
            if (products != null && !products.isEmpty()) {
                // Cập nhật giá salePrice của các sản phẩm về giá ban đầu (price)
                for (Product product : products) {
                    if(productIds.contains(product.getId())) {
                        product.setSale(null);
                        product.setSalePrice(product.getPrice());
                    }
                }
                // Lưu thay đổi vào cơ sở dữ liệu
                saleRepository.save(sale);
                productRepository.saveAll(products);
            }
        }
    }

}
