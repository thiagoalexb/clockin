package com.thiagoalexb.dev.clockin.ui.address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddressViewModel extends ViewModel {

    private static final String TAG = "AddressViewModel";

    private final AddressRepository addressRepository;
    public final MutableLiveData<Address> address;
    private final MutableLiveData<Long> statusInsert;

    private Integer addressId;
    private String addressUUID;

    @Inject
    public AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
        this.address = new MutableLiveData<>();
        this.statusInsert = new MutableLiveData<>();
        this.addressId = 0;
    }

    public void checkAddress(){
        addressRepository.get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((addressDb, throwable) -> {
                    address.setValue(addressDb);
                });
    }

    public void insert(Address address){
        addressRepository.insert(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((status, throwable) -> {
                    statusInsert.setValue(status);
                });
    }

    public LiveData<Address> getAddress(){
        return address;
    }

    public LiveData<Long> getStatusInsert(){
        return statusInsert;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public String getAddressUUID() {
        return addressUUID;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public void setAddressUUID(String addressUUID) {
        this.addressUUID = addressUUID;
    }
}
