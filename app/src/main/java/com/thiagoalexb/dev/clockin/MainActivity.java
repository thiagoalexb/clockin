package com.thiagoalexb.dev.clockin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Application;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.thiagoalexb.dev.clockin.geofence.Geofencing;
import com.thiagoalexb.dev.clockin.geofence.GoogleApiClientService;
import com.thiagoalexb.dev.clockin.service.AddressService;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    public GoogleApiClient googleApiClient;

    @Inject
    GoogleApiClientService googleApiClientService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setGeofencing();
    }

    private void setGeofencing() {
        if(googleApiClient == null) return;

        googleApiClient.registerConnectionCallbacks(googleApiClientService);
        googleApiClient.registerConnectionFailedListener(googleApiClientService);
    }
}
