package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sit.itp_team_9_smartandconnectedbusstops.SGPM25.JSONPM25Parser;
import com.sit.itp_team_9_smartandconnectedbusstops.SGPSI.Item;
import com.sit.itp_team_9_smartandconnectedbusstops.SGPSI.JSONPSIParser;
import com.sit.itp_team_9_smartandconnectedbusstops.SGPSI.RegionMetadatum;
import com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature.Items;
import com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature.JSONTemperatureParser;
import com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature.Metadata;
import com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature.Readings;
import com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature.Stations;
import com.sit.itp_team_9_smartandconnectedbusstops.SGUV.JSONUVParser;
import com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.Area_metadata;
import com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.Forecasts;
import com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.JSONWeatherParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SGWeather {
    private static final String TAG = SGWeather.class.getSimpleName();
    private HttpURLConnection urlConnection;
    private String mTemperature;
    private String mPM25;
    private String mPM10;
    private String mUV;
    private String mWeatherForecast;
    private LatLng latLng;

    private String mTempForLatLong;
    private String mWeatherForLatLong;

    private JSONTemperatureParser temperatureParser;
    private JSONWeatherParser weatherParser;
    private JSONPSIParser psiParser;
    private JSONUVParser uvParser;
    private JSONPM25Parser pm25Parser;

    public SGWeather() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                getTemperature();
                getForecast();
                getDataForPM25();
                getDataForPSI();
                getDataForUV();
//                updateByLocation(getLatLng());
                return null;
            }
        };
        asyncTask.execute();
    }

    public void updateLatLng(LatLng newLatLng){
        setLatLng(newLatLng);
        updateByLocation();
    }

    private void updateByLocation(){
        updateTemperature();
        updateForecast();
        updatePSI();
        updateUV();
        updatePM25();
    }

    public void updateForSpecificLocation(LatLng latLng){
        getForecastForLatLong(latLng);
        getTemperatureForLatLong(latLng);
    }

    private void updateTemperature(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(temperatureParser == null) {
                    Log.e(TAG, "updateTemperature: No Metadata?");
                    return null;
                }

                Metadata meta = temperatureParser.getMetadata();
                Stations[] station = meta.getStations();

                Comparator comp = (Comparator<Stations>) (o, o2) -> {
                    float[] result1 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o.getLocation().getLatitude()), Double.parseDouble(o.getLocation().getLongitude()), result1);
                    Float distance1 = result1[0];

                    float[] result2 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o2.getLocation().getLatitude()), Double.parseDouble(o2.getLocation().getLongitude()), result2);
                    Float distance2 = result2[0];
                    return distance1.compareTo(distance2);
                };

                long start = System.currentTimeMillis();
                Log.d(TAG, "updateTemperature: BEGIN SORTING!");
                Collections.sort(Arrays.asList(station), comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "updateTemperature: COMPLETED SORTING! "+elapsedTime+"ms");

                Items[] temperatureItems = temperatureParser.getItems();
                Readings[] readings = temperatureItems[0].getReadings();
                for(Readings entry : readings){
                    if(entry.getStation_id().equals(station[0].getId())) {
                        setmTemperature(entry.getValue());
                        Log.d(TAG, "updateTemperature: Nearest "+entry.getStation_id()+" "+station[0].getName()+" "+ getmTemperature());
                        break;
                    }
                }
                return null;
            }
        };
        task.execute();
    }

    private void getTemperatureForLatLong(LatLng latLng){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(temperatureParser == null) {
                    Log.e(TAG, "getTemperatureForLatLong: No Metadata?");
                    return null;
                }

                Metadata meta = temperatureParser.getMetadata();
                Stations[] station = meta.getStations();

                Comparator comp = (Comparator<Stations>) (o, o2) -> {
                    float[] result1 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o.getLocation().getLatitude()), Double.parseDouble(o.getLocation().getLongitude()), result1);
                    Float distance1 = result1[0];

                    float[] result2 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o2.getLocation().getLatitude()), Double.parseDouble(o2.getLocation().getLongitude()), result2);
                    Float distance2 = result2[0];
                    return distance1.compareTo(distance2);
                };

                long start = System.currentTimeMillis();
                Log.d(TAG, "getTemperatureForLatLong: BEGIN SORTING!");
                Collections.sort(Arrays.asList(station), comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "getTemperatureForLatLong: COMPLETED SORTING! "+elapsedTime+"ms");

                Items[] temperatureItems = temperatureParser.getItems();
                Readings[] readings = temperatureItems[0].getReadings();
                for(Readings entry : readings){
                    if(entry.getStation_id().equals(station[0].getId())) {
                        setmTempForLatLong(entry.getValue());
                        Log.d(TAG, "getTemperatureForLatLong: Nearest "+entry.getStation_id()+" "+station[0].getName()+" "+ getmTemperature());
                        break;
                    }
                }
                return null;
            }
        };
        task.execute();
    }

    private void updateForecast(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(weatherParser == null) {
                    Log.e(TAG, "updateForecast: No Metadata?");
                    return null;
                }

                Area_metadata[] meta = weatherParser.getArea_metadata();

                Comparator comp = (Comparator<Area_metadata>) (o, o2) -> {
                    float[] result1 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o.getLabel_location().getLatitude()), Double.parseDouble(o.getLabel_location().getLongitude()), result1);
                    Float distance1 = result1[0];

                    float[] result2 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o2.getLabel_location().getLatitude()), Double.parseDouble(o2.getLabel_location().getLongitude()), result2);
                    Float distance2 = result2[0];
                    return distance1.compareTo(distance2);
                };

                long start = System.currentTimeMillis();
                Log.d(TAG, "updateForecast: BEGIN SORTING!");
                Collections.sort(Arrays.asList(meta), comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "updateForecast: COMPLETED SORTING! "+elapsedTime+"ms");

                com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.Items[] readings = weatherParser.getItems();
                Forecasts[] forecasts = readings[0].getForecasts();
                for(Forecasts entry : forecasts){
                    if(entry.getArea().equals(meta[0].getName())) {
                        setmWeatherForecast(entry.getForecast());
                        Log.d(TAG, "updateForecast: Nearest "+entry.getArea()+" "+meta[0].getName()+" "+ getmWeatherForecast());
                        break;
                    }
                }
                return null;
            }
        };
        task.execute();
    }

    private void getForecastForLatLong(LatLng latLng){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(weatherParser == null) {
                    Log.e(TAG, "getForecastForLatLong: No Metadata?");
                    return null;
                }

                Area_metadata[] meta = weatherParser.getArea_metadata();

                Comparator comp = (Comparator<Area_metadata>) (o, o2) -> {
                    float[] result1 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o.getLabel_location().getLatitude()), Double.parseDouble(o.getLabel_location().getLongitude()), result1);
                    Float distance1 = result1[0];

                    float[] result2 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, Double.parseDouble(o2.getLabel_location().getLatitude()), Double.parseDouble(o2.getLabel_location().getLongitude()), result2);
                    Float distance2 = result2[0];
                    return distance1.compareTo(distance2);
                };

                long start = System.currentTimeMillis();
                Log.d(TAG, "getForecastForLatLong: BEGIN SORTING!");
                Collections.sort(Arrays.asList(meta), comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "getForecastForLatLong: COMPLETED SORTING! "+elapsedTime+"ms");

                com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.Items[] readings = weatherParser.getItems();
                Forecasts[] forecasts = readings[0].getForecasts();
                for(Forecasts entry : forecasts){
                    if(entry.getArea().equals(meta[0].getName())) {
                        setmWeatherForLatLong(entry.getForecast());
                        Log.d(TAG, "getForecastForLatLong: Nearest "+entry.getArea()+" "+meta[0].getName()+" "+ getmWeatherForecast());
                        break;
                    }
                }
                return null;
            }
        };
        task.execute();
    }

    private void updatePSI(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(psiParser == null) {
                    Log.e(TAG, "updatePSI: No Metadata?");
                    return null;
                }

                List<RegionMetadatum> meta = psiParser.getRegionMetadata();

                Comparator comp = (Comparator<RegionMetadatum>) (o, o2) -> {
                    float[] result1 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, o.getLabelLocation().getLatitude(), o.getLabelLocation().getLongitude(), result1);
                    Float distance1 = result1[0];

                    float[] result2 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, o2.getLabelLocation().getLatitude(), o2.getLabelLocation().getLongitude(), result2);
                    Float distance2 = result2[0];
                    return distance1.compareTo(distance2);
                };

                long start = System.currentTimeMillis();
                Log.d(TAG, "updatePSI: BEGIN SORTING!");
                Collections.sort(meta, comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "updatePSI: COMPLETED SORTING! "+elapsedTime+"ms");

                List<Item> psiParserItems = psiParser.getItems();
                Item readings = psiParserItems.get(0);
                com.sit.itp_team_9_smartandconnectedbusstops.SGPSI.Readings data = readings.getReadings();
                String nearestZone = meta.get(0).getName();
                switch (nearestZone){
                    case "west":
                        setmPM10(data.getPm10SubIndex().getWest().toString());
                        break;
                    case "east":
                        setmPM10(data.getPm10SubIndex().getEast().toString());
                        break;
                    case "south":
                        setmPM10(data.getPm10SubIndex().getSouth().toString());
                        break;
                    case "north":
                        setmPM10(data.getPm10SubIndex().getNorth().toString());
                        break;
                    case "central":
                        setmPM10(data.getPm10SubIndex().getCentral().toString());
                        break;
                    default:
                        setmPM10(data.getPm10SubIndex().getNational().toString());
                        break;
                }
                Log.d(TAG, "updatePSI: Nearest "+meta.get(0).getName()+" PM 10: "+ getmPM10()+" PM 2.5:"+getmPM25());

                return null;
            }
        };
        task.execute();
    }

    private void updateUV(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(uvParser == null) {
                    Log.e(TAG, "updateUV: No data?");
                    return null;
                }
                List<com.sit.itp_team_9_smartandconnectedbusstops.SGUV.Item> item = uvParser.getItems();
                setmUV(item.get(0).getIndex().get(0).getValue().toString());
                Log.d(TAG, "updateUV: UV="+getmUV());

                return null;
            }
        };
        task.execute();
    }

    private void updatePM25(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(pm25Parser == null) {
                    Log.e(TAG, "updatePM25: No data?");
                    return null;
                }

                List<com.sit.itp_team_9_smartandconnectedbusstops.SGPM25.RegionMetadatum> meta = pm25Parser.getRegionMetadata();

                Comparator comp = (Comparator<com.sit.itp_team_9_smartandconnectedbusstops.SGPM25.RegionMetadatum>) (o, o2) -> {
                    float[] result1 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, o.getLabelLocation().getLatitude(), o.getLabelLocation().getLongitude(), result1);
                    Float distance1 = result1[0];

                    float[] result2 = new float[3];
                    Location.distanceBetween(latLng.latitude, latLng.longitude, o2.getLabelLocation().getLatitude(), o2.getLabelLocation().getLongitude(), result2);
                    Float distance2 = result2[0];
                    return distance1.compareTo(distance2);
                };

                long start = System.currentTimeMillis();
                Log.d(TAG, "updatePM25: BEGIN SORTING!");
                Collections.sort(meta, comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "updatePM25: COMPLETED SORTING! "+elapsedTime+"ms");

                List<com.sit.itp_team_9_smartandconnectedbusstops.SGPM25.Item> psiParserItems = pm25Parser.getItems();
                com.sit.itp_team_9_smartandconnectedbusstops.SGPM25.Item readings = psiParserItems.get(0);
                com.sit.itp_team_9_smartandconnectedbusstops.SGPM25.Readings data = readings.getReadings();
                String nearestZone = meta.get(0).getName();
                switch (nearestZone){
                    case "west":
                        setmPM25(data.getPm25OneHourly().getWest().toString());
                        break;
                    case "east":
                        setmPM25(data.getPm25OneHourly().getEast().toString());
                        break;
                    case "south":
                        setmPM25(data.getPm25OneHourly().getSouth().toString());
                        break;
                    case "north":
                        setmPM25(data.getPm25OneHourly().getNorth().toString());
                        break;
                    case "central":
                        setmPM25(data.getPm25OneHourly().getCentral().toString());
                        break;
                    default:
                        setmPM25(data.getPm25OneHourly().getCentral().toString());
                        break;
                }
                Log.d(TAG, "updatePM25: Nearest "+meta.get(0).getName()+" PM 2.5:"+getmPM25());

                return null;
            }
        };
        task.execute();
    }
    private void getTemperature(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL link = new URL("https://api.data.gov.sg/v1/environment/air-temperature");
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
                        temperatureParser = gson.fromJson( buffer.toString(), JSONTemperatureParser.class );

//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getItems().toString());
//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getMetadata());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG, "getTemperature: Completed");
            }
        };
        asyncTask.execute();
    }

    private void getDataForPM25(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL link = new URL("https://api.data.gov.sg/v1/environment/pm25");
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
                        pm25Parser = gson.fromJson( buffer.toString(), JSONPM25Parser.class );

//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getItems().toString());
//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getMetadata());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG, "getDataForPM25: Completed");
            }
        };
        asyncTask.execute();
    }

    private void getDataForPSI(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL link = new URL("https://api.data.gov.sg/v1/environment/psi");
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
                        psiParser = gson.fromJson( buffer.toString(), JSONPSIParser.class );
//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getItems().toString());
//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getMetadata());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG, "getDataForPSI: Completed");
            }
        };
        asyncTask.execute();
    }

    private void getDataForUV(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL link = new URL("https://api.data.gov.sg/v1/environment/uv-index");
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
                        uvParser = gson.fromJson( buffer.toString(), JSONUVParser.class );

//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getItems().toString());
//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getMetadata());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG, "getDataForUV: Completed");
            }
        };
        asyncTask.execute();
    }

    private void getForecast(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL link = new URL("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast");
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
                        weatherParser = gson.fromJson( buffer.toString(), JSONWeatherParser.class );

//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getItems().toString());
//                        Log.d(TAG, "getTemperature: "+ temperatureParser.getMetadata());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i(TAG, "getForecast: Completed");
            }
        };
        asyncTask.execute();
    }

    public String getmTemperature() {
        return mTemperature;
    }

    private void setmTemperature(String mTemperature) {
        this.mTemperature = mTemperature;
    }

    public String getmPM25() {
        return mPM25;
    }

    private void setmPM25(String mPM25) {
        this.mPM25 = mPM25;
    }

    public String getmPM10() {
        return mPM10;
    }

    private void setmPM10(String mPM10) {
        this.mPM10 = mPM10;
    }

    public String getmUV() {
        return mUV;
    }

    private void setmUV(String mUV) {
        this.mUV = mUV;
    }

    public String getmWeatherForecast() {
        return mWeatherForecast;
    }

    private void setmWeatherForecast(String mWeatherForecast) {
        this.mWeatherForecast = mWeatherForecast;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    private void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getmTempForLatLong() {
        return mTempForLatLong;
    }

    public String getmWeatherForLatLong() {
        return mWeatherForLatLong;
    }

    public void setmTempForLatLong(String mTempForLatLong) {
        this.mTempForLatLong = mTempForLatLong;
    }

    public void setmWeatherForLatLong(String mWeatherForLatLong) {
        this.mWeatherForLatLong = mWeatherForLatLong;
    }
}
