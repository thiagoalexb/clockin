package com.thiagoalexb.dev.clockin.ui.address;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.databinding.FragmentAddressBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddressFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory modelProviderFactory;

    private AddressViewModel addressViewModel;
    private FragmentAddressBinding fragmentAddressBinding;


    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addressViewModel = ViewModelProviders.of(this, modelProviderFactory).get(AddressViewModel.class);

        fragmentAddressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        fragmentAddressBinding.setAddressViewModel(addressViewModel);

        fragmentAddressBinding.setLifecycleOwner(this);

        setObservers();

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
