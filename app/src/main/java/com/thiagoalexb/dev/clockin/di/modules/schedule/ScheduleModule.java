package com.thiagoalexb.dev.clockin.di.modules.schedule;

import android.app.Application;
import android.location.Geocoder;


import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.data.repository.ScheduleRepository;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.service.ReportService;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;


import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleModule {

    @ScheduleScope
    @Provides
    @Named("search")
    static ScheduleAdapter provideAdapter(){
        return new ScheduleAdapter(false);
    }

    @ScheduleScope
    @Provides
    static ScheduleAdapter provideAdapterSearch(){
        return new ScheduleAdapter(true);
    }

    @ScheduleScope
    @Provides
    static AddressService providerAddressService(Application application){
        return new AddressService(new AddressRepository(application));
    }

    @ScheduleScope
    @Provides
    static ScheduleService providerScheduleService(Application application){
        return new ScheduleService(new ScheduleRepository(application));
    }

    @ScheduleScope
    @Provides
    static ReportService providerReportService(ScheduleService scheduleService){
        return new ReportService(scheduleService);
    }

    @ScheduleScope
    @Provides
    static Geocoder providerGeocoder(Application application){
        return new Geocoder(application);
    }

}
