package com.thiagoalexb.dev.clockin.ui.address;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.databinding.FragmentAddressBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.geofence.Geofencing;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddressFragment extends DaggerFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    @Inject
    ViewModelProviderFactory modelProviderFactory;

    private AddressViewModel addressViewModel;
    private FragmentAddressBinding fragmentAddressBinding;
    private GoogleApiClient googleApiClient;
    private Geofencing geofencing;


    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addressViewModel = ViewModelProviders.of(this, modelProviderFactory).get(AddressViewModel.class);

        fragmentAddressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        fragmentAddressBinding.setAddressViewModel(addressViewModel);

        fragmentAddressBinding.setLifecycleOwner(this);

        this.setObservers();

        addressViewModel.checkAddress();

        return fragmentAddressBinding.getRoot();
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

        geofencing = new Geofencing(getContext(), googleApiClient);

        Address address = addressViewModel.getAddress().getValue();

        geofencing.unregisterAllGeofences();
        geofencing.updateGeofencesList(address);
        geofencing.registerAllGeofences();

        Navigation.findNavController(getView()).navigate(R.id.action_addressFragment_to_mainFragment);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void setObservers() {

        addressViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        addressViewModel.getStatusInsert().observe(getViewLifecycleOwner(), status -> {
            validateInsert(status);
        });
    }

    private void validateInsert(Long status){
        if(status == null) {
            Toast.makeText(getContext(), getString(R.string.error_address_not_save), Toast.LENGTH_LONG).show();
            return;
        }

        setGoogleClient();
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
}
