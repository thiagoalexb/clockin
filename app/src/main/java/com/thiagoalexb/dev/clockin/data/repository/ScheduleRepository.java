package com.thiagoalexb.dev.clockin.data.repository;

import android.app.Application;

import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class ScheduleRepository {

    private final AppDatabase _appDatabase;

    @Inject
    public ScheduleRepository(Application application) {
        _appDatabase = AppDatabase.getInstance(application);
    }

    public Flowable<List<Schedule>> get(){
        return _appDatabase.scheduleDao().getByMonth()
                .subscribeOn(Schedulers.io());
    }
}


