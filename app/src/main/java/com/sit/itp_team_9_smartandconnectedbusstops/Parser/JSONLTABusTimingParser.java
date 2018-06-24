package com.sit.itp_team_9_smartandconnectedbusstops.Parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.CardAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JSONLTABusTimingParser extends AsyncTask<Void, String, Map<String, Map>> {
    private static final String TAG = JSONLTABusTimingParser.class.getSimpleName();
    private HttpURLConnection urlConnection;
    private String authKey = "jtDYtND+ToK4dtaUBnPeDg==";
    private Activity activity;
    private String busStopNo;
    private List<String> urls;
    private TreeMap<String, List<String>> responseList = new TreeMap<>();
    private TreeMap<String, Map> finalResponse = new TreeMap<>();
    public MainActivity delegate = null;
    public CardAdapter delegate2 = null;


    public JSONLTABusTimingParser(List<String> urls, String busStopNo){
        this.urls = urls;
        this.busStopNo = busStopNo;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getBusStopNo() {
        return busStopNo;
    }

    public void setBusStopNo(String busStopNo) {
        this.busStopNo = busStopNo;
    }

    @Override
    protected Map<String, Map> doInBackground(Void... voids) {
        try {
            for(String url : getUrls()) {
                URL link = new URL(url+getBusStopNo());
                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("AccountKey", authKey);
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
                    JSONObject response1 = new JSONObject(buffer.toString());
                    JSONArray jsonArray = response1.getJSONArray("Services");

                    for(int i=0; i< jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String busNo = (obj.getString("ServiceNo"));
                        List<String> busTiming = new ArrayList<>();
                        busTiming.add(obj.getJSONObject("NextBus").getString("DestinationCode")); // 0
                        busTiming.add(obj.getJSONObject("NextBus").getString("EstimatedArrival")); // 1
                        busTiming.add(obj.getJSONObject("NextBus").getString("Load")); // 2
                        busTiming.add(obj.getJSONObject("NextBus").getString("Feature")); // 3
                        busTiming.add(obj.getJSONObject("NextBus").getString("Type")); // 4
                        busTiming.add(obj.getJSONObject("NextBus2").getString("EstimatedArrival")); // 5
                        busTiming.add(obj.getJSONObject("NextBus2").getString("Load")); // 6
                        busTiming.add(obj.getJSONObject("NextBus2").getString("Feature")); // 7
                        busTiming.add(obj.getJSONObject("NextBus2").getString("Type")); // 8
                        busTiming.add(obj.getJSONObject("NextBus3").getString("EstimatedArrival")); // 9
                        busTiming.add(obj.getJSONObject("NextBus3").getString("Load")); // 10
                        busTiming.add(obj.getJSONObject("NextBus3").getString("Feature")); // 11
                        busTiming.add(obj.getJSONObject("NextBus3").getString("Type")); // 12
                        busTiming.add(obj.getString("Operator")); // 13

//                        Log.d(TAG, "doInBackground: "+busNo+ " - "+ busTiming.toString());
                        responseList.put(busNo, busTiming);
                    }
                    String busStopID = response1.getString("BusStopCode");
//                    Log.d(TAG, "doInBackground: "+busStopID+ " - "+ responseList.toString());
                    finalResponse.put(busStopID, responseList);
                    urlConnection.disconnect();
                } else {
                    Log.e(TAG, "doInBackground: ERROR " + urlConnection.getResponseCode() + " when pulling LTA timing data");
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
    protected void onPostExecute(Map<String, Map> result) {
        //Do something with the JSON string

//        Log.d(TAG, "onPostExecute: Total of "+result.size()+ " data points has been added");
        if(delegate2 != null)
            delegate2.processFinishFromLTA(result);
    }
}
