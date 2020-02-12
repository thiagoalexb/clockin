package com.thiagoalexb.dev.clockin.data.repository;

import android.app.Application;

import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class ScheduleRepository {

    private final AppDatabase _appDatabase;

    @Inject
    public ScheduleRepository(Application application) {
        _appDatabase = AppDatabase.getInstance(application);
    }

    public Single<Long> insert(Schedule schedule){
        return _appDatabase.scheduleDao().insert(schedule)
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> update(Schedule schedule){
        return  _appDatabase.scheduleDao().update(schedule)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<Schedule>> getByMonth(){
        return _appDatabase.scheduleDao().getByMonth()
                .subscribeOn(Schedulers.io());
    }

    public Single<Schedule> getByDay(int year, int month, int day){
        return _appDatabase.scheduleDao().getByDay(year, month, day)
                .subscribeOn(Schedulers.io());
    }
}


