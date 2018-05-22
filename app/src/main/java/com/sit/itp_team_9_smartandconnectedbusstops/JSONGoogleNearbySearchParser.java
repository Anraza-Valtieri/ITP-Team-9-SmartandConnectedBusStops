package com.sit.itp_team_9_smartandconnectedbusstops;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONGoogleNearbySearchParser extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;

    @Override
    protected String doInBackground(String... args) {

        String result = new String();

        try {
            URL url = new URL("");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = inputStreamToString(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        return result;
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
