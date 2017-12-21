package com.mateuszl.reporterapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Klasa zawierająca metody użyteczności ogólnej
 */
public class Utils {

    /**
     * @param timestamp zmienany na obiekt 'Date'
     * @return Obiekt 'Date' w formacie "yyyy-MM-dd HH:mm:ss"
     */
    public static String getDate(String timestamp) {
        try {
            return generateCurrentTime(timestamp, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return "not specified";
        }
    }

    /**
     * @param timestamp zmienany na obiekt 'Date'
     * @return Obiekt 'Date' w formacie "HH:mm:ss"
     */
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
        Date currentTimeZone = calendar.getTime();
        return sdf.format(currentTimeZone);
    }

}
