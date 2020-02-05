package com.thiagoalexb.dev.clockin.ui.main;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private final String tag;
    private final AddressRepository addressRepository;
    private final CompositeDisposable disposable;
    private final MutableLiveData<Address> address;

    @Inject
    public MainViewModel(AddressRepository addressRepository) {

        tag = MainViewModel.class.getSimpleName();
        this.addressRepository = addressRepository;
        disposable = new CompositeDisposable();
        address = new MutableLiveData<>();
    }

    public void checkAddress(){
        disposable.add(
                addressRepository.get()
                    .subscribe(addressDb -> {
                        address.setValue(addressDb);
                    }, throwable -> {
                        address.setValue(null);
                    }));
    }

    public LiveData<Address> getAddress(){
        return address;
    }
}