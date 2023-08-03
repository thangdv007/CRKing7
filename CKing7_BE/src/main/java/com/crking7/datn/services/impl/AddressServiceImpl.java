package com.crking7.datn.services.impl;

import com.crking7.datn.repositories.AddressRepository;
import com.crking7.datn.web.dto.request.AddressRequest;
import com.crking7.datn.web.dto.response.AddressResponse;
import com.crking7.datn.mapper.AddressMapper;
import com.crking7.datn.models.Address;
import com.crking7.datn.services.AddressService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public AddressResponse createAddress(AddressRequest addressRequest) {
        Address address = addressMapper.mapToModel(addressRequest);
        address.setStatus(1);
        address.setFocus(0);
        Date currentDate = new Date();
        address.setCreatedDate(currentDate);
        address.setModifiedDate(currentDate);
        addressRepository.save(address);
        return addressMapper.mapToResponse(address);
    }

    @Override
    public AddressResponse updateAddress(long addressId, AddressRequest addressRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow();
        addressMapper.updateModel(address, addressRequest);
        Date currentDate = new Date();
        address.setModifiedDate(currentDate);
        addressRepository.save(address);
        return addressMapper.mapToResponse(address);
    }
    @Override
    public void deleteAddress(long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow();
        addressRepository.delete(address);
    }

}
