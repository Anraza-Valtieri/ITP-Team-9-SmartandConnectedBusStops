package com.sit.itp_team_9_smartandconnectedbusstops.Parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONLTABusParser extends AsyncTask<Void, String, Map<String, LTABusStopData>> {

    private static final String TAG = JSONLTABusStopParser.class.getSimpleName();
    HttpURLConnection urlConnection;
    String authKey = "jtDYtND+ToK4dtaUBnPeDg==";
    private Activity activity;
    private List<String> urls;
    Map<String, LTABusStopData> finalResponse = new HashMap<>();
    public MainActivity delegate = null;

    public JSONLTABusParser(Activity activity, List<String> urls){
        this.urls = urls;
        this.activity = activity;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    protected Map<String, LTABusStopData> doInBackground(Void... voids) {
        try {
            for(String url : getUrls()) {
                URL link = new URL(url);
                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("AccountKey", authKey);
                urlConnection.connect();
                Log.d(TAG, "Sent : " + authKey);

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
                    JSONObject response1 = new JSONObject(buffer.toString());
                    JSONArray jsonArray = response1.getJSONArray("value");
                    for(int i=0; i< jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        LTABusStopData entry = new LTABusStopData(
                                obj.getString("BusStopCode"),
                                obj.getString("RoadName"),
                                obj.getString("Description"));
                        entry.setBusStopLat(obj.getString("Latitude"));
                        entry.setBusStopLong(obj.getString("Longitude"));
                        finalResponse.put(entry.getDescription(), entry);
                    }
                    urlConnection.disconnect();
                } else {
                    Log.e(TAG, "doInBackground: ERROR " + urlConnection.getResponseCode() + " when pulling LTA data");
                    InputStream in = urlConnection.getErrorStream();
                    for (int i = 0; i < in.available(); i++) {
                        //System.out.println("" + in.read());
//                        Log.e(TAG, "doInBackground: " + in.read());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return finalResponse;
        }
    }

    @Override
    protected void onPostExecute(Map<String, LTABusStopData> result) {
        //Do something with the JSON string

        Log.d(TAG, "onPostExecute: Total of "+result.size()+ " data points has been added");
    }

}
