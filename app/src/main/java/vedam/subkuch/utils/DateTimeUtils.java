package vedam.subkuch.utils;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

    public static final String TIME_FORMAT_1 = "hh:mm a";
    public static final String TIMEZONE_GMT = "GMT";
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_TIME_FORMAT_1 = "yyyy-MM-dd'T'hh:mm:ss";
    public static String DATE_FORMAT_2 = "dd MMMM yyyy hh:mm aaa";
    public static String DATE_FORMAT_3 = "MM/dd/yyyy";
    public static String DATE_FORMAT_4 = "EEEE, MMMM dd";
    public static String DATE_FORMAT_5 = "EEEE, MMMM dd, yyyy";
    public static String DATE_FORMAT_6 = "EEE, dd MMM";
    public static String DATE_FORMAT_7 = "MMMM, EEEE";
    public static String DATE_FORMAT_8 = "dd";
    public static String DATE_FORMAT_9 = "MMMM EEEE dd";

    public static long getTimeInMillis() {
        Date date = new Date();
        return date.getTime();
    }

    public static String getFormattedDate(long time, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
        return format.format(new Date(time));
    }

    /**
     * Utility method to get a formatted date. Current format is default
     *
     * @param date       Date in String which is to be formatted
     * @param dateFormat The format in which current date is to be formatted
     * @return the formatted date
     */
    public static String getFormattedDate(String date, String dateFormat) {
        return getFormattedDate(date, DEFAULT_DATE_FORMAT, dateFormat);
    }

    /**
     * Utility method to get a formatted date.
     *
     * @param date          Date in String which is to be formatted
     * @param currentFormat The format of unformatted date
     * @param dateFormat    The format in which current date is to be formatted
     * @return the formatted date
     */
    public static String getFormattedDate(String date, String currentFormat, String dateFormat) {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(currentFormat, Locale.US);
        try {
            Date dateInCurrentFormat = currentDateFormat.parse(date);

            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
            return format.format(dateInCurrentFormat);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method gives a date string after min no of days provided. It skips the weekends if excluded
     * It consider now as initial date. The date format is the default format
     *
     * @param days              min no of days after which the date date should be returned
     * @param isWeekendExcluded Weekend should be excluded or not
     * @return the date after min no of days skipping weekends if excluded
     */
    public static String getDateStringAfterDays(int days, boolean isWeekendExcluded) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        if (isWeekendExcluded)
            while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, 1);
            }
        return simpleDateFormat.format(calendar.getTime());
    }

    public static long getDateInMillis(String date, String dateFormat) {

        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

        try {
            Date d = format.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Utility method to return calendar after n days from now
     *
     * @param numberOfDay number of days after which calendar is to be returned. Skips weekends
     * @return calendar after n days
     */
    public static Calendar getCalendarAfterDays(int numberOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numberOfDay);
        while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar;
    }

    /**
     * Returns calendar from date string. It assumes the date format to be default
     *
     * @param date date string
     * @return calendar or null if date cannot be parsed with default format
     */
    public static Calendar getCalendarFromDateString(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US);
        Calendar calendar = null;
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        return calendar;
    }

    public static int getDaysBetweenDates(String endDateStr, String startDateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            Date startDate = formatter.parse(startDateStr);
            Date endDate = formatter.parse(endDateStr);
            long diff = endDate.getTime() - startDate.getTime();
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            return 0;
        }

    }

}
