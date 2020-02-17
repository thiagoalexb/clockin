package com.thiagoalexb.dev.clockin.util;

public class TextHelper {

    public static Boolean isNullOrEmpty(String value){
        if(value == null) return true;
        if(value.trim().isEmpty()) return true;
        return false;
    }
}
