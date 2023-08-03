package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.mapper.ColorMapper;
import com.crking7.datn.mapper.ProductMapper;
import com.crking7.datn.models.*;
import com.crking7.datn.repositories.*;
import com.crking7.datn.services.ProductService;
import com.crking7.datn.utils.Utils;
import com.crking7.datn.mapper.ProductImageMapper;
import com.crking7.datn.mapper.SizeMapper;
import com.crking7.datn.web.dto.request.ColorRequest;
import com.crking7.datn.web.dto.request.ProductImageRequest;
import com.crking7.datn.web.dto.request.ProductRequest;
import com.crking7.datn.web.dto.request.SizeRequest;
import com.crking7.datn.web.dto.response.ProductResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SizeMapper sizeMapper;
    private final ColorMapper colorMapper;
    private final ProductImageMapper productImageMapper;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductImageRepository productImageRepository;
    private final Utils utils;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper,
                              SizeMapper sizeMapper,
                              ColorMapper colorMapper,
                              ProductImageMapper productImageMapper,
                              CategoryRepository categoryRepository,
                              ColorRepository colorRepository,
                              SizeRepository sizeRepository,
                              ProductImageRepository productImageRepository,
                              Utils utils) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.sizeMapper = sizeMapper;
        this.colorMapper = colorMapper;
        this.productImageMapper = productImageMapper;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productImageRepository = productImageRepository;
        this.utils = utils;
    }

    @Override
    public List<ProductResponse> getProducts(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.findAllByStatus(pageable, Constants.ACTIVE_STATUS);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }

    @Override
    public ProductResponse getProduct(long productId) {
        Product product = productRepository.findByIdAndStatus(productId, Constants.ACTIVE_STATUS);
        if (product != null) {
            return productMapper.mapModelToResponse(product);
        } else {
            return null;
        }
    }

    @Override
    public ProductResponse getProductBySize(long sizeId) {
        Size size = sizeRepository.findById(sizeId).orElseThrow();
        Color color = size.getColor();
        Product product = color.getProduct();
        return productMapper.mapModelToResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(long categoryId, int pageNo, int pageSize, String sortBy) {
        Category category = categoryRepository.findByStatusAndIdAndType(Constants.ACTIVE_STATUS, categoryId, Constants.PRODUCT_TYPE);
        if (category != null) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<Product> products = productRepository.findAllByProductCategoryAndStatus(pageable, category, Constants.ACTIVE_STATUS);

            if (products.isEmpty()) {
                return null;
            } else {
                return products.getContent().stream()
                        .map(productMapper::mapModelToResponse)
                        .toList();
            }
        } else {
            return null;
        }
    }

    private void saveColorsAndSizes(List<ColorRequest> colorRequests, long productId) {
        if (colorRequests != null) {
            for (ColorRequest colorRequest : colorRequests) {
                colorRequest.setProductId(productId);
                Color color = colorMapper.mapRequestedToModel(colorRequest);
                color.setSizes(null);
                Color newColor = colorRepository.save(color);
                List<SizeRequest> sizeRequests = colorRequest.getSizes().stream().toList();
                for (SizeRequest sizeRequest : sizeRequests) {
                    Size size = sizeMapper.mapRequestToModel(sizeRequest);
                    size.setSold(0);
                    size.setColor(newColor);
                    if (size.getColor() != null) {
                        sizeRepository.save(size);
                    }
                }
                List<Size> sizes = sizeRepository.findByColorId(color.getId());
                color.setSizes(sizes);
            }
        }

    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        long lastId = -1;
        try {
            lastId = productRepository.findNewestId();
        } catch (Exception ignored) {
        }
        String sku = utils.generateRandomCharacters(2) + String.valueOf(productRequest.getUserId()) +
                utils.generateRandomCharacters(3) + String.valueOf(lastId + 1);

        Product product = productMapper.mapRequestedToModel(productRequest);
        product.setColors(null);
        product.setSku(sku);
        // set current date
        Date currentDate = new Date();
        product.setCreatedDate(currentDate);
        product.setModifiedDate(currentDate);

        product.setStatus(1);

        product.setProductImages(null);

        // Save new product to database
        Product newProduct = productRepository.save(product);

        // Get id of product create recently
        long productId = newProduct.getId();

        List<ColorRequest> colorRequests = productRequest.getColors();
        this.saveColorsAndSizes(colorRequests, productId);

        for (ProductImageRequest pImg : productRequest.getImages()) {
            ProductImage image = new ProductImage();
            image.setUrl(pImg.getUrl());
            image.setProduct(productRepository.findById(productId).orElseThrow());
            productImageRepository.save(image);
        }
        // Save image into table image
//        this.saveImages(productRequest.getImages(), productId);


        Product product1 = productRepository.findById(productId).orElseThrow();
        List<ProductImage> productImages = productImageRepository.findByProduct(product1);
        product1.setProductImages(productImages);
        List<Color> colors = colorRepository.findByProductId(productId);
        product1.setColors(colors);
        return productMapper.mapModelToResponse(product1);
    }

    @Transactional
    private void updateColorsAndSizes(List<ColorRequest> colorRequests, long productId) {

    }


    @Override
    @Transactional
    public ProductResponse updateProduct(long id, ProductRequest productRequest) {

        return null;
    }


    @Override
    public ProductResponse hideProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow();
        if (product == null) {
            return null; // Không tìm thấy danh mục
        }
        Date currentDate = new Date();
        product.setModifiedDate(currentDate);
        product.setStatus(0);
        productRepository.save(product);
        return productMapper.mapModelToResponse(product);
    }

    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
    }
}
