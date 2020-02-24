package com.thiagoalexb.dev.clockin.util;

import java.text.Normalizer;

public class TextHelper {

    public static Boolean isNullOrEmpty(String value){
        if(value == null) return true;
        if(value.trim().isEmpty()) return true;
        return false;
    }

    public static String capitalize(String value){
        if(isNullOrEmpty(value))
            return  value;
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
