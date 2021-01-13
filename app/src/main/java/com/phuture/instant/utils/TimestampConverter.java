package com.phuture.instant.utils;

import androidx.room.TypeConverter;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimestampConverter {
    static DateFormat rssDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
    static DateFormat dbDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
    static DateFormat appDateFormat = new SimpleDateFormat("HH:mm (MM/dd)", Locale.getDefault());

    public static String toStr(Date value) {
        if (value != null) {
            try {
                return appDateFormat.format(value);
            } catch (Exception e) {}

        }
        return "";
    }

    @TypeConverter
    public static String toTimestamp(Date value) {
        if (value != null) {
            try {
                return dbDateFormat.format(value);
            } catch (Exception e) {}
        }
        return null;
    }

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return rssDateFormat.parse(value);
            } catch (ParseException e) {
                try {
                    return dbDateFormat.parse(value);
                } catch (ParseException e2) {
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
