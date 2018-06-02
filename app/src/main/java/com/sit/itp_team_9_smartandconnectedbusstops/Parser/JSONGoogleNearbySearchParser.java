package com.sit.itp_team_9_smartandconnectedbusstops.Parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleBusStopData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONGoogleNearbySearchParser extends AsyncTask<Void, String, List<GoogleBusStopData>> {

    private static final String TAG = JSONGoogleNearbySearchParser.class.getSimpleName();
    private Activity activity;
    private List<String> urls;
    private List<GoogleBusStopData> responseList = new ArrayList<>();


    public JSONGoogleNearbySearchParser(Activity activity, List<String> urls){
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
    protected List<GoogleBusStopData> doInBackground(Void... voids) {
        GoogleBusStopData response;
        try {
            for(String url : getUrls()) {
                URL link = new URL(url);
                /*
                https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=1.3736638,103.9554456&rankby=distance&type=transit_station&key=AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s
                 */
                HttpURLConnection urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                String authKey = "AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s";
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
                    JSONArray jsonArray = response1.getJSONArray("results");

                    for(int i=0; i< jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        GoogleBusStopData entry = new GoogleBusStopData();
                        entry.setName(obj.getString("name"));
                        entry.setPlaceId(obj.getString("place_id"));
                        entry.setIcon(obj.getString("icon"));
                        entry.setLat(obj.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                        entry.setLng(obj.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                        //Log.d(TAG, "doInBackground: "+entry.getName());
                        boolean toAdd = true;
                        for(int j=0; j<responseList.size();j++){
                            if(responseList.get(j).getName().equals(entry.getName()))
                                toAdd=false;
                        }
                        if(toAdd)
                            responseList.add(entry);
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

            return responseList;
        }
    }

    @Override
    protected void onPostExecute(List<GoogleBusStopData> result) {
        //Do something with the JSON string

        Log.d(TAG, "onPostExecute: Total of "+result.size()+ " data points has been added");
    }
}
