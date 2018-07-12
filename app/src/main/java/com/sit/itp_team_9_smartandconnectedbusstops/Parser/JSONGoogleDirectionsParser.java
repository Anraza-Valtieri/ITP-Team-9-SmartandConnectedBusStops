package com.sit.itp_team_9_smartandconnectedbusstops.Parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONGoogleResponseRoute;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesSteps;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONGoogleDirectionsParser extends AsyncTask<Void, String, List<GoogleRoutesData>> {

    private static final String TAG = JSONGoogleDirectionsParser.class.getSimpleName();
    //private Activity activity;
    private List<String> urls;
    private List<GoogleRoutesData> routesList = new ArrayList<>();
    public JSONGoogleResponseRoute delegate = null;


    public JSONGoogleDirectionsParser(Activity activity, List<String> urls){
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
     * Uses query to get JSON data from Google and sets in GoogleRoutesData
     *
     * @param voids
     * @return routesList List<GoogleRoutesData>
     */
    @Override
    protected List<GoogleRoutesData> doInBackground(Void... voids) {
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
                    JSONArray jsonArray = response1.getJSONArray("routes");
                    Log.i(TAG, "jsonArray");
                    if(response1.getString("status").equals("OK")) {
                        //if response is normal
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            GoogleRoutesData entry = new GoogleRoutesData();
                            entry.setID(i);
                            entry.setCopyrights(obj.getString("copyrights"));
                            entry.setWarnings(obj.getJSONArray("warnings"));
                            entry.setSummary(obj.getString("summary"));
                            JSONArray legsArray = obj.getJSONArray("legs");
                            for (int j = 0; j < legsArray.length(); j++) {
                                JSONObject legsObject = legsArray.getJSONObject(j);
                                entry.setTotalDistance(legsObject.getJSONObject("distance").getString("text"));
                                entry.setTotalDuration(legsObject.getJSONObject("duration").getString("text"));
                                JSONArray stepsArray = legsObject.getJSONArray("steps");
                                List<GoogleRoutesSteps> stepsList = new ArrayList<>(); //to store steps
                                for (int k = 0; k < stepsArray.length(); k++) {
                                    //steps stored using GoogleRoutesSteps
                                    GoogleRoutesSteps steps = new GoogleRoutesSteps();
                                    JSONObject stepsObject = stepsArray.getJSONObject(k);
                                    steps.setEndLocationLat(stepsObject.getJSONObject("end_location").getDouble("lat"));
                                    steps.setEndLocationLng(stepsObject.getJSONObject("end_location").getDouble("lng"));
                                    steps.setStartLocationLat(stepsObject.getJSONObject("start_location").getDouble("lat"));
                                    steps.setStartLocationLng(stepsObject.getJSONObject("start_location").getDouble("lng"));
                                    steps.setDistance(stepsObject.getJSONObject("distance").getString("text"));
                                    steps.setDuration(stepsObject.getJSONObject("duration").getString("text"));
                                    steps.setHtmlInstructions(stepsObject.getString("html_instructions"));
                                    steps.setPolyline(stepsObject.getJSONObject("polyline").getString("points"));
                                    Log.d(TAG,"polyline"+ steps.getPolyline());
                                    steps.setTravelMode(stepsObject.getString("travel_mode"));

                                    //detailed, step-by-step instructions
                                    if (steps.getTravelMode().matches("TRANSIT")) {
                                        steps.setNumStops(stepsObject.getJSONObject("transit_details")
                                                .getInt("num_stops"));
                                        Log.i(TAG, "NUMSTOPS: " + steps.getNumStops().toString());
                                        String vehicle = stepsObject.getJSONObject("transit_details")
                                                .getJSONObject("line").getJSONObject("vehicle").getString("type");
                                        if (vehicle.equals("SUBWAY") || vehicle.equals("TRAM")) {
                                            //subway = mrt, tram = lrt
                                            steps.setTrainLine(stepsObject.getJSONObject("transit_details")
                                                    .getJSONObject("line").optString("name"));
                                            steps.setDepartureStop(stepsObject.getJSONObject("transit_details")
                                                    .getJSONObject("departure_stop").optString("name"));
                                            steps.setArrivalStop(stepsObject.getJSONObject("transit_details")
                                                    .getJSONObject("arrival_stop").optString("name"));
                                        } else if (vehicle.equals("BUS")) {
                                            steps.setBusNum(stepsObject.getJSONObject("transit_details")
                                                    .getJSONObject("line").optString("short_name"));
                                            steps.setDepartureStop(stepsObject.getJSONObject("transit_details")
                                                    .getJSONObject("departure_stop").optString("name"));
                                            steps.setArrivalStop(stepsObject.getJSONObject("transit_details")
                                                    .getJSONObject("arrival_stop").optString("name"));
                                            //Double newBusDistance = entry.getTotalBusDistance() + Double.valueOf(steps.getDistance());
                                            //entry.setTotalBusDistance(newBusDistance);
                                        }
                                    } else if (steps.getTravelMode().matches("WALKING")) {
                                        //JSONArray detailedStepsArray = stepsObject.getJSONArray("steps");
                                        //List<GoogleRoutesSteps> detailedStepsList = new ArrayList<>();
                                        //to store walking steps
                                        /*for (int l = 0; l < detailedStepsArray.length(); l++) {
                                            JSONObject detailedStepsObject = detailedStepsArray.getJSONObject(l);
                                            GoogleRoutesSteps detailedSteps = new GoogleRoutesSteps();
                                            detailedSteps.setDistance(detailedStepsObject
                                                    .getJSONObject("distance").getString("text"));
                                            detailedSteps.setDuration(detailedStepsObject
                                                    .getJSONObject("duration").getString("text"));
                                            detailedSteps.setHtmlInstructions(detailedStepsObject
                                                    .optString("html_instructions"));
                                            detailedStepsList.add(detailedSteps);
                                        }
                                        steps.setDetailedSteps(detailedStepsList);*/
                                        //steps.setHtmlInstructions(stepsObject.getString("html_instructions"));
                                        steps.setDuration(stepsObject.getJSONObject("duration").getString("text"));
                                        steps.setDistance(stepsObject.getJSONObject("distance").getString("text"));
                                    }
                                    Log.i(TAG, "STEP: " + steps.toString());
                                    stepsList.add(steps);
                                }
                                entry.setSteps(stepsList);
                            }

                            //entry.setLegs(obj.getJSONArray("legs"));
                            //entry.setLat(obj.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                            //entry.setLng(obj.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                            //Log.d(TAG, "doInBackground: "+entry.getName());
                        /*boolean toAdd = true;
                        for(int a=0; a<responseList.size();a++){
                            if(responseList.get(a).getName().equals(entry.getName()))
                                toAdd=false;
                        }
                        if(toAdd)*/
                            routesList.add(entry);
                        }
                    }else{
                        //if there is an error in Google response
                        GoogleRoutesData entry = new GoogleRoutesData();
                        entry.setError(response1.getString("status"));
                        routesList.add(entry);
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

            return routesList;
        }
    }

    @Override
    protected void onPostExecute(List<GoogleRoutesData> result) {
        //Do something with the JSON string

        Log.i(TAG, "onPostExecute: Total of "+result.size()+ " routes added");
        Log.i(TAG,"routes: " + result + "\n");
        //delegate.processFinishFromGoogle(result);
    }
}
