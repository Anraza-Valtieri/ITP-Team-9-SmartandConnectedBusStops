package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.JSONLTABusRoute;
import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.Value;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.FareDetails;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class NavigateTransitCard extends Card {

    private static final String TAG = NavigateTransitCard.class.getSimpleName();

    private final static int CCL_COLOR = Color.argb(249,244, 168, 37);
    private final static int NEL_COLOR = Color.argb(255,132, 65, 132);
    private final static int DTL_COLOR = Color.argb(255,1, 87, 155);
    private final static int EWL_COLOR = Color.argb(255,24, 158, 74);
    private final static int NSL_COLOR = Color.argb(255,211, 47, 47);
    private final static int WALKING_COLOR = Color.argb(255,120, 120, 120);
    private final static int LRT_COLOR = Color.argb(255,158, 158, 158);
    private final static int BUS_COLOR = Color.argb(255,0, 0, 0);

    private final static String CIRCLE_LINE = "CCL";
    private final static String DOWNTOWN_LINE = "DTL";
    private final static String NORTH_EAST_LINE = "NEL";
    private final static String EAST_WEST_LINE = "EWL";
    private final static String NORTH_SOUTH_LINE = "NSL";
    private final static String LRT_LINE = "LRT";

    private int ID;
    private String totalTime;
    private String cost;
    private String totalDistance;
    private String condition;
    private List<List<Object>> timeTaken; //time(int),weight(float), colour(int) (breakdown bar based on this)
    private String startingStation;
    private String startingStationTimeTaken;
    private int imageViewStartingStation;
    private int imageViewStartingStationColor;
    private String numStops;
    private List<String> inBetweenStops;
    private boolean isFavorite;
    //private List<String> transitStations;
    private Map<String,List<Object>> transitStations; //arrival stop, List<image resource(int),color(int),
                                                        // lineName(string), arrivalStop (string)>
    private String error;
    //private String transferStation;
    //private String endingStation;

    //private int imageViewTransitStation;
    //private int imageViewTransitStationColor;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getStartingStation() {
        return startingStation;
    }

    public void setStartingStation(String startingStation) {
        this.startingStation = startingStation;
    }

    public List<List<Object>> getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(List<List<Object>> timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getNumStops() {
        return numStops;
    }

    public void setNumStops(String numStops) {
        this.numStops = numStops;
    }

    public List<String> getInBetweenStops() {
        return inBetweenStops;
    }

    public void setInBetweenStops(List<String> inBetweenStops) {
        this.inBetweenStops = inBetweenStops;
    }

    /*public String getTransferStation() {
        return transferStation;
    }

    public void setTransferStation(String transferStation) {
        this.transferStation = transferStation;
    }

    public String getEndingStation() {
        return endingStation;
    }

    public void setEndingStation(String endingStation) {
        this.endingStation = endingStation;
    }*/

    public int getImageViewStartingStation() {
        return imageViewStartingStation;
    }

    public void setImageViewStartingStation(int imageViewStartingStation) {
        this.imageViewStartingStation = imageViewStartingStation;
    }

    public int getImageViewStartingStationColor() {
        return imageViewStartingStationColor;
    }

    public void setImageViewStartingStationColor(int imageViewStartingStationColor) {
        this.imageViewStartingStationColor = imageViewStartingStationColor;
    }

    public String getStartingStationTimeTaken() {
        return startingStationTimeTaken;
    }

    public void setStartingStationTimeTaken(String startingStationTimeTaken) {
        this.startingStationTimeTaken = startingStationTimeTaken;
    }

    /*public List<String> getTransitStations() {
        return transitStations;
    }

    public void setTransitStations(List<String> transitStations) {
        this.transitStations = transitStations;
    }*/

    public Map<String, List<Object>> getTransitStations() {
        return transitStations;
    }

    private void setTransitStations(Map<String, List<Object>> transitStations) {
        this.transitStations = transitStations;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    /**
     * Sets and returns a NavigateTransitCard card
     * <p>
     * This method always returns immediately
     *
     * @param googleRoutesData GoogleRoutesData
     * @return card NavigateTransitCard
     */
    public static NavigateTransitCard getRouteData(GoogleRoutesData googleRoutesData, String fareTypes, String trafCon) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                NavigateTransitCard card = new NavigateTransitCard();
                card.setType(Card.NAVIGATE_TRANSIT_CARD);
                if (googleRoutesData.getError() == null || googleRoutesData.getError().isEmpty()){
                    card.setID(googleRoutesData.getID());

                    String translatedDistance = "";

                    if(googleRoutesData.getTotalDistance().contains("km")) {
                        translatedDistance = googleRoutesData.getTotalDistance().replace("km", MainActivity.context.getResources().getString(R.string.km));
                    }
                    else if(googleRoutesData.getTotalDistance().contains("m")) {
                        translatedDistance = googleRoutesData.getTotalDistance().replace("m", MainActivity.context.getResources().getString(R.string.m));
                    }

                    card.setTotalDistance(translatedDistance);

                    String translatedDuration = "";
                    String hourTranslate = "";

                    if(googleRoutesData.getTotalDuration().contains("0 mins")) {
                        googleRoutesData.setTotalDuration(googleRoutesData.getTotalDuration().replace("0 mins", ""));
                    }

                    if(!(googleRoutesData.getTotalDuration().contains("hour")) && googleRoutesData.getTotalDuration().contains("mins")) {
                        translatedDuration = googleRoutesData.getTotalDuration().replace("mins", MainActivity.context.getResources().getString(R.string.minutes));
                    }
                    else if(googleRoutesData.getTotalDuration().contains("hour") && googleRoutesData.getTotalDuration().contains("mins")) {
                        hourTranslate = googleRoutesData.getTotalDuration().replace("hour", MainActivity.context.getResources().getString(R.string.hour));
                        translatedDuration = hourTranslate.replace("mins", MainActivity.context.getResources().getString(R.string.minutes));
                    }
                    else if(googleRoutesData.getTotalDuration().contains("hours") && googleRoutesData.getTotalDuration().contains("mins")) {
                        hourTranslate = googleRoutesData.getTotalDuration().replace("hours", MainActivity.context.getResources().getString(R.string.hours));
                        translatedDuration = hourTranslate.replace("mins", MainActivity.context.getResources().getString(R.string.minutes));
                    }

                    card.setTotalTime(translatedDuration);
                    card.setCondition(trafCon);

                    //in Steps
                    List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
                    if (routeSteps != null) {
                        Map<String,List<Object>> transitStations = new LinkedHashMap<>();
                        List<List<Object>> timeTakenList = new ArrayList<>();
                        List<TransitModeDistances> listOfTransitModeAndDistances = new ArrayList<>();

                        //find largest duration of each step for weights in breakdownBar
                        int largestDuration = 0;
                        for (int i = 0; i < routeSteps.size(); i++) {
                            Log.i(TAG,"DURATION: "+routeSteps.get(i).getDuration());
                            String intValue = routeSteps.get(i).getDuration().replaceAll("[^0-9]", "");
                            int duration = Integer.parseInt(intValue);
                            if (largestDuration <= duration){
                                largestDuration = duration;
                            }
                        }

                        for (int i = 0; i < routeSteps.size(); i++) {
                            List<Object> timeTakenEachStep = new ArrayList<>();
                            String travelMode = routeSteps.get(i).getTravelMode();
                            String intValue = routeSteps.get(i).getDuration().replaceAll("[^0-9]", "");
                            float timeTakenWeight = Float.parseFloat(intValue)/largestDuration;
                            Log.i(TAG,"largestDuration= "+largestDuration);
                            Log.i(TAG,"timeTakenWeight= "+timeTakenWeight);
                            Log.d("Byebye", routeSteps.get(i).getDuration());
                            timeTakenEachStep.add(routeSteps.get(i).getDuration());
                            timeTakenEachStep.add(timeTakenWeight);
                            switch (travelMode){
                                case "WALKING":
                                    timeTakenEachStep.add(NavigateTransitCard.WALKING_COLOR);
                                    List<Object> walkingDetails = new ArrayList<>();
                                    int imageViewWalking = R.drawable.ic_baseline_directions_walk_24px;
                                    walkingDetails.add(imageViewWalking);
                                    walkingDetails.add(NavigateTransitCard.WALKING_COLOR);


                                    String translatedWalkingDistance = "";
                                    if(routeSteps.get(i).getDistance().contains("km")) {
                                        translatedWalkingDistance = routeSteps.get(i).getDistance().replace("km", MainActivity.context.getResources().getString(R.string.km));
                                    }

                                    else if(routeSteps.get(i).getDistance().contains("m")) {
                                        translatedWalkingDistance = routeSteps.get(i).getDistance().replace("m", MainActivity.context.getResources().getString(R.string.m));
                                    }

                                    transitStations.put(translatedWalkingDistance,walkingDetails);
                                    break;

                                case "TRANSIT":
                                    //train, lrt and bus
                                    String trainLine = routeSteps.get(i).getTrainLine();
                                    String lineName = null;
                                    card.setNumStops(String.valueOf(routeSteps.get(i).getNumStops()));
                                    int imageViewTransit;
                                    int imageViewColor;
                                    if (trainLine != null) {
                                        listOfTransitModeAndDistances.add(new TransitModeDistances("Subway", trainLine, routeSteps.get(i).getDistance()));
                                        //if train
                                        imageViewTransit = R.drawable.ic_directions_train_black_24dp;
                                        switch (trainLine) {
                                            case "Downtown Line":
                                                imageViewColor = NavigateTransitCard.DTL_COLOR;
                                                timeTakenEachStep.add(NavigateTransitCard.DTL_COLOR);
                                                lineName = NavigateTransitCard.DOWNTOWN_LINE;
                                                break;
                                            case "North East Line":
                                                imageViewColor = NavigateTransitCard.NEL_COLOR;
                                                timeTakenEachStep.add(NavigateTransitCard.NEL_COLOR);
                                                lineName = NavigateTransitCard.NORTH_EAST_LINE;
                                                break;
                                            case "East West Line":
                                                imageViewColor = NavigateTransitCard.EWL_COLOR;
                                                timeTakenEachStep.add(NavigateTransitCard.EWL_COLOR);
                                                lineName = NavigateTransitCard.EAST_WEST_LINE;
                                                break;
                                            case "North South Line":
                                                imageViewColor = NavigateTransitCard.NSL_COLOR;
                                                timeTakenEachStep.add(NavigateTransitCard.NSL_COLOR);
                                                lineName = NavigateTransitCard.NORTH_SOUTH_LINE;
                                                break;
                                            case "Circle Line":
                                                imageViewColor = NavigateTransitCard.CCL_COLOR;
                                                timeTakenEachStep.add(NavigateTransitCard.CCL_COLOR);
                                                lineName = NavigateTransitCard.CIRCLE_LINE;
                                                break;
                                            default:
                                                //if lrt
                                                imageViewColor = NavigateTransitCard.LRT_COLOR;
                                                timeTakenEachStep.add(NavigateTransitCard.LRT_COLOR);
                                                lineName = NavigateTransitCard.LRT_LINE;
                                                break;
                                        }
                                    }else{
                                        //if bus
                                        lineName = routeSteps.get(i).getBusNum();
                                        listOfTransitModeAndDistances.add(new TransitModeDistances("Bus", lineName, routeSteps.get(i).getDistance()));
                                        imageViewTransit = R.drawable.ic_directions_bus_black_24dp;
                                        imageViewColor = NavigateTransitCard.BUS_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.BUS_COLOR);
                                    }
                                    if (!transitStations.containsKey(routeSteps.get(i).getDepartureStop())) {
                                        //sets transport type image, color and line name/bus num for each part of the journey
                                        List<Object> stationDetails = new ArrayList<>();
                                        stationDetails.add(imageViewTransit);
                                        stationDetails.add(imageViewColor);
                                        stationDetails.add(lineName); //line name is bus num
                                        //get in between bus stops
                                        List<String> busStopNames = new ArrayList<>();
                                        if (imageViewColor == NavigateTransitCard.BUS_COLOR) {
                                            Log.i(TAG,"is this a bus?");
                                            //get departure stop's code
                                            //go into linkedlist to get the next X stops
                                            // then get bus stop name
                                            //MainActivity mainActivity = new MainActivity();
                                            Map<String, String> allBusByIdMap = MainActivity.allBusByID;
//                                            Log.i(TAG,"allBusByIdMap "+allBusByIdMap.keySet());
                                            String departureBusStopCode = "";
//                                            for (Map.Entry<String, String> entry : allBusByIdMap.entrySet()) {
//                                        Log.i(TAG,"allBusByIdMap.entrySet()");
                                            if(allBusByIdMap.containsKey(routeSteps.get(i).getDepartureStop())){
//                                                String busStopIDCode = entry.getKey();
                                                Log.i(TAG,"c "+routeSteps.get(i).getDepartureStop());
//                                                String busStopName = entry.getValue();
                                                String busStopName = allBusByIdMap.get(routeSteps.get(i).getDepartureStop());
                                                if (busStopName.equals(routeSteps.get(i).getDepartureStop())){
//                                                    departureBusStopCode = busStopIDCode;
                                                    departureBusStopCode = routeSteps.get(i).getDepartureStop();
                                                    Log.i(TAG,"DEPARTURE CODE "+ departureBusStopCode);
                                                }
                                            }

                                            List<String> busStopCodes = new ArrayList<>();
                                            JSONLTABusRoute busRoute = new JSONLTABusRoute();
                                            Map<String, LinkedList<Value>> busMap = busRoute.getBusRouteMap();
                                            for (int j = 0; j < busMap.size(); j++) {
                                                Log.i(TAG,"this is busMap loop" );
                                                if (busMap.get(lineName) != null) {
                                                    //if bus route exists
                                                    LinkedList<Value> busValue = busMap.get(lineName);
                                                    for (int k = 0; k < busValue.size(); k++) {
                                                        Log.i(TAG,"this is busMap loop + busValueSize" );
                                                        if (busValue.get(k).getBusStopCode().equals(departureBusStopCode)){
                                                            //if departure stop is found, save the next X num of stops
                                                            for (int l = 0; l < Integer.parseInt(card.getNumStops()); l++){
                                                                busStopCodes.add(busValue.get(k+l).getBusStopCode());
                                                                Log.i(TAG,"busStopCodes:"+busStopCodes);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            for (int k = 0; k < busStopCodes.size(); k++){
                                                Log.i(TAG,"busStopCodes k loop");
                                                //Find names of all in busStopCodes
                                                // Key: Bus stop ID Value: Bus stop name
                                                //key: busStopCodes.get(l);
                                                String busStopName = allBusByIdMap.get(busStopCodes.get(k));
                                                busStopNames.add(busStopName);
                                                Log.i(TAG,"Bus stop name (add 1): "+busStopName);
                                            }
                                        }
                                        //key is bus num, value: linkedlist, get bus stop number
                                        //bus stop number get bus stop name from hashmap allBusById
                                        stationDetails.add(routeSteps.get(i).getArrivalStop());
                                        stationDetails.add(routeSteps.get(i).getDuration());
                                        if (!busStopNames.isEmpty()){
                                            stationDetails.add(busStopNames);
                                            Log.i(TAG,"BUS STOP NAMES: "+busStopNames);
                                        }
                                        transitStations.put(routeSteps.get(i).getDepartureStop(),stationDetails);
                                    }
                                    break;
                            }
                            timeTakenList.add(timeTakenEachStep);
                        }
                        //fare calculation
                        double transitDistance = 0.0;
                        for(int i=0; i<listOfTransitModeAndDistances.size(); i++) {

                            transitDistance += Double.valueOf(listOfTransitModeAndDistances.get(i).getDistance().replaceAll("[^.0-9]+", ""));
                        }

                        FareDetails fareDetails = new FareDetails();

                        fareDetails.populateStudentFareDistance();
                        fareDetails.populateStudentFaresMap();

                        fareDetails.populateAdultFareDistance();
                        fareDetails.populateAdultFaresMap();

                        fareDetails.populateSeniorFareDistance();
                        fareDetails.populateSeniorFaresMap();

                        String price = "";

                        if(transitDistance > 0.0) {

                            if(fareTypes.equals(MainActivity.context.getResources().getString(R.string.student))) {

                                for(int i = 0; i < fareDetails.getStudentFareDistance().size(); i++) {

                                    if(i == 0) {
                                        if(transitDistance <= fareDetails.getStudentFareDistance().get(0)) {
                                            price = "$" + fareDetails.getStudentFaresMap().get(fareDetails.getStudentFareDistance().get(0)).getBusMrt();
                                        }
                                    }
                                    else {
                                        if(transitDistance > fareDetails.getStudentFareDistance().get(i-1) && transitDistance <= fareDetails.getStudentFareDistance().get(i)) {
                                            price = "$" + fareDetails.getStudentFaresMap().get(fareDetails.getStudentFareDistance().get(i)).getBusMrt();
                                        }
                                        else if(transitDistance > fareDetails.getStudentFareDistance().get(fareDetails.getStudentFareDistance().size() - 1)){
                                            price = "$" + fareDetails.getStudentFaresMap().get(fareDetails.getStudentFareDistance().get(i)).getBusMrt();
                                        }
                                    }

                                }
                            }
                            else if(fareTypes.equals(MainActivity.context.getResources().getString(R.string.adult))) {

                                for(int i = 0; i < fareDetails.getAdultFareDistance().size(); i++) {

                                    if(i == 0) {
                                        if(transitDistance <= fareDetails.getAdultFareDistance().get(0)) {
                                            price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(0)).getBusMrt();
                                        }
                                    }
                                    else {
                                        if (transitDistance > fareDetails.getAdultFareDistance().get(i - 1) && transitDistance <= fareDetails.getAdultFareDistance().get(i)) {
                                            price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                                        }
                                        else if(transitDistance > fareDetails.getAdultFareDistance().get(fareDetails.getAdultFareDistance().size() - 1)){
                                            price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                                        }
                                    }
                                }
                            }
                            else if(fareTypes.equals(MainActivity.context.getResources().getString(R.string.seniorcitizens))) {

                                for(int i = 0; i < fareDetails.getSeniorFareDistance().size(); i++) {

                                    if(i == 0) {
                                        if(transitDistance <= fareDetails.getSeniorFareDistance().get(0)) {
                                            price = "$" + fareDetails.getSeniorFaresMap().get(fareDetails.getSeniorFareDistance().get(0)).getBusMrt();
                                        }
                                    }
                                    else {
                                        if(transitDistance > fareDetails.getSeniorFareDistance().get(i-1) && transitDistance <= fareDetails.getSeniorFareDistance().get(i)) {
                                            price = "$" + fareDetails.getSeniorFaresMap().get(fareDetails.getSeniorFareDistance().get(i)).getBusMrt();
                                        }
                                        else if(transitDistance > fareDetails.getSeniorFareDistance().get(fareDetails.getSeniorFareDistance().size() - 1)){
                                            price = "$" + fareDetails.getSeniorFaresMap().get(fareDetails.getSeniorFareDistance().get(i)).getBusMrt();
                                        }
                                    }

                                }
                            }
                        }

                        card.setCost(price);
                        card.setTimeTaken(timeTakenList);
                        card.setTransitStations(transitStations);
                    }
                }else{
                    card.setError(googleRoutesData.getError());
                }
                return card;
            }
        };
        try {
            return (NavigateTransitCard)asyncTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
