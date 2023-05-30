package com.example.mealsapp.utils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String getString(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return  format.format(date);
    }

    public static String getDayName(Date date) {
        Format f = new SimpleDateFormat("EEEE");
        return f.format(date);
    }

    public static Date getDateObject(String dateStr) {
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getString(int year, int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return  format.format(calendar.getTime());
    }
}
