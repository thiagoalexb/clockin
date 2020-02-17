package com.thiagoalexb.dev.clockin.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.service.AddressService;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.util.Resource;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private final String TAG;
    private final AddressService addressService;
    private final ScheduleService scheduleService;
    private final CompositeDisposable disposable;
    private final MutableLiveData<Address> address;
    private final MutableLiveData<Resource<List<Schedule>>> schedules;
    private final MutableLiveData<String> currentMonth;

    @Inject
    public MainViewModel(AddressService addressService, ScheduleService scheduleService) {

        this.TAG = MainViewModel.class.getSimpleName();
        this.addressService = addressService;
        this.scheduleService = scheduleService;
        this.disposable = new CompositeDisposable();
        this.address = new MutableLiveData<>();
        this.schedules = new MutableLiveData<>();
        this.currentMonth = new MutableLiveData<>();
        this.currentMonth.setValue(setCaptalize(LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "br"))));
    }

    public void checkAddress(){
        addressService.get()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((addressDb, throwable) -> {
                            address.setValue(addressDb);
                        });
    }

    public void checkSchedules(){
        disposable.add(
                scheduleService.getByMonth()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(schedulesDb ->{
                            List<Schedule> list = new ArrayList<>();
                            for (int i = 1; i < 10; i++){
                                LocalDateTime now = LocalDateTime.now();
                                now = now.plusDays(i);
                                Schedule sc = new Schedule(now, now, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
                                now = now = now.plusHours(8);
                                sc.setDepartureTime(now);
                                list.add(sc);
                            }
                            schedules.setValue(Resource.success(list));
                        } ));
    }

    private String setCaptalize(String text){
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public LiveData<Address> getAddress(){
        return address;
    }

    public LiveData<Resource<List<Schedule>>> getSchedules() {
        return schedules;
    }

    public LiveData<String> getCurrentMonth () { return currentMonth; }
}