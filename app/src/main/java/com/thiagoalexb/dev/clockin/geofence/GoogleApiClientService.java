package com.thiagoalexb.dev.clockin.geofence;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.thiagoalexb.dev.clockin.service.AddressService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class GoogleApiClientService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final Application application;
    private final AddressService addressService;
    private final Geofencing geofencing;
    private final GoogleApiClient googleApiClient;

    @Inject
    public GoogleApiClientService(Application application, AddressService addressService, GoogleApiClient googleApiClient, Geofencing geofencing){
        this.application = application;
        this.addressService = addressService;
        this.googleApiClient = googleApiClient;
        this.geofencing = geofencing;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(application.getApplicationContext(), "conectado", Toast.LENGTH_LONG);

        addressService.get().observeOn(AndroidSchedulers.mainThread())
                .subscribe((addressDb, throwable) -> {
                    if (addressDb == null) return;
                    geofencing.updateGeofencesList(addressDb);
                    geofencing.registerAllGeofences();
                });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(application.getApplicationContext(), "Suspenso", Toast.LENGTH_LONG);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(application.getApplicationContext(), "Erro ao se conectar", Toast.LENGTH_LONG);
    }
}
