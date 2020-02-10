package com.thiagoalexb.dev.clockin.di.main;

import android.app.Application;
import android.location.Geocoder;

import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.data.repository.ScheduleRepository;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.ui.main.ScheduleAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @MainScope
    @Provides
    static ScheduleAdapter provideAdapter(){
        return new ScheduleAdapter();
    }

    @MainScope
    @Provides
    static AddressRepository providerAddressRepository(Application application){
        return new AddressRepository(application);
    }

    @MainScope
    @Provides
    static ScheduleService providerScheduleService(ScheduleRepository scheduleRepository){
        return new ScheduleService(scheduleRepository);
    }

    @MainScope
    @Provides
    static ScheduleRepository providerScheduleRepository(Application application){
        return new ScheduleRepository(application);
    }

    @MainScope
    @Provides
    static Geocoder providerGeocoder(Application application){
        return new Geocoder(application);
    }

}
