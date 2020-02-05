package com.thiagoalexb.dev.clockin.data.repository;

import android.app.Application;

import com.thiagoalexb.dev.clockin.data.AppDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddressRepository {

    private final AppDatabase _appDatabase;

    @Inject
    public AddressRepository(Application application) {
        _appDatabase = AppDatabase.getInstance(application);
    }
}
