package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.mapper.BannerMapper;
import com.crking7.datn.models.Banner;
import com.crking7.datn.models.Category;
import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.exceptions.ResourceNotFoundException;
import com.crking7.datn.repositories.BannerRepository;
import com.crking7.datn.repositories.CategoryRepository;
import com.crking7.datn.services.BannerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final BannerMapper bannerMapper;

    public BannerServiceImpl(BannerRepository bannerRepository, CategoryRepository categoryRepository, BannerMapper bannerMapper) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.bannerMapper = bannerMapper;
    }

    @Override
    public List<BannerResponse> getNumberOfBanners(int number) {
        Pageable pageable = PageRequest.of(0, number, Sort.by("id").descending());
        Page<Banner> banners = bannerRepository.findByStatus(pageable, Constants.ACTIVE_STATUS);
        if (!banners.isEmpty()){
            return banners.getContent().stream()
                    .map(bannerMapper::mapModelToResponse)
                    .toList();
        }
        return null;
    }

    @Override
    public List<BannerResponse> getNumberOfBannersByCategory(long categoryId, int number) {
        Pageable pageable = PageRequest.of(0, number, Sort.by("id").descending());

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

        Page<Banner> banners = bannerRepository.findByCategoryAndStatus(pageable, category, Constants.ACTIVE_STATUS);
        if (!banners.isEmpty()){
            return banners.getContent().stream()
                    .map(bannerMapper::mapModelToResponse)
                    .toList();
        }
        return null;
    }

    @Override
    public BannerResponse createBanner(BannerRequest bannerRequest) {
        Category category = categoryRepository.findById(bannerRequest.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category", "id", bannerRequest.getCategoryId()));;
        if(category == null){
            BannerResponse errMessage = new BannerResponse();
            errMessage.setMsg("Danh mục không tồn tại");
            return  errMessage;
        }
        else {
            Banner banner = bannerRepository.findByName(bannerRequest.getName());
            if(banner != null){
                return null;
            }else{
                Banner bannerNew = bannerMapper.mapRequestToModel(bannerRequest);
                Date date = new Date();
                bannerNew.setCreatedDate(date);
                bannerNew.setModifiedDate(date);
                bannerNew.setStatus(1);
                bannerNew.setMsg("Tạo banner thành công");
                bannerNew.setCategory(category);
                Banner bannerResp = bannerRepository.save(bannerNew);
                BannerResponse bannerResponse = bannerMapper.mapModelToResponse(bannerResp);
                return bannerResponse;
            }
        }
    }

    @Override
    public BannerResponse getBannerById(long id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));
        if(banner != null ){
            return bannerMapper.mapModelToResponse(banner);
        }else{
            BannerResponse errMessage = new BannerResponse();
            errMessage.setMsg("Banner không tồn tại");
            return errMessage;
        }
    }

    @Override
    public List<BannerResponse> getBanners(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Banner> banners  = bannerRepository.findByStatus(pageable, Constants.ACTIVE_STATUS);
        if (!banners.isEmpty()){
            return banners.stream()
                    .map(bannerMapper::mapModelToResponse)
                    .toList();
        }else {
            return null;
        }
    }
    @Override
    public List<BannerResponse> getAllBanners(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Banner> banners  = bannerRepository.findAll(pageable);
        if (!banners.isEmpty()){
            return banners.stream()
                    .map(bannerMapper::mapModelToResponse)
                    .toList();
        }else {
            return null;
        }
    }
    @Override
    public void deleteBanner(long id ) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));
        if (banner != null ){
            banner.setCategory(null);
            bannerRepository.save(banner);
        }
        bannerRepository.delete(banner);
    }

    @Override
    public BannerResponse updateBanner(long id, BannerRequest bannerRequest) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));
        if (banner == null) {
            return null; // Không tìm thấy banner
        }
        bannerMapper.updateModel(banner, bannerRequest);
        Date currentDate = new Date();
        banner.setModifiedDate(currentDate);
        bannerRepository.save(banner);
        return bannerMapper.mapModelToResponse(banner);
    }

    @Override
    public BannerResponse hideBanner(long id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));
        if (banner == null) {
            return null; // Không tìm thấy banner
        }
        Date currentDate = new Date();
        banner.setModifiedDate(currentDate);
        banner.setStatus(0);
        bannerRepository.save(banner);
        return bannerMapper.mapModelToResponse(banner);
    }
}