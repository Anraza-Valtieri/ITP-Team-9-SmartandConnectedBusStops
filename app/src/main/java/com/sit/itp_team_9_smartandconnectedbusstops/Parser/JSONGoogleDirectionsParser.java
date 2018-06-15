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
    private Activity activity;
    private List<String> urls;
    private List<GoogleRoutesData> routesList = new ArrayList<>();
    public JSONGoogleResponseRoute delegate = null;


    public JSONGoogleDirectionsParser(Activity activity, List<String> urls){
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
    protected List<GoogleRoutesData> doInBackground(Void... voids) {
        GoogleRoutesData response;
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
                String authKey = "AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM";
                Log.i(TAG, "Sent : " + authKey);

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
                    JSONArray jsonArray = response1.getJSONArray("routes");

                    for(int i=0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        GoogleRoutesData entry = new GoogleRoutesData();
                        entry.setCopyrights(obj.getString("copyrights"));
                        entry.setWarnings(obj.getJSONArray("warnings"));
                        JSONArray legsArray = obj.getJSONArray("legs");
                        for(int j=0; j < legsArray.length(); j++) {
                            JSONObject legsObject = legsArray.getJSONObject(j); //TODO not sure if always only 0?
                            entry.setTotalDistance(legsObject.getJSONObject("distance").getString("text"));
                            entry.setTotalDuration(legsObject.getJSONObject("duration").getString("text"));
                            JSONArray stepsArray = legsObject.getJSONArray("steps");
                            List<GoogleRoutesSteps> stepsList = new ArrayList<>(); //to store steps
                            for(int k=0; k < stepsArray.length(); k++) {
                                GoogleRoutesSteps steps = new GoogleRoutesSteps();
                                JSONObject stepsObject = stepsArray.getJSONObject(k);
                                steps.setDistance(stepsObject.getJSONObject("distance").getString("text"));
                                steps.setDuration(stepsObject.getJSONObject("duration").getString("text"));
                                steps.setHtmlInstructions(stepsObject.getString("html_instructions"));
                                steps.setTravelMode(stepsObject.getString("travel_mode"));

                                //step-by-step instructions
                                //TODO CHECK THIS WHOLE SECTION especially optString parts
                                if (steps.getTravelMode().matches("TRANSIT")){
                                    steps.setNumStops(stepsObject.getJSONObject("transit_details")
                                            .getInt("num_stops"));
                                    Log.i(TAG,"NUMSTOPS: "+ steps.getNumStops().toString());

                                    //Train
                                    steps.setTrainLine(stepsObject.getJSONObject("transit_details")
                                            .getJSONObject("line").optString("name"));
                                    steps.setDepartureStop(stepsObject.getJSONObject("transit_details")
                                            .getJSONObject("departure_stop").optString("name"));
                                    steps.setArrivalStop(stepsObject.getJSONObject("transit_details")
                                            .getJSONObject("arrival_stop").optString("name"));

                                    //Bus
                                    steps.setBusNum(stepsObject.getJSONObject("transit_details")
                                            .getJSONObject("line").optString("short_name"));
                                }
                                else if (steps.getTravelMode().matches("WALKING")){
                                    JSONArray detailedStepsArray = stepsObject.getJSONArray("steps");
                                    List<GoogleRoutesSteps> detailedStepsList = new ArrayList<>();
                                    //to store walking steps
                                    for(int l=0; l < detailedStepsArray.length(); l++) {
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
                                    steps.setDetailedSteps(detailedStepsList);
                                }
                                Log.i(TAG,"STEP: "+steps.toString());
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
        delegate.processFinishFromGoogle(result);
    }
}