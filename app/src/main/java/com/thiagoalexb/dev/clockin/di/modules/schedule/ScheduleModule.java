package com.thiagoalexb.dev.clockin.di.modules.schedule;

import android.app.Application;
import android.location.Geocoder;


import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.data.repository.ScheduleRepository;
import com.thiagoalexb.dev.clockin.network.AddressApi;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.service.ReportService;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.ui.dayschedules.DayScheduleAdapter;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;


import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

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
    static AddressApi provideAddressApi(Retrofit retrofit){
        return retrofit.create(AddressApi.class);
    }

    @ScheduleScope
    @Provides
    static AddressRepository providerAddressRepository(Application application, AddressApi addressApi){
        return new AddressRepository(application, addressApi);
    }

    @ScheduleScope
    @Provides
    static AddressService providerAddressService(AddressRepository addressRepository){
        return new AddressService(addressRepository);
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
