package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.response.BannerResponse;

import java.util.List;

public interface BannerService {
    /**
     * get number of banners
     */
    List<BannerResponse> getNumberOfBanners(int number);

    List<BannerResponse> getNumberOfBannersByCategory(long categoryId, int number);

    BannerResponse createBanner(BannerRequest bannerRequest);

    BannerResponse getBannerById(long id);

    List<BannerResponse> getBanners(int pageNo, int pageSize, String sortBy);
    List<BannerResponse> getAllBanners(int pageNo, int pageSize, String sortBy);

    void deleteBanner(long id);

    BannerResponse updateBanner(long id, BannerRequest bannerRequest);

    BannerResponse hideBanner(long id);
}
