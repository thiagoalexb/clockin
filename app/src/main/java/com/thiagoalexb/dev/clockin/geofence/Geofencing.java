package com.thiagoalexb.dev.clockin.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.thiagoalexb.dev.clockin.broadcasts.GeofenceBroadcastReceiver;
import com.thiagoalexb.dev.clockin.data.models.Address;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class Geofencing implements ResultCallback {

    public static final String TAG = Geofencing.class.getSimpleName();
    private static final float GEOFENCE_RADIUS = 150;

    private List<Geofence> geofences;
    private PendingIntent pendingIntent;
    private Context context;
    private GeofencingClient geofencingClient;

    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, result.getStatus().toString());
    }


    public Geofencing(Context context) {
        this.context = context;
        this.pendingIntent = null;
        this.geofences = new ArrayList<>();
        this.geofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void registerAllGeofences() {
        geofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(task -> {
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(aVoid -> {

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Não foi possível ativar o geofence, salve novamente o endereço", Toast.LENGTH_LONG).show();
                    });
        });
    }

    public void updateGeofencesList(Address address) {

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(address.getAddressUUID())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setCircularRegion(address.getLatitude(), address.getLongitude(), GEOFENCE_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            geofences.add(geofence);

    }

    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
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


