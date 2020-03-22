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
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

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

            Comparator<Schedule> comparator = Comparator.comparing( Schedule::getSizeEntry );

            Optional<Schedule> scheduleMaxEntries = schedules.stream().max(comparator);

            int sizeHeaders = (scheduleMaxEntries.get().getSizeEntry() * 2) + 2;

            String[] headers = new String[sizeHeaders];
            headers[0] = "Data";
            headers[1] = "Dia";

            data.add(headers);

            for (int j = 2; j < sizeHeaders; j++){
                headers[j] = j % 2 == 0 ? "Entrada" : "Saída";
            }

            Integer days = schedules.get(0).getDate().toLocalDate().lengthOfMonth();
            Locale locale = DateHelper.getLocale();


            for (int i = 1; i <= days; i++){
                LocalDateTime localDateTime = LocalDateTime.of(year, month, i, 0, 0);
                String date = DateHelper.getMiniumDateDate(localDateTime);
                String dayOfWeek = TextHelper.capitalize(localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, locale));
                String dayOfWeekFormatted = TextHelper.unaccent(dayOfWeek);

                int count = i;

                Optional<Schedule> schedule = schedules.stream().filter(s ->  s.getDate().getYear() == year && s.getDate().getMonthValue() == month && s.getDate().getDayOfMonth() == count)
                                                        .findFirst();

                if(!schedule.isPresent())
                    data.add(new String[]{ date, dayOfWeekFormatted });
                else{
                    int sizeColumns = (schedule.get().getSizeEntry() * 2) + 2;
                    String[] rows = new String[sizeColumns];
                    rows[0] = date;
                    rows[1] = dayOfWeekFormatted;

                    int firstRow = 2;

                    for (int n = 0; n < schedule.get().getEntryTimes().size(); n++){
                        String entry = schedule.get().getEntryTimes().get(n);
                        rows[firstRow] = DateHelper.getHourMinute(entry);
                        firstRow  = firstRow + 2;
                    }

                    firstRow = 3;
                    for (int n = 0; n < schedule.get().getDepartureTimes().size(); n++){
                        String departure = schedule.get().getDepartureTimes().get(n);

                        if(!TextHelper.isNullOrEmpty(departure)){
                             rows[firstRow] = DateHelper.getHourMinute(departure);
                            firstRow  = firstRow + 2;
                        }
                    }

                    data.add(rows);
                }

            }

            writer.writeAll(data);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
