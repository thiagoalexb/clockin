package com.thiagoalexb.dev.clockin.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.data.models.ScheduleYearMonth;
import com.thiagoalexb.dev.clockin.service.ReportService;
import com.thiagoalexb.dev.clockin.service.ScheduleService;
import com.thiagoalexb.dev.clockin.util.DateHelper;
import com.thiagoalexb.dev.clockin.util.Resource;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ReportViewModel extends ViewModel {

    private final ScheduleService scheduleService;
    private final ReportService reportService;
    private final MutableLiveData<List<Integer>> years;
    private final MutableLiveData<List<String>> monthNames;
    private final MutableLiveData<List<Integer>> months;
    private final MutableLiveData<Boolean> isValid;
    private final MutableLiveData<Resource<List<Schedule>>> schedules;

    private Integer year;
    private Integer month;

    @Inject
    public ReportViewModel(ScheduleService scheduleService, ReportService reportService){

        this.scheduleService = scheduleService;
        this.reportService = reportService;
        this.years = new MutableLiveData<>();
        this.monthNames = new MutableLiveData<>();
        this.months = new MutableLiveData<>();
        this.isValid = new MutableLiveData<>(false);
        this.schedules = new MutableLiveData<>();

        init();
    }

    private void init(){
        scheduleService.getYearsMonths()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scheduleYearMonths -> {
                    List<Integer> years = new ArrayList<>();
                    List<Integer> months = new ArrayList<>();
                    List<String> monthNames = new ArrayList<>();
                    for (ScheduleYearMonth scheduleYearMonth : scheduleYearMonths){
                        years.add(scheduleYearMonth.getYear());
                        months.add(scheduleYearMonth.getMonth());
                        monthNames.add(DateHelper.getMonthName(scheduleYearMonth.getMonth()));
                    }

                    this.years.setValue(years);
                    this.monthNames.setValue(monthNames);
                    this.months.setValue(months);
                });
    }

    public void search(){
        scheduleService.getByYearMonth(year, month)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedules -> {
                    this.schedules.setValue(Resource.success(schedules));
                });
    }

    public void print(){
        reportService.buildSheet(year, month);
    }

    public void setYear(Integer value){
        year = value;
        isValid.setValue(year != null && year > 0 && month != null && month > 0);
    }

    public void setMonth(Integer value){
        month = months.getValue().get(value);
        isValid.setValue(year != null && year > 0 && month != null && month > 0);
    }

    public LiveData<Boolean> getIsValid(){
        return isValid;
    }

    public LiveData<List<Integer>> getYears(){
        return years;
    }

    public LiveData<List<String>> getMonthNames(){
        return monthNames;
    }

    public LiveData<Resource<List<Schedule>>> getSchedules() {
        return schedules;
    }

}
