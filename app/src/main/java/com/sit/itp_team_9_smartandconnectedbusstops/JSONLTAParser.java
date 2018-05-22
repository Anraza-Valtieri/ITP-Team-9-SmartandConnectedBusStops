package com.sit.itp_team_9_smartandconnectedbusstops;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONLTAParser extends AsyncTask<Void, String, List<LTABusStopData>> {

    private static final String TAG = JSONLTAParser.class.getSimpleName();
    HttpURLConnection urlConnection;
    String authKey = "jtDYtND+ToK4dtaUBnPeDg==";
    private Activity activity;
    private List<String> urls;
    List<LTABusStopData> responseList = new ArrayList<>();

    public JSONLTAParser(Activity activity, List<String> urls){
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
    protected List<LTABusStopData> doInBackground(Void... voids) {
        LTABusStopData response;

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
                    Log.d(TAG, "doInBackground: statusCode >= 200 && statusCode < 400");
                    // Create an InputStream in order to extract the response object
                    //is = urlConnection.getInputStream();
                    InputStream in = urlConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuffer buffer = new StringBuffer();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                    }
                    Gson gson = new Gson();
                    response = gson.fromJson(buffer.toString(), LTABusStopData.class);
                    for (LTABusStopData entry : response.getResults()) {
                        //Toast.makeText(this, entry.busStopID, Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "doInBackground ID: " + entry.getBusStopCode() + " Desc: " + entry.getDescription() + " Rd name: " + entry.getRoadName());
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
    protected void onPostExecute(List<LTABusStopData> result) {
        //Do something with the JSON string
        for (LTABusStopData entry : result) {
            //Toast.makeText(this, entry.busStopID, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPostExecute ID: " + entry.getBusStopCode() + " Desc: " + entry.getDescription() + " Rd name: " + entry.getRoadName());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Add a new document with a generated ID
            db.collection("BusStops")
                    .document(entry.getBusStopCode())
                    .set(entry);
        }
        /*
        This stores Bus stop data locally onto device using FireStore.
        They follow LTABusStopData's variables
        The structure is as follows
        Collection : BusStops
        DocumentID : 00481

        busStopCode = 00481
        roadName = Woodlands Rd
        description = BT PANJANG TEMP BUS PK

         */
        Log.d(TAG, "onPostExecute: Total of "+result.size()+ " data points has been added");
    }

}
