package com.thiagoalexb.dev.clockin.data.converters;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class Converters {

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long longValue) {
        if(longValue == null) return null;

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), TimeZone.getDefault().toZoneId());
        return localDateTime;
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime localDateTime){
        if(localDateTime == null) return null;

        Long milliseconds = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return milliseconds;
    }
}
