package com.thiagoalexb.dev.clockin.service;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class AddressService {

    private final AddressRepository addressRepository;

    @Inject
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
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
