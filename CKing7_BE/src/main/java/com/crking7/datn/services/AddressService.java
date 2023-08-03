package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.AddressRequest;
import com.crking7.datn.web.dto.response.AddressResponse;

public interface AddressService {
    AddressResponse createAddress(AddressRequest addressRequest);
    AddressResponse updateAddress(long addressId, AddressRequest addressRequest);
    void deleteAddress(long addressId);
}
