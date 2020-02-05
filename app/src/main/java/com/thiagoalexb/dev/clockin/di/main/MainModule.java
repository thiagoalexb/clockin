package com.thiagoalexb.dev.clockin.di.main;

import android.app.Application;

import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.ui.main.ScheduleAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @MainScope
    @Provides
    static ScheduleAdapter provideAdapter(){
        return new ScheduleAdapter(new ArrayList<>());
    }

    @MainScope
    @Provides
    static AddressRepository providerAddressRepository(Application application){
        return new AddressRepository(application);
    }

}
