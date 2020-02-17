package com.thiagoalexb.dev.clockin.di.modules;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.thiagoalexb.dev.clockin.geofence.Geofencing;
import com.thiagoalexb.dev.clockin.geofence.GoogleApiClientService;
import com.thiagoalexb.dev.clockin.service.AddressService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    static GoogleApiClient providerGoogleApiClient(Application application){
        return new GoogleApiClient.Builder(application)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Singleton
    @Provides
    static Geofencing providerGeofencing(Application application, GoogleApiClient googleApiClient){
        return new Geofencing(application, googleApiClient);
    }

    @Singleton
    @Provides
    static GoogleApiClientService providerGoogpleGoogleApiClientService(Application application, AddressService addressService, GoogleApiClient googleApiClient, Geofencing geofencing){
        return new GoogleApiClientService(application, addressService, googleApiClient, geofencing);
    }
}
