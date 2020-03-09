package com.thiagoalexb.dev.clockin.service;

import android.util.Log;
import android.util.Pair;

import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.data.models.ScheduleYearMonth;
import com.thiagoalexb.dev.clockin.data.repository.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ScheduleService {

    private final CompositeDisposable disposable;
    private final ScheduleRepository scheduleRepository;

    @Inject
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.disposable = new CompositeDisposable();
    }

    public Single<Schedule> getById(int id){
        return scheduleRepository.getById(id);
    }

    public Flowable<List<Schedule>> getByMonth(int month){
        return scheduleRepository.getByMonth(month);
    }

    public Flowable<List<Schedule>> getByYearMonth(int year, int month){
        return scheduleRepository.getByYearMonth(year, month);
    }

    public void saveEntry(){
        LocalDateTime now = LocalDateTime.now();

        Schedule scheduleDb = new Schedule(now, now, now.getDayOfMonth(), now.getMonthValue(), now.getYear());

        disposable.add(scheduleRepository.getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> {
                    disposable.clear();
                }, throwable -> {
                    disposable.add(scheduleRepository.insert(scheduleDb)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((aLong, throwable1) -> {
                                disposable.clear();
                            }));
                }));
    }

    public void saveDeparture(){
        LocalDate now = LocalDate.now();

        disposable.add(scheduleRepository.getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((schedule, throwable) -> {
                    disposable.clear();
                    if(throwable != null) return;
                    if(schedule.getEntryTime() == null) return;
                    if(schedule.getDepartureTime() != null) return;

                    schedule.setDepartureTime(LocalDateTime.now());
                    disposable.add(scheduleRepository.update(schedule)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((aInt, throwableUpdate) -> {
                                disposable.clear();
                            }));
                }));
    }

    public Single<Long> save(Schedule schedule){
        return scheduleRepository.insert(schedule);
    }

    public Flowable<List<ScheduleYearMonth>> getYearsMonths(){
        return scheduleRepository.getYearsMonths();
    }
}
