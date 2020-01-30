package com.thiagoalexb.dev.clockin.data.converters;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class Converters {
    @TypeConverter
    public static LocalDate toLocalDate(Long longValue) {
        if(longValue == null) return null;

        LocalDate date = Instant.ofEpochMilli(longValue).atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    @TypeConverter
    public static Long fromLocalDate(LocalDate localDate){
        if(localDate == null) return null;

        LocalDateTime localDateTime = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        long millis = zonedDateTime.toInstant().toEpochMilli();
        return millis;
    }

    @TypeConverter
    public static LocalTime toLocalTime(Long longValue) {
        if(longValue == null) return null;

        LocalTime date = Instant.ofEpochMilli(longValue).atZone(ZoneId.systemDefault()).toLocalTime();
        return date;
    }

    @TypeConverter
    public static Long fromLocalTime(LocalTime localTime){
        if(localTime == null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        return calendar.getTimeInMillis();
    }
}
