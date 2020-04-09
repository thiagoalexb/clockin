package com.thiagoalexb.dev.clockin.service;

import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.data.models.ScheduleYearMonth;
import com.thiagoalexb.dev.clockin.data.repository.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

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

        disposable.add(scheduleRepository.getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> {
                    ArrayList<String> entryTimes = schedule.getEntryTimes();
                    entryTimes.add(now.toString());
                    schedule.setEntryTimes(entryTimes);
                    disposable.add(scheduleRepository.update(schedule)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((aLong, throwable1) -> {
                                disposable.clear();
                            }));
                }, throwable -> {
                    ArrayList<String> entryTimes = new ArrayList<>();
                    entryTimes.add(now.toString());
                    Schedule scheduleDb = new Schedule(now, entryTimes, now.getDayOfMonth(), now.getMonthValue(), now.getYear());
                    disposable.add(scheduleRepository.insert(scheduleDb)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((aLong, throwable1) -> {
                                disposable.clear();
                            }));
                }));
    }

    public void saveDeparture(){
        LocalDateTime now = LocalDateTime.now();

        disposable.add(scheduleRepository.getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((schedule, throwable) -> {
                    disposable.clear();
                    if(throwable != null) return;
                    if(schedule.getEntryTimes() == null) return;

                    ArrayList<String> departureTimes = schedule.getDepartureTimes();
                    if(departureTimes == null)
                        departureTimes = new ArrayList<>();


                    departureTimes.add(now.toString());

                    schedule.setDepartureTimes(departureTimes);

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

    public Single<Integer> delete(Schedule schedule){
        return scheduleRepository.delete(schedule);
    }

    public Flowable<List<ScheduleYearMonth>> getYearsMonths(){
        return scheduleRepository.getYearsMonths();
    }
}
