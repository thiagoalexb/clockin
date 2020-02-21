package com.thiagoalexb.dev.clockin.util;

import androidx.databinding.InverseMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Converters {

    @InverseMethod("toInteger")
    public static String toString(Integer value) {
        try {
            return Integer.toString(value);
        } catch (Exception e) {
            return "";
        }
    }

    public static Integer toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    @InverseMethod("toLocalDateTime")
    public static String dateToString(LocalDateTime value) {
        try {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm");
            return value.format(pattern);
        } catch (Exception e) {
            return "";
        }
    }

    public static LocalDateTime toLocalDateTime(String value) {
        try {
            String[] arr = value.split(":");

            LocalDate localDate = LocalDate.now();

            LocalDateTime local = localDate.atTime(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
            return local;
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
