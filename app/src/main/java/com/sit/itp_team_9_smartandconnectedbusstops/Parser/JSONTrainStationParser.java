package com.sit.itp_team_9_smartandconnectedbusstops.Parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONGoogleResponseRoute;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesSteps;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.TrainStation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONTrainStationParser extends AsyncTask<Void, String, List<TrainStation>> {

    private static final String TAG = JSONTrainStationParser.class.getSimpleName();
    private List<String> urls;
    private List<TrainStation> trainStationList = new ArrayList<>();
    public JSONGoogleResponseRoute delegate = null;

    public JSONTrainStationParser(List<String> urls){
        this.urls = urls;
        //this.activity = activity;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    /**
     * Uses query to get JSON data from data.gov.sg and sets in TrainStation
     *
     * @param voids
     * @return trainStationList List<TrainStation>
     */
    @Override
    protected List<TrainStation> doInBackground(Void... voids) {
        GoogleRoutesData response;
        try {
            for(String url : getUrls()) {
                URL link = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                //String authKey = "AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM";
                //Log.i(TAG, "Sent : " + authKey);
                Log.i(TAG,url);
                // Get the response code
                int statusCode = urlConnection.getResponseCode();
                if (statusCode >= 200 && statusCode < 400) {
                    // Create an InputStream in order to extract the response object
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuffer buffer = new StringBuffer();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    JSONObject response1 = new JSONObject(buffer.toString());
                    JSONObject result = response1.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("records");
                    Log.i(TAG, "jsonArray");
                    for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            TrainStation entry = new TrainStation();
                            entry.setStationName(obj.getString("mrt_station_english"));
                            entry.setStationNameChinese(obj.getString("mrt_station_chinese"));
                            entry.setStationCode(obj.getString("stn_code"));
                            entry.setLineName(obj.getString("mrt_line_english"));
                            entry.setLineNameChinese(obj.getString("mrt_line_chinese"));
                            entry.setStationNum(Integer.parseInt(entry.getStationCode().replaceAll("[^0-9]","")));
                            trainStationList.add(entry);
                        }
                    urlConnection.disconnect();
                } else {
                    Log.e(TAG, "doInBackground: ERROR " + urlConnection.getResponseCode() + " when pulling Google maps data");
                    InputStream in = urlConnection.getErrorStream();
                    for (int i = 0; i < in.available(); i++) {
                        System.out.println("" + in.read());
//                        Log.e(TAG, "doInBackground: " + in.read());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            return trainStationList;
        }
    }

    @Override
    protected void onPostExecute(List<TrainStation> result) {
        //Do something with the JSON string

        Log.i(TAG, "onPostExecute: Total of "+result.size()+ " train stations found");
        Log.i(TAG,"routes: " + result + "\n");
        //delegate.processFinishFromGoogle(result);
    }
}
