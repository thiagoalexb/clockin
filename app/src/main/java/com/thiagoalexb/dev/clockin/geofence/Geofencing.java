package com.thiagoalexb.dev.clockin.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.thiagoalexb.dev.clockin.broadcasts.GeofenceBroadcastReceiver;
import com.thiagoalexb.dev.clockin.data.models.Address;

import java.util.ArrayList;
import java.util.List;

public class Geofencing implements ResultCallback {

    public static final String TAG = Geofencing.class.getSimpleName();
    private static final float GEOFENCE_RADIUS = 50;
    private static final long GEOFENCE_TIMEOUT = 24 * 60 * 60 * 1000;

    private List<Geofence> geofences;
    private PendingIntent pendingIntent;
    private GoogleApiClient googleApiClient;
    private Context context;

    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, result.getStatus().toString());
    }

    public Geofencing(Context context, GoogleApiClient client) {
        this.context = context;
        googleApiClient = client;
        pendingIntent = null;
        geofences = new ArrayList<>();
    }

    public void registerAllGeofences() {

        boolean isConnected = googleApiClient.isConnected();
        if (googleApiClient == null || !isConnected ||
                geofences == null || geofences.size() == 0) {
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {
            Log.e(TAG, securityException.getMessage());
        }
    }

    public void unregisterAllGeofences() {

        if (googleApiClient == null || !googleApiClient.isConnected()) {
            return;
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {
            Log.e(TAG, securityException.getMessage());
        }
    }

    public void updateGeofencesList(Address address) {

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(address.getId().toString())
                    .setExpirationDuration(GEOFENCE_TIMEOUT)
                    .setCircularRegion(address.getLatitude(), address.getLongitude(), GEOFENCE_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            geofences.add(geofence);

    }

    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {

        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }



}


