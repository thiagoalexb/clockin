package com.thiagoalexb.dev.clockin.util;

import androidx.databinding.InverseMethod;


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
}
