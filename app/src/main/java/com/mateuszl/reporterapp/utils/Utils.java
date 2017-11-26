package com.mateuszl.reporterapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static String getDate(String timestamp) {
        try {
            return generateCurrentTime(timestamp, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return "not specified";
        }
    }

    public static String getTime(String timestamp) {
        try {
            return generateCurrentTime(timestamp, new SimpleDateFormat("HH:mm:ss"));
        } catch (Exception e) {
            return "not specified";
        }
    }

    private static String generateCurrentTime(String timestamp, SimpleDateFormat sdf) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(Long.decode(timestamp) * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return sdf.format(currenTimeZone);
    }

}
