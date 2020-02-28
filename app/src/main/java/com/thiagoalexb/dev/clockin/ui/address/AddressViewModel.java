package com.thiagoalexb.dev.clockin.ui.address;

import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.util.Resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class AddressViewModel extends ViewModel {

    private final CompositeDisposable disposable;
    private final Geocoder geocoder;
    private final AddressService addressService;
    private final MutableLiveData<Long> statusInsert;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<Resource<Address>> addressResource;
    private final MutableLiveData<Boolean> isValid;

    @Inject
    public AddressViewModel(Geocoder geocoder, AddressService addressService) {

        this.disposable = new CompositeDisposable();
        this.geocoder = geocoder;
        this.addressService = addressService;
        this.statusInsert = new MutableLiveData<>();
        this.address = new MutableLiveData<>(new Address());
        this.addressResource = new MutableLiveData<>();
        this.isValid = new MutableLiveData<>(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void checkAddress() {
        addressResource.setValue(Resource.loading(null));
        disposable.add(addressService.get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((address, throwable) -> {
                    if (address == null || throwable != null) {
                        addressResource.setValue(Resource.success(null));
                        return;
                    }
                    this.address.setValue(address);
                    addressResource.setValue(Resource.success(address));
                }));
    }

    public void insert() {
        addressResource.setValue(Resource.loading(null));
        Address address = getLocation(getAddress().getValue());

        if (address == null) {
            addressResource.setValue(Resource.success(null));
            return;
        }

        disposable.add(addressService.insert(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((status, throwable) -> {
                    statusInsert.setValue(status);
                    addressResource.setValue(Resource.success(null));
                }));
    }


    @Nullable
    private Address getLocation(Address address) {
        try {
            List<android.location.Address> addresses;
            addresses = geocoder.getFromLocationName(getAddressFormatted(address), 1);

            android.location.Address location = addresses.get(0);

            address.setLatitude(location.getLatitude());
            address.setLongitude(location.getLongitude());

            if(address.getAddressUUID() == null)
                address.setAddressUUID(UUID.randomUUID().toString());

            return address;
        } catch (Exception e) {
            statusInsert.setValue(null);
            return null;
        }
    }

    private String getAddressFormatted(@NotNull Address address) {

        return address.getStreet()
                + ", " + address.getNumber()
                + ", " + address.getNeighborhood()
                + " - " + address.getCity()
                + ", " + address.getState();
    }

    public LiveData<Address> getAddress(){
        isValid.setValue(addressService.isValid(address.getValue()));
        return address;
    }

    public LiveData<Long> getStatusInsert() {
        return statusInsert;
    }

    public LiveData<Boolean> getIsValid(){
        return isValid;

    }

    public LiveData<Resource<Address>> getAddressResource(){
        return addressResource;
    }
}
