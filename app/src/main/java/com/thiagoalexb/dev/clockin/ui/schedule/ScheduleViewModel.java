package com.thiagoalexb.dev.clockin.ui.schedule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.util.DateHelper;
import com.thiagoalexb.dev.clockin.util.Resource;
import com.thiagoalexb.dev.clockin.util.TextHelper;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ScheduleViewModel extends ViewModel {

    private final CompositeDisposable disposable;
    private final AddressService addressService;
    private final ScheduleService scheduleService;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<Resource<List<Schedule>>> schedules;
    private final MutableLiveData<String> currentMonth;

    @Inject
    public ScheduleViewModel(AddressService addressService, ScheduleService scheduleService) {

        this.disposable = new CompositeDisposable();
        this.addressService = addressService;
        this.scheduleService = scheduleService;
        this.address = new MutableLiveData<>();
        this.schedules = new MutableLiveData<>();
        this.currentMonth = new MutableLiveData<>();
        this.currentMonth.setValue(TextHelper.capitalize(DateHelper.getCurrentMonth()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void checkAddress(){
        disposable.add(addressService.get()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((addressDb, throwable) -> {
                            address.setValue(addressDb);
                        }));
    }

    public void checkSchedules(int month){
        disposable.add(
                scheduleService.getByMonth(month)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(schedulesDb ->{
                            schedules.setValue(Resource.success(schedulesDb));
                        }));
    }

    public LiveData<Address> getAddress(){
        return address;
    }

    public LiveData<Resource<List<Schedule>>> getSchedules() {
        return schedules;
    }

    public LiveData<String> getCurrentMonth () { return currentMonth; }
}