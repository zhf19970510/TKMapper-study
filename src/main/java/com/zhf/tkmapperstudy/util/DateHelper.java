package com.zhf.tkmapperstudy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static String Date2String(LocalDate date, Object... pattern) {
        if (date == null)
            return "";
        String formatDate = null;

        if (pattern != null && pattern.length > 0) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern[0].toString());
            formatDate = date.format(df);
        } else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatDate = date.format(df);
        }
        return formatDate;
    }

    public static LocalDate String2Date(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocalDate.MIN;
    }


    public static String DateTime2String(LocalDateTime dateTime, Object... pattern) {
        if (dateTime == null)
            return "";
        String formatDate = null;

        if (pattern != null && pattern.length > 0) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern[0].toString());
            formatDate = dateTime.format(df);
        } else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            formatDate = dateTime.format(df);
        }
        return formatDate;
    }

    public static Date getYesterDay(Date currentDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
}
