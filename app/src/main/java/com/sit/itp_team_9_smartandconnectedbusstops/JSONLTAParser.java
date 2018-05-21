package com.sit.itp_team_9_smartandconnectedbusstops;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONLTAParser extends AsyncTask<String, String, String> {

    private static final String TAG = JSONLTAParser.class.getSimpleName();
    HttpURLConnection urlConnection;
    String authKey = "jtDYtND+ToK4dtaUBnPeDg==";
    @Override
    protected String doInBackground(String... args) {

        String result = new String();

        try {
            URL url = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusStops");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("AccountKey",authKey);
            urlConnection.connect();
            Log.d(TAG,"Sent : " +authKey);

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
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                Gson gson = new Gson();
                LTABusStopData response = gson.fromJson(buffer.toString(), LTABusStopData.class);

                for (LTABusStopResult entry : response.getResults()) {
                    //Toast.makeText(this, entry.busStopID, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "ID: "+entry.getBusStopCode() + " Desc: " + entry.getDescription() + " Rd name: " + entry.getRoadName());
                }

            }
            else {
                Log.e(TAG, "doInBackground: ERROR! "+urlConnection.getResponseCode() );
                InputStream in = urlConnection.getErrorStream();
                for (int i = 0; i < in.available(); i++) {
                    //System.out.println("" + in.read());
                    Log.e(TAG, "doInBackground: "+in.read());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
            return result;
        }

    }

    @Override
    protected void onPostExecute(String result) {

        //Do something with the JSON string

    }

    private String inputStreamToString(InputStream is) {
        String rLine;
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }
}
