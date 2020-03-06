package com.thiagoalexb.dev.clockin.service;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.AddressApiResponse;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.util.TextHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AddressService {

    private final AddressRepository addressRepository;

    @Inject
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Boolean isValid(Address address){
        if(address == null) {
            return false;
        }
        else {
            if(TextHelper.isNullOrEmpty(address.getState())
                    || TextHelper.isNullOrEmpty(address.getCity())
                    || TextHelper.isNullOrEmpty(address.getNeighborhood())
                    || TextHelper.isNullOrEmpty(address.getStreet())
                    || address.getNumber() == null
                    || (address.getNumber() != null && address.getNumber() <= 0))
                return false;
            else
                return true;
        }
    }

    public Single<Address> get(){
        return addressRepository.get();
    }

    public Single<Long> insert(Address address){
        return addressRepository.insert(address);
    }

    public Single<List<AddressApiResponse>> get(Address address){
        return  addressRepository.get(buildQuery(address));
    }

    private String buildQuery(Address address){
        return address.getStreet()
                + ", " + address.getNumber()
                + ", " + address.getNeighborhood()
                + " - " + address.getCity()
                + ", " + address.getState();
    }
}
