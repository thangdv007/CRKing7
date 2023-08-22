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
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.ProductResponse;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final SaleRepository saleRepository;
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
                              SaleRepository saleRepository,
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
        this.saleRepository = saleRepository;
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
        product.setVisited(product.getVisited() + 1);
        productRepository.save(product);
        return productMapper.mapModelToResponse(product);
    }

    @Override
    public ProductResponse getProductAdmin(long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return productMapper.mapModelToResponse(product);
    }

    @Override
    public ProductResponse getProductBySize(long sizeId) {
        Size size = sizeRepository.findById(sizeId).orElseThrow();
        if (size != null) {
            Color color = size.getColor();
            if (color != null) {
                Product product = color.getProduct();
                return productMapper.mapModelToResponse(product);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<ProductResponse> getProductsByKeyword(String keyword, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.searchAllByKeyword(keyword, pageable);
        return products.getContent().stream()
                .map(productMapper::mapModelToResponse)
                .toList();

    }

    @Override
    public List<ProductResponse> getProductsByValueSize(String valueSize, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.searchAllByValueSize(valueSize, pageable);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }

    @Override
    public List<ProductResponse> getProductsByValueColor(String valueColor, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.searchAllByValueColor(valueColor, pageable);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }

    @Override
    public List<ProductResponse> getProductsByPrice(int minPrice, int maxPrice, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.searchAllByPrice(minPrice, maxPrice, pageable);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }

    @Override
    public List<ProductResponse> searchProduct(String valueSize, String valueColor, Integer minPrice, Integer maxPrice, long categoryId, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.searchProductInCategory(valueSize, valueColor, minPrice, maxPrice, categoryId, pageable);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }

    @Override
    public List<ProductResponse> getAllProducts(String keyword, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.getAllByKeyword(keyword, pageable);
        return products.getContent().stream()
                .map(productMapper::mapModelToResponse)
                .toList();
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
        product.setSalePrice(productRequest.getPrice());
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
        List<Color> colors = colorRepository.findByProduct(product1);
        product1.setColors(colors);
        return productMapper.mapModelToResponse(product1);
    }

    @Transactional
    private void updateColorsAndSizes(List<ColorUDRequest> colorRequests, long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        if (colorRequests != null) {
            List<Color> colors = colorRepository.findByProduct(product);

            List<Color> newColors = new ArrayList<>();
            for (ColorUDRequest colorRequest : colorRequests) {

                if (colorRequest.getId() == 0) {
                    // Nếu màu không tồn tại, tạo hình ảnh mới và liên kết nó với sản phẩm.
                    Color newColor = new Color();
                    newColor.setValue(colorRequest.getValue());
                    newColor.setProduct(product);
                    newColor.setSizes(null);
                    Color color = colorRepository.save(newColor);
                    List<SizeUDRequest> sizeRequests = colorRequest.getSizes().stream().toList();
                    for (SizeUDRequest sizeRequest : sizeRequests) {
                        Size newSize = new Size();
                        newSize.setTotal(sizeRequest.getTotal());
                        newSize.setValue(sizeRequest.getValue());
                        newSize.setSold(0);
                        newSize.setColor(newColor);
                        if (newSize.getColor() != null) {
                            sizeRepository.save(newSize);
                        }
                    }
                    List<Size> sizes = sizeRepository.findByColorId(newColor.getId());
                    newColor.setSizes(sizes);
                    newColors.add(color);
                } else {
                    Color color = colorRepository.findById(colorRequest.getId()).orElseThrow();
                    // Nếu hình ảnh đã tồn tại, cập nhật các thuộc tính của nó.
                    color.setValue(colorRequest.getValue());
                    color.setProduct(product);
                    List<SizeUDRequest> sizeRequests = colorRequest.getSizes();
                    if (sizeRequests != null) {
                        List<Size> sizes = sizeRepository.findByColor(color);
                        List<Size> newSizes = new ArrayList<>();
                        for (SizeUDRequest sizeRequest : sizeRequests) {
                            if (sizeRequest.getId() == 0) {
                                Size newSize = new Size();
                                newSize.setTotal(sizeRequest.getTotal());
                                newSize.setValue(sizeRequest.getValue());
                                newSize.setSold(0);
                                newSize.setColor(color);
                                Size size = sizeRepository.save(newSize);
                                newSizes.add(size);
                            } else {
                                Size size = sizeRepository.findById(sizeRequest.getId()).orElseThrow();
                                size.setTotal(sizeRequest.getTotal());
                                size.setValue(sizeRequest.getValue());
                                size.setColor(color);
                            }
                        }
                        sizes.addAll(newSizes);
                    }
                }
            }
            colors.addAll(newColors);
        }
    }


    @Override
    @Transactional
    public ProductResponse updateProduct(ProductUDRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId()).orElseThrow();
        List<ProductImageUDRequest> imageRequests = productRequest.getImages();
        List<ColorUDRequest> colorRequests = productRequest.getColors();
        List<Color> colors = colorRepository.findByProduct(product);
        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        // Lưu trữ giá salePrice hiện tại của sản phẩm
        int currentSalePrice = product.getSalePrice();
        if (product == null) {
            return null;
        }
        productMapper.updateModel2(product, productRequest);
        product.setSalePrice(currentSalePrice);
        Date currentDate = new Date();
        product.setModifiedDate(currentDate);
        product.setColors(colors);
        product.setProductImages(productImages);
        Product uDProduct = productRepository.save(product);
        long productId = uDProduct.getId();
        this.updateColorsAndSizes(colorRequests, productId);
        // Cập nhật hình ảnh
        if (imageRequests != null) {
            List<ProductImage> existingImages = productImageRepository.findByProduct(product);
            List<ProductImage> newImages = new ArrayList<>();
            for (ProductImageUDRequest imageRequest : imageRequests) {
                if (imageRequest.getId() == 0) {
                    // Nếu hình ảnh không tồn tại, tạo hình ảnh mới và liên kết nó với sản phẩm.
                    ProductImage newImage = new ProductImage();
                    newImage.setUrl(imageRequest.getUrl());
                    newImage.setProduct(product);
                    ProductImage image = productImageRepository.save(newImage);
                    newImages.add(image);
                } else {
                    ProductImage existingImage = productImageRepository.findById(imageRequest.getId()).orElseThrow();
                    // Nếu hình ảnh đã tồn tại, cập nhật các thuộc tính của nó.
                    existingImage.setUrl(imageRequest.getUrl());
                }
            }
            existingImages.addAll(newImages);
        }
        Product product1 = productRepository.findById(productId).orElseThrow();
        List<ProductImage> productImagess = productImageRepository.findByProduct(product1);
        product1.setProductImages(productImagess);
        List<Color> colorss = colorRepository.findByProduct(product1);
        product1.setColors(colorss);

        return productMapper.mapModelToResponse(product);
    }

    @Override
    public ProductResponse hideProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow();
        if (product == null) {
            return null; // Không tìm thấy danh mục
        }
        Date currentDate = new Date();
        product.setModifiedDate(currentDate);
        if (product.getStatus() == 1) {
            product.setStatus(0);
        }
        productRepository.save(product);
        return productMapper.mapModelToResponse(product);
    }

    @Override
    public ProductResponse showProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow();
        if (product == null) {
            return null; // Không tìm thấy danh mục
        }
        Date currentDate = new Date();
        product.setModifiedDate(currentDate);
        if (product.getStatus() == 0) {
            product.setStatus(1);
        }
        productRepository.save(product);
        return productMapper.mapModelToResponse(product);
    }

    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getProductBySaleId(Long saleId, int pageNo, int pageSize, String sortBy) {
        Sale sale = saleRepository.findById(saleId).orElseThrow();
        if (sale != null) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<Product> products = productRepository.findAllBySaleAndStatus(sale, pageable, Constants.ACTIVE_STATUS);
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


    @Override
    @Transactional
    public List<ProductResponse> getRelatedProducts(Long categoryId, int limit) {
        List<Product> products = productRepository.findRelatedProducts(categoryId, limit);
        if (products.isEmpty()) {
            return null;
        } else {
            for (Product product : products) {
                Hibernate.initialize(product.getColors()); // Khởi tạo collection colors trong phiên làm việc hiện tại
                Hibernate.initialize(product.getProductImages()); // Khởi tạo collection productImages trong phiên làm việc hiện tại
            }
            return products.stream()
                    .map(product -> productMapper.mapModelToResponse(product))
                    .toList();
        }
    }

    @Override
    public List<ProductResponse> getBestSellerProducts(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.getBestSellerProducts(pageable);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }

    @Override
    public List<ProductResponse> getProductByQuantity(boolean isActive, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Product> products = productRepository.getProductByQuantity(isActive, pageable);
        if (products.isEmpty()) {
            return null;
        } else {
            return products.getContent().stream()
                    .map(productMapper::mapModelToResponse)
                    .toList();
        }
    }
}
