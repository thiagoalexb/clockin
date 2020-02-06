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

        fragmentAddressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        fragmentAddressBinding.setLifecycleOwner(this);

        addressViewModel = ViewModelProviders.of(this, modelProviderFactory).get(AddressViewModel.class);

        fragmentAddressBinding.setAddressViewModel(addressViewModel);

        setObservers();

        addressViewModel.checkAddress();

        setElements();

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
            Toast.makeText(getContext(), "Erro ao salvar endereço", Toast.LENGTH_LONG);
            return;
        }

        Navigation.findNavController(getView()).navigate(R.id.action_addressFragment_to_mainFragment);
    }

    private void getLocationFromAddress(String strAddress) {

        try {
            Geocoder coder = new Geocoder(getContext());
            List<Address> address;
            address = coder.getFromLocationName(strAddress, 1);

            if (address == null) {
                Toast.makeText(getContext(), "Endereço não encontrado", Toast.LENGTH_LONG);
                return;
            }

            Address location = address.get(0);


            com.thiagoalexb.dev.clockin.data.models.Address addressDatabase = new com.thiagoalexb.dev.clockin.data.models.Address();


            addressDatabase.setState(fragmentAddressBinding.stateEditText.getText().toString());
            addressDatabase.setCity(fragmentAddressBinding.cityEditText.getText().toString());
            addressDatabase.setNeighborhood(fragmentAddressBinding.neighborhoodEditText.getText().toString());
            addressDatabase.setStreet(fragmentAddressBinding.streetEditText.getText().toString());
            addressDatabase.setNumber(Integer.parseInt(fragmentAddressBinding.numberEditText.getText().toString()));
            addressDatabase.setLatitude(location.getLatitude());
            addressDatabase.setLongitude(location.getLongitude());
            if (addressViewModel.getAddressId() > 0) {
                addressDatabase.setId(addressViewModel.getAddressId());
                addressDatabase.setAddressUUID(addressViewModel.getAddressUUID());
            } else
                addressDatabase.setAddressUUID(UUID.randomUUID().toString());

            addressViewModel.insert(addressDatabase);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setElements() {
        Button button = fragmentAddressBinding.getRoot().findViewById(R.id.save_address_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = fragmentAddressBinding.streetEditText.getText()
                        + ", " + fragmentAddressBinding.numberEditText.getText()
                        + ", " + fragmentAddressBinding.neighborhoodEditText.getText()
                        + " - " + fragmentAddressBinding.cityEditText.getText()
                        + ", " + fragmentAddressBinding.stateEditText.getText();

                getLocationFromAddress(address);
            }
        });
    }

}
