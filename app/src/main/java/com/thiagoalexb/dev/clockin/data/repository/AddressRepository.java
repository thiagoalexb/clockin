package com.thiagoalexb.dev.clockin.data.repository;

import android.app.Application;

import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.AddressApiResponse;
import com.thiagoalexb.dev.clockin.network.AddressApi;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AddressRepository {

    private final AppDatabase _appDatabase;
    private final AddressApi _addressApi;

    @Inject
    public AddressRepository(Application application, AddressApi addressApi) {
        _appDatabase = AppDatabase.getInstance(application);
        _addressApi = addressApi;

    }

    public Single<Address> get(){
        return _appDatabase.addressDao().get()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<AddressApiResponse>> get(String query){
        return _addressApi.get(query, "json", "1")
                .subscribeOn(Schedulers.io());
    }

    public Single<Long> insert(Address address){
        return _appDatabase.addressDao().insert(address)
                .subscribeOn(Schedulers.io());
    }

}
