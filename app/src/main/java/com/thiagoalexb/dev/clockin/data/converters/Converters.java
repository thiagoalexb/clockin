package com.thiagoalexb.dev.clockin.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        if(value == null) return  null;

        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        if(list == null) return  null;

        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
