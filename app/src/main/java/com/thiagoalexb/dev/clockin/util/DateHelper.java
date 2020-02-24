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
        String dateFormatted = localDateTime.format(
                DateTimeFormatter.ofLocalizedDate( FormatStyle.MEDIUM )
                        .withLocale(new Locale("pt", "br")));

        return dateFormatted;
    }

    public static String getDate(LocalDateTime localDateTime, String pattern){
        DateTimeFormatter patterDateTimeFormattern = DateTimeFormatter.ofPattern(pattern);
        String date = localDateTime.format(patterDateTimeFormattern);
        return date;
    }

    public static String getHourMinute(LocalDateTime localDateTime){
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm");
        String hourMinute = localDateTime.format(pattern);
        return hourMinute;
    }
}
