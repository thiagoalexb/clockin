package com.thiagoalexb.dev.clockin.data.repository;

import android.app.Application;
import android.util.Pair;

import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.data.models.ScheduleYearMonth;

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

    public Single<Integer> delete(Schedule schedule){
        return _appDatabase.scheduleDao().delete(schedule)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<Schedule>> getByMonth(int month){
        return _appDatabase.scheduleDao().getByMonth(month)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<Schedule>> getByYearMonth(int year, int month){
        return _appDatabase.scheduleDao().getByYearMonth(year, month)
                .subscribeOn(Schedulers.io());
    }

    public Single<Schedule> getByDay(int year, int month, int day){
        return _appDatabase.scheduleDao().getByDay(year, month, day)
                .subscribeOn(Schedulers.io());
    }

    public Single<Schedule> getById(int id){
        return  _appDatabase.scheduleDao().getById(id)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<ScheduleYearMonth>> getYearsMonths(){
        return _appDatabase.scheduleDao().getYearsMonths()
                .subscribeOn(Schedulers.io());
    }
}


