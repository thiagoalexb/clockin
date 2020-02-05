package com.thiagoalexb.dev.clockin.ui.address;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;

import javax.inject.Inject;

public class AddressViewModel extends ViewModel {

    private static final String TAG = "AddressViewModel";

    @Inject
    AddressRepository addressRepository;

    public AddressViewModel() {
        if(addressRepository == null)
            Log.d(TAG, "AddressViewModel: NULLZAO");
        else
            Log.d(TAG, "AddressViewModel: NOTNULLZAO");
    }
}
