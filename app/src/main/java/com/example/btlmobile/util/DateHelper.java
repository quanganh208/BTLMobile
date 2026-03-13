package com.example.btlmobile.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date formatting utilities for Vietnamese locale.
 */
public final class DateHelper {
    private DateHelper() {}

    private static final Locale VN_LOCALE = new Locale("vi", "VN");

    /** Get current date as "dd/MM/yyyy" */
    public static String getCurrentDate() {
        return new SimpleDateFormat(AppConstants.DATE_FORMAT, VN_LOCALE).format(new Date());
    }

    /** Get current month as "MM/yyyy" */
    public static String getCurrentMonth() {
        return new SimpleDateFormat(AppConstants.MONTH_FORMAT, VN_LOCALE).format(new Date());
    }

    /** Format Date object to "dd/MM/yyyy" */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(AppConstants.DATE_FORMAT, VN_LOCALE).format(date);
    }

    /** Format Date object to custom pattern */
    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        return new SimpleDateFormat(pattern, VN_LOCALE).format(date);
    }
}
