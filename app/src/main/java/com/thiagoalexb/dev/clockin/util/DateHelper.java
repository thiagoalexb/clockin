package com.thiagoalexb.dev.clockin.util;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateHelper {
    public static String getMonthName(int monthValue){
        return TextHelper.capitalize(Month.of(monthValue).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pt", "br")));
    }

    public static String getMediumDate(LocalDateTime localDateTime){
        if(localDateTime == null) return "";

        String dateFormatted = localDateTime.format(
                DateTimeFormatter.ofLocalizedDate( FormatStyle.MEDIUM )
                        .withLocale(getLocale()));

        return dateFormatted;
    }

    public static String getMiniumDateDate(LocalDateTime localDateTime){
        if(localDateTime == null) return "";

        String dateFormatted = localDateTime.format(
                DateTimeFormatter.ofLocalizedDate( FormatStyle.SHORT )
                        .withLocale(getLocale()));

        return dateFormatted;
    }

    public static String getHourMinute(LocalDateTime localDateTime){
        if(localDateTime == null) return "";

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm");
        String hourMinute = localDateTime.format(pattern);
        return hourMinute;
    }

    public static Locale getLocale(){
        return new Locale("pt", "br");
    }

    public static String getCurrentMonth(){
        return LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL, DateHelper.getLocale());
    }
}
