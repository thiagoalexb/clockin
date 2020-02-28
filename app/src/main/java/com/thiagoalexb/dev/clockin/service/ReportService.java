package com.thiagoalexb.dev.clockin.service;

import android.os.Environment;

import com.opencsv.CSVWriter;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.util.DateHelper;
import com.thiagoalexb.dev.clockin.util.TextHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class ReportService {

    private final ScheduleService scheduleService;

    public static String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClockInSheet.csv");

    @Inject
    public ReportService(ScheduleService scheduleService){

        this.scheduleService = scheduleService;
    }

    public void buildSheet(List<Schedule> schedules, int year, int month){
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Data", "Dia", "Hora Entrada", "Hora Saida"});

            Integer days = schedules.get(0).getDate().toLocalDate().lengthOfMonth();
            Locale locale = DateHelper.getLocale();

            for (int i = 1; i <= days; i++){
                LocalDateTime localDateTime = LocalDateTime.of(year, month, i, 0, 0);
                String date = DateHelper.getDate(localDateTime, "dd/MM/yyyy");
                String dayOfWeek = TextHelper.capitalize(localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, locale));
                String dayOfWeekFormatted = TextHelper.unaccent(dayOfWeek);
                Integer position = i - 1;
                if(position >= schedules.size())
                    data.add(new String[]{ date, dayOfWeekFormatted, "", "", "" });
                else{
                    Schedule schedule = schedules.get(position);
                    data.add(new String[]{date, dayOfWeekFormatted, DateHelper.getHourMinute(schedule.getEntryTime()), DateHelper.getHourMinute(schedule.getDepartureTime()) });
                }
            }

            writer.writeAll(data);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}