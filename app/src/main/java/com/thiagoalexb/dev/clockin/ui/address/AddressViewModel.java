package com.thiagoalexb.dev.clockin.ui.address;

import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.util.Resource;

import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class AddressViewModel extends ViewModel {

    private final CompositeDisposable disposable;
    private final AddressService addressService;
    private final MutableLiveData<Long> statusInsert;
    private final MutableLiveData<Long> statusSearch;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<Resource<Address>> addressResource;
    private final MutableLiveData<Boolean> isValid;

    @Inject
    public AddressViewModel(AddressService addressService) {

        this.disposable = new CompositeDisposable();
        this.addressService = addressService;
        this.statusInsert = new MutableLiveData<>();
        this.statusSearch = new MutableLiveData<>();
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

        Address address = getAddress().getValue();
        addressResource.setValue(Resource.loading(null));
        disposable.add(addressService.get(address)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((addressApiResponses, throwable) -> {
                    if(throwable != null) {
                        addressResource.postValue(Resource.success(null));
                        statusSearch.postValue(null);
                        return;
                    }

                    if(addressApiResponses.size() == 0) {
                        addressResource.postValue(Resource.success(null));
                        statusSearch.postValue(null);
                        return;
                    }

                    address.setLatitude(Float.parseFloat(addressApiResponses.get(0).getLat()));
                    address.setLongitude(Float.parseFloat(addressApiResponses.get(0).getLon()));

                    if(address.getAddressUUID() == null)
                        address.setAddressUUID(UUID.randomUUID().toString());

                    disposable.add(addressService.insert(address)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((status, throwableInsert) -> {
                                statusInsert.setValue(status);
                                addressResource.setValue(Resource.success(null));
                            }));
                })
        );

    }

    public LiveData<Address> getAddress(){
        isValid.setValue(addressService.isValid(address.getValue()));
        return address;
    }

    public LiveData<Long> getStatusInsert() {
        return statusInsert;
    }

    public LiveData<Long> getStatusSearch() {
        return statusSearch;
    }

    public LiveData<Boolean> getIsValid(){
        return isValid;

    }

    public LiveData<Resource<Address>> getAddressResource(){
        return addressResource;
    }
}
