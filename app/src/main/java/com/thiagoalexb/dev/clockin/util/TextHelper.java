package com.thiagoalexb.dev.clockin.util;

public class TextHelper {

    public static Boolean isNullOrEmpty(String value){
        if(value == null) return true;
        if(value.trim().isEmpty()) return true;
        return false;
    }

    public static String Capitalize(String value){
        if(isNullOrEmpty(value))
            return  value;
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}
