package com.sit.itp_team_9_smartandconnectedbusstops.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static ViewGroup.LayoutParams params;

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
    @SuppressLint("SimpleDateFormat")
    public static Date formatTime(String datetime){
        Date result = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= 24) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            try {
                Date newdate = format.parse(datetime);
                result = newdate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));

            try {
                Date newdate = format.parse(datetime);
                result = newdate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

                long relativetime = apptTime - currentTime;
                if(TimeUnit.MILLISECONDS.toHours(relativetime) > 0){
                    String text = String.format(Locale.getDefault(), "%02d "+ MainActivity.context.getResources().getString(R.string.hour)+" %02d "+MainActivity.context.getResources().getString(R.string.minute),
                            TimeUnit.MILLISECONDS.toHours(relativetime),
                            TimeUnit.MILLISECONDS.toMinutes(relativetime));
                    return text;
                }
                if(TimeUnit.MILLISECONDS.toMinutes(relativetime) > 0){
                    String text = String.format(Locale.getDefault(), "%02d "+MainActivity.context.getResources().getString(R.string.minute),
                            TimeUnit.MILLISECONDS.toMinutes(relativetime));
                    return text;
                }

                if(TimeUnit.MILLISECONDS.toSeconds(relativetime) > 20){
                    String text = String.format(Locale.getDefault(), "%02d "+MainActivity.context.getResources().getString(R.string.secs),
                            TimeUnit.MILLISECONDS.toSeconds(relativetime));
                    return text;
                }

                if(TimeUnit.MILLISECONDS.toSeconds(relativetime) < 20){
                    String text = String.format(Locale.getDefault(), ""+MainActivity.context.getResources().getString(R.string.arriving));
                    return text;
                }
//                CharSequence relativetime = DateUtils.getRelativeTimeSpanString(apptTime,
//                        currentTime, DateUtils.SECOND_IN_MILLIS,  DateUtils.FORMAT_ABBREV_RELATIVE);
//                return relativetime.toString();
                return "Arr";
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

    public static String dateCheck2(final Date date){
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
        if(!datetime.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(datetime).getTime());// all done
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return cal;
        }
        return cal;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean haveNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void showNoNetworkDialog(Context context){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("No network detected")
                .setMessage("Internet not available, Please check your internet connectivity and try again")
                .setPositiveButton("Settings", (dialog, id) -> {
                    Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dialogIntent);
                })
                .setNegativeButton(" Cancel ", (dialog, id) -> ((Activity) context).finish())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }

    public static String loadBUSRouteJSONFromAsset(Context context) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String json = null;
                try {
                    InputStream is = context.getAssets().open("bus_routes.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return json;
            }
        };
        try {
            return asyncTask.execute().get().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadRailRouteJSONFromAsset(Context context) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String json = null;
                try {
                    InputStream is = context.getAssets().open("MRT_lines.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return json;
            }
        };
        try {
            return asyncTask.execute().get().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // for navigate cards: allow card to expand to accommodate expandable list
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        params = listView.getLayoutParams();
        ViewGroup.LayoutParams paramsToSet = listView.getLayoutParams();
        paramsToSet.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(paramsToSet);
        listView.requestLayout();
    }

    public static void setListViewToOriginal(ListView listView) {
        // for navigate cards: allow card to go back to original height
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        ViewGroup.LayoutParams paramsToSet = listView.getLayoutParams();
        paramsToSet.height = 55; //TODO change hardcoded height
        listView.setLayoutParams(paramsToSet);
        listView.requestLayout();

    }
}
