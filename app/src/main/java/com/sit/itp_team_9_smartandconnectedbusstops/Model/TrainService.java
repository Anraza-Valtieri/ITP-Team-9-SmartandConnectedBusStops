package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.sit.itp_team_9_smartandconnectedbusstops.TrainServicesAlerts.TrainServicesAlerts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TrainService {
    private static final String TAG = TrainService.class.getSimpleName();
    TrainServicesAlerts trainServicesAlerts;
    String lastUpdate = "";
    private HttpURLConnection urlConnection;

    private final Handler handler = new Handler();
    private final Runnable runnable = () -> {
        updateData();
    };

    public TrainService() {
        updateData();
    }

    public TrainServicesAlerts getTrainServicesAlerts() {
        return trainServicesAlerts;
    }

    public void setTrainServicesAlerts(TrainServicesAlerts trainServicesAlerts) {
        this.trainServicesAlerts = trainServicesAlerts;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    private void updateData(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL link = new URL("http://datamall2.mytransport.sg/ltaodataservice/TrainServiceAlerts");
                    urlConnection = (HttpURLConnection) link.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.connect();
                    //                Log.d(TAG, "Sent : " + authKey);

                    // Get the response code
                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode >= 200 && statusCode < 400) {
                        //Log.d(TAG, "doInBackground: statusCode >= 200 && statusCode < 400");
                        // Create an InputStream in order to extract the response object
                        InputStream in = urlConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        StringBuffer buffer = new StringBuffer();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            buffer.append(line + "\n");
                            //Log.d("Response: ", "> " + line);
                        }
                        Gson gson = new Gson();
                        trainServicesAlerts = gson.fromJson( buffer.toString(), TrainServicesAlerts.class );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                handler.postDelayed(runnable, 60000);
                Log.i(TAG, "TrainService: Completed");
            }
        };
        asyncTask.execute();
    }
}
