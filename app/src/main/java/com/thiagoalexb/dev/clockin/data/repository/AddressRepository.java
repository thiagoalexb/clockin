package com.thiagoalexb.dev.clockin.data.repository;

import android.app.Application;

import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AddressRepository {

    private final AppDatabase _appDatabase;

    @Inject
    public AddressRepository(Application application) {
        _appDatabase = AppDatabase.getInstance(application);
    }

    public Maybe<Address> get(){
        return _appDatabase.addressDao().get()
                .subscribeOn(Schedulers.io());
    }

}
