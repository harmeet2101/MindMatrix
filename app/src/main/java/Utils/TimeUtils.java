package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ganesha on 23/11/16.
 */

public class TimeUtils {

    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMER_FORMAT = "%02d:%02d:%02d";
    public static final String CHAT_TIME_FORMAT = "hh:mm a";
    public static final String HOUR_FORMAT = "hh aaa";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String QUIZ_TIME_FORMAT="EEE, MMM d, yyyy";
    public static final String BLOG_TIME_FORMAT="MMM d, yyyy";

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static long getTimeDiff(String first_date, String secound_date) {

        long difference = 0;
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);

        try {
            Date date1 = format.parse(first_date);

            Date date2 = format.parse(secound_date);

            difference = date1.getTime() - date2.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return difference;

    }


    public static String getGmtTime() {

        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        //Date date1 = sdf.parse(new);

        return sdf.format(new Date());


    }

    public static long getLocalTime(String message_time) {

        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);


        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        try {
            format.setTimeZone(TimeZone.getTimeZone(tz.getDisplayName()));

            Date date1 = format.parse(message_time);

            return date1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public static String getFormattedTime(long time, String time_format) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        String timezone = tz.getDisplayName();

        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(time_format);
        // format.setTimeZone(TimeZone.getTimeZone("IST"));
        return format.format(date);
    }

    public static String getFormattedTime(String date, String time_format) {


        try {
            SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
            Calendar cal = Calendar.getInstance(); // creates calendar

            cal.setTime(format.parse(date)); // sets calendar time/date


            return new SimpleDateFormat(time_format).format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String getTimeAgo(String time) {

        long diff = getTimeDiff(getGmtTime(), time);


        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public static long minToMilliSec(int minitus) {


        // assume server send in minutes format
        return minitus * 60 * 1000;

    }


    public static long millSecToHours(long milliSec) {


        // assume server send in minutes format
        return milliSec / (60 * 60 * 1000) % 24;

    }


    public static long millSecToMins(long milliSec) {


        // assume server send in minutes format
        return milliSec / (60 * 1000);

    }


    public static String addHoursToDate(String date, int hours) {


        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);

        try {

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(format.parse(date)); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, hours); // adds one hour
            cal.getTime();


            return format.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "0";


    }


    public static String addMinuteToDate(String date, int minute) {


        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);

        try {

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(format.parse(date)); // sets calendar time/date
            cal.add(Calendar.MINUTE, minute);
            cal.getTime();


            return format.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "0";


    }


    public static long ageCalculator(String date_of_birth) {
        long difference = 0;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        String current_date = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        try {
            Date date1 = format.parse(current_date);

            Date date2 = format.parse(date_of_birth);

            difference = date1.getTime() - date2.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // //AppLog.Log("age_getting", "age_in_years" + ((((difference / 1000) / 3600) / 24) / 365));
        return ((((difference / 1000) / 3600) / 24) / 365);
    }


}
