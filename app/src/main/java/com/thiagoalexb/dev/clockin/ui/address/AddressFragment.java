package com.thiagoalexb.dev.clockin.ui.address;


import android.app.Activity;
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
import com.thiagoalexb.dev.clockin.ui.BaseFragment;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

public class AddressFragment extends BaseFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;

    @Inject
    public GoogleApiClient googleApiClient;

    private AddressViewModel addressViewModel;
    private FragmentAddressBinding fragmentAddressBinding;

    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLoading(false);

        addressViewModel = ViewModelProviders.of(this, modelProviderFactory).get(AddressViewModel.class);

        fragmentAddressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        fragmentAddressBinding.setAddressViewModel(addressViewModel);

        fragmentAddressBinding.setLifecycleOwner(this);

        this.setObservers();

        addressViewModel.checkAddress();

        return fragmentAddressBinding.getRoot();
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

        Navigation.findNavController(getView()).navigate(R.id.action_addressFragment_to_mainFragment);
    }
}
