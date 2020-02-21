package com.thiagoalexb.dev.clockin.util;

import androidx.databinding.InverseMethod;

import java.time.LocalDateTime;


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
            return value.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static LocalDateTime toLocalDateTime(String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
