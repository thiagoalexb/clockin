package com.thiagoalexb.dev.clockin.ui.dayschedules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.TypeSchedule;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.util.Resource;


import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class EditScheduleViewModel extends ViewModel {

    private final CompositeDisposable disposable;
    private final MutableLiveData<Resource<Schedule>> scheduleResource;
    private final MutableLiveData<Schedule> schedule;
    private final MutableLiveData<Long> statusInsert;
    private final ScheduleService scheduleService;


    @Inject
    public EditScheduleViewModel(ScheduleService scheduleService) {

        this.disposable = new CompositeDisposable();
        this.scheduleResource = new MutableLiveData<>();
        this.schedule = new MutableLiveData<>();
        this.statusInsert = new MutableLiveData<>();
        this.scheduleService = scheduleService;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void checkSchedule(int id, int position, TypeSchedule typeSchedule){

        scheduleResource.setValue(Resource.loading(null));
        disposable.add(scheduleService.getById(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((scheduleDb, throwable) -> {
                            scheduleResource.setValue(Resource.success(scheduleDb));
                            schedule.setValue(scheduleDb);
                            if(typeSchedule == TypeSchedule.ENTRY)
                                scheduleDb.setEntryTime(LocalDateTime.parse(scheduleDb.getEntryTimes().get(position)));
                            else
                                scheduleDb.setDepartureTime(LocalDateTime.parse(scheduleDb.getDepartureTimes().get(position)));
                        }));
    }

    public void checkSchedule(int id){

        scheduleResource.setValue(Resource.loading(null));
        disposable.add(scheduleService.getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((scheduleDb, throwable) -> {
                    scheduleResource.setValue(Resource.success(scheduleDb));
                    schedule.setValue(scheduleDb);
                }));
    }

    public void save(int position, TypeSchedule typeSchedule){
        scheduleResource.setValue(Resource.loading(null));

        Schedule schedule = this.schedule.getValue();

        if(typeSchedule == TypeSchedule.ENTRY){
            schedule.getEntryTimes().remove(position);
            schedule.getEntryTimes().add(position, schedule.getEntryTime().toString());
        }else{
            schedule.getDepartureTimes().remove(position);
            schedule.getDepartureTimes().add(position, schedule.getDepartureTime().toString());
        }

        disposable.add(scheduleService.save(schedule)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((status, throwable) -> {
                            scheduleResource.setValue(Resource.success(null));
                            statusInsert.setValue(status);
                        }));
    }

    public LiveData<Resource<Schedule>> getScheduleResource(){
        return scheduleResource;
    }

    public LiveData<Schedule> getSchedule() {
        return schedule;
    }

    public LiveData<Long> getStatusInsert(){
        return statusInsert;
    }
}
