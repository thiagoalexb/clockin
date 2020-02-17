package com.thiagoalexb.dev.clockin.service;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.util.TextHelper;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

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
        return addressRepository.get()
                .subscribeOn(Schedulers.io());
    }

    public Single<Long> insert(Address address){
        return addressRepository.insert(address)
                .subscribeOn(Schedulers.io());
    }
}
