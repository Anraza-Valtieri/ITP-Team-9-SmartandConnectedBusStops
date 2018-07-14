package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
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
import java.util.HashMap;
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
    private String mLocation;

    private String mTempForLatLong;
    private String mWeatherForLatLong;

    private JSONTemperatureParser temperatureParser;
    private JSONWeatherParser weatherParser;
    private JSONPSIParser psiParser;
    private JSONUVParser uvParser;
    private JSONPM25Parser pm25Parser;

    private HashMap<String, String> locationHashMap = new HashMap<>();

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
        updateForSpecificLocation(getLatLng());
    }

    public void updateForSpecificLocation(LatLng latLng){
//        Log.d("SGWEATHER -------------", latLng.latitude + ", " + latLng.longitude);
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
                    setmTemperature("Refreshing data..");
                    getTemperature();
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
//                Log.d(TAG, "updateTemperature: BEGIN SORTING!");
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
                    setmTemperature("Refreshing data..");
                    getTemperature();
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
//                Log.d(TAG, "getTemperatureForLatLong: BEGIN SORTING!");
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

        populateLocationHashMap();

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                if(weatherParser == null) {
                    Log.e(TAG, "updateForecast: No Metadata?");
                    setmWeatherForecast("Refreshing data..");
                    getForecast();
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
//                Log.d(TAG, "updateForecast: BEGIN SORTING!");
                Collections.sort(Arrays.asList(meta), comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "updateForecast: COMPLETED SORTING! "+elapsedTime+"ms");

                com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.Items[] readings = weatherParser.getItems();
                Forecasts[] forecasts = readings[0].getForecasts();
                for(Forecasts entry : forecasts){
                    if(entry.getArea().equals(meta[0].getName())) {

                        String dayNightForecast = "";
                        if(entry.getForecast().contains("Day")) {
                            dayNightForecast = entry.getForecast().replace("Day", MainActivity.context.getResources().getString(R.string.day));
                        }
                        else if(entry.getForecast().contains("Night")) {
                            dayNightForecast = entry.getForecast().replace("Night", MainActivity.context.getResources().getString(R.string.night));
                        }

                        String translatedForecast = "";
                        if(dayNightForecast.contains("Fair")) {
                            translatedForecast = dayNightForecast.replace("Fair", MainActivity.context.getResources().getString(R.string.fair));
                        }
                        else if(entry.getForecast().contains("Partly Cloudy")) {
                            translatedForecast = dayNightForecast.replace("Partly Cloudy", MainActivity.context.getResources().getString(R.string.partcloudy));
                        }

                        setmWeatherForecast(translatedForecast);
                        setmLocation(locationHashMap.get(entry.getArea()));
//                        Log.d(TAG, "updateForecast: Nearest "+entry.getArea()+" "+meta[0].getName()+" "+ getmWeatherForecast());
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
                    setmWeatherForLatLong("Refreshing data..");
                    getForecast();
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
//                Log.d(TAG, "getForecastForLatLong: BEGIN SORTING!");
                Collections.sort(Arrays.asList(meta), comp);
                long elapsedTime = System.currentTimeMillis() - start;
                Log.d(TAG, "getForecastForLatLong: COMPLETED SORTING! "+elapsedTime+"ms");

                com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast.Items[] readings = weatherParser.getItems();
                Forecasts[] forecasts = readings[0].getForecasts();
                for(Forecasts entry : forecasts){
                    if(entry.getArea().equals(meta[0].getName())) {
                        setmWeatherForLatLong(entry.getForecast());
//                        Log.d(TAG, "getForecastForLatLong: Nearest "+entry.getArea()+" "+meta[0].getName()+" "+ getmWeatherForecast());
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
                    setmPM10("Refreshing data..");
                    getDataForPSI();
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
//                Log.d(TAG, "updatePSI: BEGIN SORTING!");
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
//                Log.d(TAG, "updatePSI: Nearest "+meta.get(0).getName()+" PM 10: "+ getmPM10()+" PM 2.5:"+getmPM25());

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
                    getDataForUV();
                    return null;
                }
                List<com.sit.itp_team_9_smartandconnectedbusstops.SGUV.Item> item = uvParser.getItems();
                setmUV(item.get(0).getIndex().get(0).getValue().toString());
                Log.d(TAG, "updateUV: UV = "+getmUV());

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
                    Log.e(TAG, "updatePM25: No metadata?");
                    setmPM25("Refreshing data..");
                    getDataForPM25();
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
//                Log.d(TAG, "updatePM25: BEGIN SORTING!");
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
                Log.d(TAG, "updatePM25: Nearest "+meta.get(0).getName()+" PM 2.5 : "+getmPM25());

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

    public void populateLocationHashMap() {
        locationHashMap.put("Ang Mo Kio", MainActivity.context.getResources().getString(R.string.AngMoKio));
        locationHashMap.put("Bedok", MainActivity.context.getResources().getString(R.string.Bedok));
        locationHashMap.put("Bishan", MainActivity.context.getResources().getString(R.string.Bishan));
        locationHashMap.put("Boon Lay", MainActivity.context.getResources().getString(R.string.BoonLay));
        locationHashMap.put("Bukit Batok", MainActivity.context.getResources().getString(R.string.BukitBatok));
        locationHashMap.put("Bukit Merah", MainActivity.context.getResources().getString(R.string.BukitMerah));
        locationHashMap.put("Bukit Panjang", MainActivity.context.getResources().getString(R.string.BukitPanjang));
        locationHashMap.put("Bukit Timah", MainActivity.context.getResources().getString(R.string.BukitTimah));
        locationHashMap.put("Central Water Catchment", MainActivity.context.getResources().getString(R.string.CentralWaterCatchment));
        locationHashMap.put("Changi", MainActivity.context.getResources().getString(R.string.Changi));
        locationHashMap.put("Choa Chu Kang", MainActivity.context.getResources().getString(R.string.ChoaChuKang));
        locationHashMap.put("Clementi", MainActivity.context.getResources().getString(R.string.Clementi));
        locationHashMap.put("City", MainActivity.context.getResources().getString(R.string.City));
        locationHashMap.put("Geylang", MainActivity.context.getResources().getString(R.string.Geylang));
        locationHashMap.put("Hougang", MainActivity.context.getResources().getString(R.string.Hougang));
        locationHashMap.put("Jalan Bahar", MainActivity.context.getResources().getString(R.string.JalanBahar));
        locationHashMap.put("Jurong East", MainActivity.context.getResources().getString(R.string.JurongEast));
        locationHashMap.put("Jurong Island", MainActivity.context.getResources().getString(R.string.JurongIsland));
        locationHashMap.put("Jurong West", MainActivity.context.getResources().getString(R.string.JurongWest));
        locationHashMap.put("Kallang", MainActivity.context.getResources().getString(R.string.Kallang));
        locationHashMap.put("Lim Chu Kang", MainActivity.context.getResources().getString(R.string.LimChuKang));
        locationHashMap.put("Mandai", MainActivity.context.getResources().getString(R.string.Mandai));
        locationHashMap.put("Marine Parade", MainActivity.context.getResources().getString(R.string.MarineParade));
        locationHashMap.put("Pasir Ris", MainActivity.context.getResources().getString(R.string.PasirRis));
        locationHashMap.put("Paya Lebar", MainActivity.context.getResources().getString(R.string.PayaLebar));
        locationHashMap.put("Pioneer", MainActivity.context.getResources().getString(R.string.Pioneer));
        locationHashMap.put("Pulau Tekong", MainActivity.context.getResources().getString(R.string.PulauTekong));
        locationHashMap.put("Pulau Ubin", MainActivity.context.getResources().getString(R.string.PulauUbin));
        locationHashMap.put("Punggol", MainActivity.context.getResources().getString(R.string.Punggol));
        locationHashMap.put("Queenstown", MainActivity.context.getResources().getString(R.string.Queenstown));
        locationHashMap.put("Seletar", MainActivity.context.getResources().getString(R.string.Seletar));
        locationHashMap.put("Sembawang", MainActivity.context.getResources().getString(R.string.Sembawang));
        locationHashMap.put("Sengkang", MainActivity.context.getResources().getString(R.string.Sengkang));
        locationHashMap.put("Sentosa", MainActivity.context.getResources().getString(R.string.Sentosa));
        locationHashMap.put("Serangoon", MainActivity.context.getResources().getString(R.string.Serangoon));
        locationHashMap.put("Southern Islands", MainActivity.context.getResources().getString(R.string.SouthernIslands));
        locationHashMap.put("Sungei Kadut", MainActivity.context.getResources().getString(R.string.SungeiKadut));
        locationHashMap.put("Tampines", MainActivity.context.getResources().getString(R.string.Tampines));
        locationHashMap.put("Tanglin", MainActivity.context.getResources().getString(R.string.Tanglin));
        locationHashMap.put("Tengah", MainActivity.context.getResources().getString(R.string.Tengah));
        locationHashMap.put("Toa Payoh", MainActivity.context.getResources().getString(R.string.ToaPayoh));
        locationHashMap.put("Tuas", MainActivity.context.getResources().getString(R.string.Tuas));
        locationHashMap.put("Western Islands", MainActivity.context.getResources().getString(R.string.WesternIslands));
        locationHashMap.put("Western Water Catchment", MainActivity.context.getResources().getString(R.string.WesternWaterCatchment));
        locationHashMap.put("Woodlands", MainActivity.context.getResources().getString(R.string.Woodlands));
        locationHashMap.put("Yishun", MainActivity.context.getResources().getString(R.string.Yishun));
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

    private void setmTempForLatLong(String mTempForLatLong) {
        this.mTempForLatLong = mTempForLatLong;
    }

    private void setmWeatherForLatLong(String mWeatherForLatLong) {
        this.mWeatherForLatLong = mWeatherForLatLong;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
