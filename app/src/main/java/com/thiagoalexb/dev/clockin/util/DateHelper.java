package com.thiagoalexb.dev.clockin.util;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateHelper {
    public static String getMonthName(int monthValue){
        return TextHelper.Capitalize(Month.of(monthValue).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pt", "br")));
    }
}
