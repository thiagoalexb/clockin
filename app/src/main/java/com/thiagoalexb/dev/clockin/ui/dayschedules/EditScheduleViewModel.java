package com.thiagoalexb.dev.clockin.ui.dayschedules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.util.Resource;


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

    public void checkSchedule(int id){

        scheduleResource.setValue(Resource.loading(null));
        disposable.add(scheduleService.getById(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((scheduleDb, throwable) -> {
                            scheduleResource.setValue(Resource.success(scheduleDb));
                            schedule.setValue(scheduleDb);
                        }));
    }

    public void save(){
        scheduleResource.setValue(Resource.loading(null));
        disposable.add(scheduleService.save(this.schedule.getValue())
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
