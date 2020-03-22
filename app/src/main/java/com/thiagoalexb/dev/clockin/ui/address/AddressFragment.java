package com.thiagoalexb.dev.clockin.ui.address;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

        this.checkInternet();

        this.setObservers();

        this.setEvents();

        addressViewModel.checkAddress();

        return fragmentAddressBinding.getRoot();
    }

    private void checkInternet(){
        if(isNetworkAvailable()) return;

        createDialogNeedInternet();
    }

    private void createDialogNeedInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle(R.string.internet);
        builder.setMessage(R.string.internet_warning);

        builder.setPositiveButton(R.string.OK, (dialog, id) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setObservers() {

        addressViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        addressViewModel.getStatusInsert().observe(getViewLifecycleOwner(), this::validateInsert);

        addressViewModel.getAddressResource().removeObservers(getViewLifecycleOwner());
        addressViewModel.getAddressResource().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status);
        });

        addressViewModel.getStatusSearch().removeObservers(getViewLifecycleOwner());
        addressViewModel.getStatusSearch().observe(getViewLifecycleOwner(), resource -> {
            Toast.makeText(getContext(), getString(R.string.address_not_found), Toast.LENGTH_LONG).show();
        });
    }

    private void setEvents() {
        fragmentAddressBinding.insertAddressButton.setOnClickListener(view ->{
            if(!isNetworkAvailable()) {
                Toast.makeText(getContext(), getString(R.string.internet_warning), Toast.LENGTH_LONG).show();
                return;
            }
            addressViewModel.insert();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
