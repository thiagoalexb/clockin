package com.thiagoalexb.dev.clockin.ui.main;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.data.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainViewModel extends ViewModel {

    private final String TAG;
    private final AddressRepository addressRepository;
    private final ScheduleRepository scheduleRepository;
    private final CompositeDisposable disposable;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<List<Schedule>> schedules;

    @Inject
    public MainViewModel(AddressRepository addressRepository, ScheduleRepository scheduleRepository) {

        TAG = MainViewModel.class.getSimpleName();
        this.addressRepository = addressRepository;
        this.scheduleRepository = scheduleRepository;
        disposable = new CompositeDisposable();
        address = new MutableLiveData<>();
        schedules = new MutableLiveData<>();
    }

    public void checkAddress(){
                addressRepository.get()
                    .subscribe(new MaybeObserver<Address>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onSuccess(Address address1) {
                            address.setValue(address1);
                        }

                        @Override
                        public void onError(Throwable e) {
                            address.setValue(new Address());
                        }

                        @Override
                        public void onComplete() {
                            address.setValue(new Address());
                        }
                    });
    }

    public void checkSchedules(){
        disposable.add(
                scheduleRepository.get()
                        .subscribe(addressDb -> {
                            schedules.setValue(addressDb);
                        }, throwable -> {
                            schedules.setValue(new ArrayList<>());
                        }));
    }

    public LiveData<Address> getAddress(){
        return address;
    }

    public LiveData<List<Schedule>> getSchedules() {
        return schedules;
    }
}