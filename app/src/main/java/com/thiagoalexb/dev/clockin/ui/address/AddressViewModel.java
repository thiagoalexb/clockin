package com.thiagoalexb.dev.clockin.ui.address;

import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.util.TextHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddressViewModel extends ViewModel {

    private static final String TAG = "AddressViewModel";

    private final Geocoder geocoder;
    private final AddressService addressService;
    private final MutableLiveData<Long> statusInsert;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<Boolean> isValid;

    @Inject
    public AddressViewModel(Geocoder geocoder, AddressService addressService) {
        this.geocoder = geocoder;
        this.addressService = addressService;

        this.statusInsert = new MutableLiveData<>();
        this.address = new MutableLiveData<>();
        this.address.setValue(new Address());
        this.isValid = new MutableLiveData<>(false);
    }

    public void checkAddress() {
        addressService.get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((addressDb, throwable) -> {
                    if (addressDb == null) return;
                    address.setValue(addressDb);
                });
    }

    public void insert() {

        Address address = getLocation(getAddress().getValue());

        if (address == null) return;

        addressService.insert(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((status, throwable) -> {
                    statusInsert.setValue(status);
                });
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
        String addressFormatted = address.getStreet()
                + ", " + address.getNumber()
                + ", " + address.getNeighborhood()
                + " - " + address.getCity()
                + ", " + address.getState();

        return addressFormatted;
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
}
