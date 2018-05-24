package com.sit.itp_team_9_smartandconnectedbusstops.Utils;

import android.os.AsyncTask;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class Utils {
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static Date formatTime(String datetime){
        Date result = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        try {
            Date newdate = format.parse(datetime);
            result = newdate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String dateCheck(final Date date){
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Date currentDate = Calendar.getInstance().getTime();
                long apptTime = date.getTime();
                long currentTime = currentDate.getTime();

                CharSequence relativetime = DateUtils.getRelativeTimeSpanString(apptTime,
                        currentTime, DateUtils.SECOND_IN_MILLIS,  DateUtils.FORMAT_ABBREV_RELATIVE);
                return relativetime.toString();
            }
        };
        String result = "";
        try {
            result = ((String)task.execute(date).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date formatCardTime(String datetime){
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault());
        try {
            cal.setTime(sdf.parse(datetime).getTime());// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}
