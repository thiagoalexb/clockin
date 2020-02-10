package com.thiagoalexb.dev.clockin.ui.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.geofence.Geofencing;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.FragmentMainBinding;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MainFragment extends DaggerFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    @Inject
    public ScheduleAdapter scheduleAdapter;

    private MainViewModel mainViewModel;
    private GoogleApiClient googleApiClient;
    private Geofencing geofencing;
    private FragmentMainBinding fragmentMainBinding;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        mainViewModel = ViewModelProviders.of(this, modelProviderFactory).get(MainViewModel.class);

        getLocationPermission();

        setObservers();

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        setElements();
        setGoogleClient();
    }

    @Override
    public void onStart() {
        super.onStart();
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            setGoogleClient();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(googleApiClient != null  && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: CONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNECTED MALUCOW");

        setGeofencing();

        mainViewModel.checkAddress();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void getLocationPermission() {
        requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    private void setGoogleClient() {
        if(googleApiClient == null)
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        else
            googleApiClient.connect();
    }

    private void setGeofencing() {
        geofencing = new Geofencing(getContext(), googleApiClient);
    }

    private void setElements() {
        fragmentMainBinding.placeBtn.setOnClickListener(view -> navigateToAddress(view));
        fragmentMainBinding.schedulesRecyclerView.setAdapter(scheduleAdapter);
        fragmentMainBinding.schedulesRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
    }

    private void navigateToAddress(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addressFragment);
    }

    private void createDialogAddressNotification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.address);
        builder.setMessage(R.string.address_mandatory);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                navigateToAddress(getView());
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setObservers(){

        mainViewModel.getAddress().removeObservers(getViewLifecycleOwner());
        mainViewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
            validateAddress(address);
            mainViewModel.checkSchedules();
        });

        mainViewModel.getSchedules().removeObservers(getViewLifecycleOwner());
        mainViewModel.getSchedules().observe(getViewLifecycleOwner(), schedules -> {
            validateSchedules(schedules);
        });
    }

    private void validateAddress(Address address){
        if(address == null)
            navigateToAddress(getView());
        else {
            geofencing.updateGeofencesList(address);
            geofencing.unregisterAllGeofences();
            geofencing.registerAllGeofences();
        }
    }

    private void validateSchedules(List<Schedule> schedules){
        if(schedules == null) return;

        scheduleAdapter.setSchedules(schedules);
    }
}
