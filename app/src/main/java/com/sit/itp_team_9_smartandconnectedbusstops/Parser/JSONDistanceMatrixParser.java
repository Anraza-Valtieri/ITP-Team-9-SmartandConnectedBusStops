package com.sit.itp_team_9_smartandconnectedbusstops.Parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.DistanceData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONDistanceMatrixParser extends AsyncTask<Void, String, List<DistanceData>> {
    private static final String TAG = JSONDistanceMatrixParser.class.getSimpleName();
    HttpURLConnection urlConnection;
    String authKey = "jtDYtND+ToK4dtaUBnPeDg==";
    private Activity activity;
    private List<String> urls;
    private List<DistanceData> distanceDataList = new ArrayList<>();
    public MainActivity delegate = null;

    public JSONDistanceMatrixParser(Activity mainactivity, List<String> url){
        urls = url;
        activity = mainactivity;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    protected List<DistanceData> doInBackground(Void... voids) {
        if(getUrls().size() < 1)
            return null;

        try {
            for(String url : getUrls()) {
                URL link = new URL(url);
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
                    JSONArray jsonArrayRows = response1.getJSONArray("rows");

                    DistanceData durationDetails = new DistanceData();
                    if(response1.getString("status").equals("OK")) {
                        Log.e(TAG, "STATUS OK!");
                        for (int i = 0; i < jsonArrayRows.length(); i++) {
                            JSONObject obj = jsonArrayRows.getJSONObject(i);


                            JSONArray jsonArrayEle = obj.getJSONArray("elements");
                            for (int j = 0; j < jsonArrayEle.length(); j++) {
                                JSONObject details = jsonArrayEle.getJSONObject(j);

                                durationDetails.setStartAdd(response1.getJSONArray("origin_addresses").toString());
                                durationDetails.setEndAdd(response1.getJSONArray("destination_addresses").toString());
                                durationDetails.setDistance(details.getJSONObject("distance").getString("text"));
                                durationDetails.setDuration(details.getJSONObject("duration").getString("text"));
                                durationDetails.setDuration_in_traffic(details.getJSONObject("duration_in_traffic").getString("text"));
                            }
                            distanceDataList.add(durationDetails);
                    }
                }
                    urlConnection.disconnect();
                } else {
                    Log.e(TAG, "doInBackground: ERROR " + urlConnection.getResponseCode() + " when pulling DistanceMatrix data");
                    InputStream in = urlConnection.getErrorStream();
                    for (int i = 0; i < in.available(); i++) {
                        //System.out.println("" + in.read());
//                        Log.e(TAG, "doInBackground: " + in.read());
                    }
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return distanceDataList;
        }
    }
    @Override
    protected void onPostExecute(List<DistanceData> result) {
        //Do something with the JSON string

        Log.i(TAG, "onPostExecute: Total of "+result.size()+ " duration added");
        Log.i(TAG,"distancedata: " + result + "\n");
    }
}
