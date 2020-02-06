package com.thiagoalexb.dev.clockin.ui.main;

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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private final String TAG;
    private final AddressRepository addressRepository;
    private final ScheduleRepository scheduleRepository;
    private final CompositeDisposable disposable;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<List<Schedule>> schedules;

    @Inject
    public MainViewModel(AddressRepository addressRepository, ScheduleRepository scheduleRepository) {

        this.TAG = MainViewModel.class.getSimpleName();
        this.addressRepository = addressRepository;
        this.scheduleRepository = scheduleRepository;
        this.disposable = new CompositeDisposable();
        this.address = new MutableLiveData<>();
        this.schedules = new MutableLiveData<>();
    }

    public void checkAddress(){
                addressRepository.get()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((addressDb, throwable) -> {
                            address.setValue(addressDb);
                        });
    }

    public void checkSchedules(){
        disposable.add(
                scheduleRepository.get()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(schedulesDb ->{
                            schedules.setValue(schedulesDb);
                        } ));
    }

    public LiveData<Address> getAddress(){
        return address;
    }

    public LiveData<List<Schedule>> getSchedules() {
        return schedules;
    }
}