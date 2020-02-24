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
import io.reactivex.schedulers.Schedulers;

public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Inject
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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

        scheduleRepository.getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> {
                }, throwable -> {
                    scheduleRepository.insert(scheduleDb)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((aLong, throwable1) -> {
                                Log.d("", "saveEntry: ");
                            });
                });
    }

    public void saveDeparture(){
        LocalDate now = LocalDate.now();

        scheduleRepository.getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((schedule, throwable) -> {
                    if(throwable != null) return;
                    if(schedule.getEntryTime() == null) return;
                    if(schedule.getDepartureTime() != null) return;

                    schedule.setDepartureTime(LocalDateTime.now());
                    scheduleRepository.update(schedule)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((aInt, throwableUpdate) -> {
                                Log.d("", "saveDeparture: ");
                            });
                });
    }

    public Single<Long> save(Schedule schedule){
        return scheduleRepository.insert(schedule);
    }

    public Flowable<List<ScheduleYearMonth>> getYearsMonths(){
        return scheduleRepository.getYearsMonths();
    }
}
