package com.thiagoalexb.dev.clockin.ui.address;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.databinding.FragmentAddressBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.geofence.Geofencing;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;

import java.util.Objects;

import javax.inject.Inject;

public class AddressFragment extends BaseFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;

    private AddressViewModel addressViewModel;
    private FragmentAddressBinding fragmentAddressBinding;
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

    private void setObservers() {

        addressViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        addressViewModel.getStatusInsert().observe(getViewLifecycleOwner(), this::validateInsert);

        addressViewModel.getAddressResource().removeObservers(getViewLifecycleOwner());
        addressViewModel.getAddressResource().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status);
        });
    }

    private void validateInsert(Long status){

        if(status == null) {
            Toast.makeText(getContext(), getString(R.string.error_address_not_save), Toast.LENGTH_LONG).show();
            return;
        }

        setGeofencing();
    }

    private void setGeofencing(){

        Address address = addressViewModel.getAddress().getValue();

        geofencing = new Geofencing(getContext());

        geofencing.updateGeofencesList(Objects.requireNonNull(address));

        geofencing.registerAllGeofences();

        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_addressFragment_to_mainFragment);
    }
}
