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
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class DaySchedulesViewModel extends ViewModel {

    private final CompositeDisposable disposable;
    private final MutableLiveData<Resource<Schedule>> scheduleResource;
    private final MutableLiveData<Schedule> schedule;
    private final MutableLiveData<Long> statusInsert;
    private final ScheduleService scheduleService;


    @Inject
    public DaySchedulesViewModel(ScheduleService scheduleService) {

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

    public void checkSchedule(int id){

        scheduleResource.setValue(Resource.loading(null));
        disposable.add(scheduleService.getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((scheduleDb, throwable) -> {
                    scheduleResource.setValue(Resource.success(scheduleDb));

                    if(throwable != null)
                    {
                        schedule.setValue(null);
                        return;
                    }

                    scheduleDb.setEntryTimes(sortSchedules(scheduleDb.getEntryTimes()));
                    scheduleDb.setDepartureTimes(sortSchedules(scheduleDb.getDepartureTimes()));

                    schedule.setValue(scheduleDb);
                }));
    }

    private ArrayList<String> sortSchedules(ArrayList<String> schedules){
        if(schedules == null) return new ArrayList<>();

        Collections.sort(schedules);
        return schedules;
    }

    public void updateTime(int position, TypeSchedule typeSchedule, LocalDateTime newTime){
        scheduleResource.setValue(Resource.loading(null));

        Schedule schedule = this.schedule.getValue();

        if(typeSchedule == TypeSchedule.ENTRY){
            schedule.getEntryTimes().remove(position);
            schedule.getEntryTimes().add(position, newTime.toString());
        }else{
            schedule.getDepartureTimes().remove(position);
            schedule.getDepartureTimes().add(position, newTime.toString());
        }

        disposable.add(scheduleService.save(schedule)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((status, throwable) -> {
                            scheduleResource.setValue(Resource.success(null));
                            statusInsert.setValue(status);
                        }));
    }

    public void insertEntry(LocalDateTime entryTime){
        Schedule schedule = this.schedule.getValue();
        schedule.getEntryTimes().add(entryTime.toString());

        disposable.add(scheduleService.save(schedule)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((status, throwable) -> {
                    scheduleResource.setValue(Resource.success(null));
                    statusInsert.setValue(status);
                }));
    }

    public void insertDeparture(LocalDateTime departureTime){
        Schedule schedule = this.schedule.getValue();
        ArrayList<String> departures = schedule.getDepartureTimes();

        if(departures == null)
            departures = new ArrayList<>();

        departures.add(departureTime.toString());

        schedule.setDepartureTimes(departures);

        disposable.add(scheduleService.save(schedule)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((status, throwable) -> {
                    scheduleResource.setValue(Resource.success(null));
                    statusInsert.setValue(status);
                }));
    }

    public void deleteSchedule(int position, TypeSchedule typeSchedule){
        scheduleResource.setValue(Resource.loading(null));

        Schedule schedule = this.schedule.getValue();

        if(typeSchedule == TypeSchedule.ENTRY)
            schedule.getEntryTimes().remove(position);
        else
            schedule.getDepartureTimes().remove(position);

        if(schedule.getEntryTimes().size() == 0){
            disposable.add(scheduleService.delete(schedule)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((status, throwable) -> {
                        scheduleResource.setValue(Resource.success(null));
                        statusInsert.setValue(Long.parseLong(status.toString()));
                    }));
        }else{
            disposable.add(scheduleService.save(schedule)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((status, throwable) -> {
                        scheduleResource.setValue(Resource.success(null));
                        statusInsert.setValue(status);
                    }));
        }
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
