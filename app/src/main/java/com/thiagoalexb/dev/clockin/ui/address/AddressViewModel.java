package com.thiagoalexb.dev.clockin.ui.address;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;

import javax.inject.Inject;

public class AddressViewModel extends ViewModel {

    private static final String TAG = "AddressViewModel";

    @Inject
    public AddressViewModel(AddressRepository addressRepository) {
        if(addressRepository == null)
        Log.d(TAG, "AddressViewModel: kkkkkk errow");
        else
            Log.d(TAG, "AddressViewModel: foite");
    }
}
