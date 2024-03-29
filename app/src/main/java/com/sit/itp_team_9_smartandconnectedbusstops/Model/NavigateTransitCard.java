package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.JSONLTABusRoute;
import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.Value;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONTrainStationParser;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.FareDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    public final static int WALKING_COLOR = Color.argb(255,120, 120, 120);
    private final static int LRT_COLOR = Color.argb(255,158, 158, 158);
    private final static int BUS_COLOR = Color.argb(255,0, 0, 0);

    private final static String CIRCLE_LINE = "CCL";
    private final static String DOWNTOWN_LINE = "DTL";
    private final static String NORTH_EAST_LINE = "NEL";
    private final static String EAST_WEST_LINE = "EWL";
    private final static String NORTH_SOUTH_LINE = "NSL";
    private final static String CHANGI_AIRPORT_BRANCH_LINE = "CGL";
    private final static String CIRCLE_LINE_EXTENSION = "CEL";
    private final static String LRT_LINE = "LRT";

    private int ID;
    private String totalTime;
    private String cost;
    private String totalDistance;
    private List<List<Object>> timeTaken; //time(int),weight(float), colour(int) (breakdown bar based on this)
    private String departureStationCode;
    private String numStops;
    private List<String> inBetweenStops;
    private List<LatLng> polyLines;
    private String condition;
    private boolean isFavorite, isUmbrella;
    private Map<String,List<Object>> transitStations; //arrival stop, List<image resource(int),color(int), lineName(string), arrivalStop (string)>

    private String placeidStart, placeidEnd, routeID;
    public ArrayList<String> favRoute = new ArrayList<>();
    private List<String> inBetweenTrainStations;

    private LatLng latLng;

    //For sorting
    private float totalWalkingDistance;
    private int totalTimeInt;
    private float totalDistanceFloat;

    private String error;

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

    public List<LatLng> getPolyLines() {
        return polyLines;
    }

    public void setPolyLines(List<LatLng> polyLines) {
        this.polyLines = polyLines;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDepartureStationCode() {
        return departureStationCode;
    }

    public void setDepartureStationCode(String departureStationCode) {
        this.departureStationCode = departureStationCode;
    }

    public Map<String, List<Object>> getTransitStations() {
        return transitStations;
    }

    private void setTransitStations(Map<String, List<Object>> transitStations) {
        this.transitStations = transitStations;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getTotalWalkingDistance() {
        return totalWalkingDistance;
    }

    public void setTotalWalkingDistance(float totalWalkingDistance) {
        this.totalWalkingDistance = totalWalkingDistance;
    }

    public int getTotalTimeInt() {
        return totalTimeInt;
    }

    public void setTotalTimeInt(int totalTimeInt) {
        this.totalTimeInt = totalTimeInt;
    }

    public float getTotalDistanceFloat() {
        return totalDistanceFloat;
    }

    public void setTotalDistanceFloat(float totalDistanceFloat) {
        this.totalDistanceFloat = totalDistanceFloat;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStartPlaceId() {
        return placeidStart;
    }

    public void setStartPlaceId(String placeidStart) {
        this.placeidStart = placeidStart;
    }

    public String geEndPlaceId() {
        return placeidEnd;
    }

    public void setEndPlaceId(String placeidEnd) {
        this.placeidEnd = placeidEnd;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public boolean isUmbrella(){return isUmbrella;}

    public void setUmbrella(boolean umbrella){ isUmbrella = umbrella;}

    /**
     * Sets and returns a NavigateTransitCard card
     * <p>
     * This method always returns immediately
     *
     * @param googleRoutesData GoogleRoutesData
     * @return card NavigateTransitCard
     */
    public static NavigateTransitCard getRouteData(GoogleRoutesData googleRoutesData, String fareTypes, String trafCon, boolean umbrellaBring) {
        NavigateTransitCard card = new NavigateTransitCard();
        card.setType(Card.NAVIGATE_TRANSIT_CARD);
        card.setNeedsUpdate(true);
        if (googleRoutesData.getError() == null || googleRoutesData.getError().isEmpty()){
            card.setID(googleRoutesData.getID());
            card.setTotalDistance(googleRoutesData.getTotalDistance());
            card.setTotalTime(googleRoutesData.getTotalDuration());
            card.setStartPlaceId(googleRoutesData.getStartPlaceId());
            card.setEndPlaceId(googleRoutesData.geEndPlaceId());
            card.setUmbrella(umbrellaBring);
            String routeID = googleRoutesData.getStartPlaceId()+"/"+ googleRoutesData.geEndPlaceId()+"/"+googleRoutesData.getID()+"/"+fareTypes;
            card.setRouteID(routeID);
            Log.d(TAG, "getRouteData: setRouteID: "+routeID);
           
            //For sorting
            card.setTotalDistanceFloat(convertDistanceToKm(googleRoutesData.getTotalDistance()));
            card.setTotalTimeInt(convertTimeToMinutes(googleRoutesData.getTotalDuration()));

            //in Steps
            List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
            if (routeSteps != null) {
                Map<String,List<Object>> transitStations = new LinkedHashMap<>();
                List<List<Object>> timeTakenList = new ArrayList<>();
                List<TransitModeDistances> listOfTransitModeAndDistances = new ArrayList<>();
                List<LatLng> listOfPolyLines = new ArrayList<>();
                float totalWalkingDistance = 0;
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
                    listOfPolyLines.addAll(routeSteps.get(i).getPolyline());
                    String travelMode = routeSteps.get(i).getTravelMode();
                    String intValue = routeSteps.get(i).getDuration().replaceAll("[^0-9]", "");
                    float timeTakenWeight = Float.parseFloat(intValue)/largestDuration;
                    Log.i(TAG,"largestDuration= "+largestDuration);
                    Log.i(TAG,"timeTakenWeight= "+timeTakenWeight);
                    timeTakenEachStep.add(routeSteps.get(i).getDuration());
                    timeTakenEachStep.add(timeTakenWeight);
                    switch (travelMode){
                        case "WALKING":
                            timeTakenEachStep.add(NavigateTransitCard.WALKING_COLOR);
                            List<Object> walkingDetails = new ArrayList<>();
                            int imageViewWalking = R.drawable.ic_baseline_directions_walk_24px;
                            walkingDetails.add(imageViewWalking);
                            walkingDetails.add(NavigateTransitCard.WALKING_COLOR);
                            walkingDetails.add(null);

                            //get detailed walking steps
                            List<String> walkingDetailedStepsChildren = new ArrayList<>();
                            for (int j = 0; j < routeSteps.get(i).getDetailedSteps().size(); j++){
                                walkingDetailedStepsChildren.add(routeSteps.get(i).getDetailedSteps().get(j).getHtmlInstructions());
                            }
                            for (int j = 3; j < 5; j++) {
                                walkingDetails.add(j,null);
                            }
                            walkingDetails.add(5,routeSteps.get(i).getDuration());
                            walkingDetails.add(6,walkingDetailedStepsChildren);
                            transitStations.put(routeSteps.get(i).getDistance(),walkingDetails);

                            //for sorting by walking distance
                            float walkingDistance = convertDistanceToKm(routeSteps.get(i).getDistance());
                            Log.i(TAG, "walkingDistance"+ walkingDistance);
                            totalWalkingDistance += walkingDistance;
                            Log.i(TAG, "totalWalkingDistance"+ totalWalkingDistance);
                            break;

                        case "TRANSIT":
                            //train, lrt and bus
                            String trainLine = routeSteps.get(i).getTrainLine();
                            String lineName;
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
                                listOfTransitModeAndDistances.add(new TransitModeDistances("bus", lineName, routeSteps.get(i).getDistance()));
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
                                stationDetails.add(routeSteps.get(i).getNumStops());

                                //get in between bus stops
                                List<String> busStopNames = new ArrayList<>();
                                List<String> trainStationNames = new ArrayList<>();
                                if (imageViewTransit == R.drawable.ic_directions_bus_black_24dp) {
                                    Log.i(TAG,"is this a bus?");

                                    //bus stop code: get departure stop's code
                                    Map<String, String> allBusStopsByIdMap = MainActivity.allBusByID; //bus stop IDs
                                    List<String> departureBusStopCodeList = new ArrayList<>(); //names are not unique
                                    List<String> arrivalBusStopCodeList = new ArrayList<>();
                                    String departureBusStopCode = null, arrivalBusStopCode = null;
                                    for (Map.Entry<String, String> entry : allBusStopsByIdMap.entrySet()) {
                                        String busStopIDCode = entry.getKey();
                                        //Log.i(TAG,"busStopIDCode "+busStopIDCode);
                                        String busStopName = entry.getValue();
                                        //Log.i(TAG,"busStopName "+busStopName);
                                        if (busStopName != null && routeSteps.get(i).getDepartureStop() != null
                                                && routeSteps.get(i).getDepartureStop().equalsIgnoreCase(busStopName)){
                                            //departureBusStopCode = busStopIDCode;
                                            departureBusStopCodeList.add(busStopIDCode);
                                            Log.i(TAG,"departure code list "+ departureBusStopCodeList);
                                        }else{
                                            departureBusStopCodeList.add("UNKNOWN");
                                        }
                                        if (busStopName != null && routeSteps.get(i).getDepartureStop() != null
                                                && routeSteps.get(i).getArrivalStop().equalsIgnoreCase(busStopName)){
                                            //arrivalBusStopCode = busStopIDCode;
                                            arrivalBusStopCodeList.add(busStopIDCode);
                                            Log.i(TAG,"arrival code list"+ arrivalBusStopCodeList);
                                        }else{
                                            arrivalBusStopCodeList.add("UNKNOWN");
                                        }
                                    }
                                    //bus route
                                    JSONLTABusRoute busRoute = new JSONLTABusRoute();
                                    Map<String, LinkedList<Value>> busMap = busRoute.getBusRouteMap();
                                    List<String> inBetweenBusStopCodes = new ArrayList<>();
                                    OUTER_LOOP:
                                    for (Map.Entry<String, LinkedList<Value>> busMapEntry : busMap.entrySet()) {
                                        String busServiceNumber = busMapEntry.getKey();
                                        LinkedList<Value> busMapValue = busMapEntry.getValue();
                                        if (busServiceNumber.equalsIgnoreCase(lineName)) {
                                            //if found the correct bus service no.
                                            Log.i(TAG,"found the correct bus num!");
                                            Log.i(TAG,busMapValue.toString());

                                            for (int j = 0; j < busMapValue.size(); j++) {
                                                //loop through the bus service's bus stops (going through the bus route)
                                                for (String potentialArrivalBusStopCode : arrivalBusStopCodeList) {
                                                    //Log.i(TAG, "arrival codes: "
                                                    //        + " " + potentialArrivalBusStopCode);
                                                    if (busMapValue.get(j).getBusStopCode()
                                                            .equals(potentialArrivalBusStopCode)) {
                                                        Log.i(TAG, "ONLY NEED ARRIVAL");
                                                        int arrivalBusDirection =
                                                                busMapValue.get(j).getDirection();
                                                        if ((j - routeSteps.get(i).getNumStops()) > 0
                                                                && busMapValue.get(j - routeSteps.get(i)
                                                                .getNumStops()) != null
                                                                && busMapValue.get(j - routeSteps.get(i).getNumStops())
                                                                .getDirection() == arrivalBusDirection) {
                                                            for (int k = (routeSteps.get(i).getNumStops() - 1); k > 0; k--) {
                                                                inBetweenBusStopCodes.add
                                                                        (busMapValue.get(j - k).getBusStopCode());
                                                                Log.i(TAG, "inBetweenBusStopCodes"
                                                                        + inBetweenBusStopCodes);
                                                            }
                                                            break OUTER_LOOP;
                                                        }

                                                    }
                                                }
                                                for (String potentialDepartureBusStopCode : departureBusStopCodeList) {
                                                    //Log.i(TAG,"no arrival");
                                                    //only departureBusStopCode found
                                                    if (busMapValue.get(j).getBusStopCode()
                                                            .equals(potentialDepartureBusStopCode)){
                                                        Log.i(TAG, "ONLY NEED DEPARTURE");
                                                        int departureBusDirection = busMapValue.get(j).getDirection();
                                                        if ((j + routeSteps.get(i).getNumStops()) < busMapValue.size()
                                                                && busMapValue.get(j + routeSteps.get(i).getNumStops()) != null
                                                                && busMapValue.get(j + routeSteps.get(i).getNumStops())
                                                                .getDirection() == departureBusDirection )
                                                        {
                                                            //if arrival stop is not null
                                                            // and direction of stops match
                                                            for (int k = 1; k < routeSteps.get(i).getNumStops(); k++) {
                                                                inBetweenBusStopCodes.add(busMapValue
                                                                        .get(j + k).getBusStopCode());
                                                                Log.i(TAG, "inBetweenBusStopCodes"
                                                                        + inBetweenBusStopCodes);
                                                            }
                                                            break OUTER_LOOP;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    for (int j = 0; j < inBetweenBusStopCodes.size(); j++) {
                                        //Find names of all in busStopCodes
                                        // Key: Bus stop ID Value: Bus stop name
                                        String busStopName = allBusStopsByIdMap.get(inBetweenBusStopCodes.get(j));
                                        busStopNames.add(busStopName);
                                        Log.i(TAG, "Bus stop name (add 1): " + busStopName);
                                    }
                                }else{
                                    //train stations

                                    //get all stations in trainLine
                                    String queryAllStationsInLine;
                                    switch (trainLine){
                                        case "Circle Line":
                                            queryAllStationsInLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                    + "环线";
                                            break;
                                        case "Circle Line Extension":
                                            queryAllStationsInLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                    + "环线延长线";
                                            break;
                                        default:
                                            queryAllStationsInLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                    + trainLine;
                                            break;
                                    }
                                    List<TrainStation> allTrainStationsInLine = lookUpTrainStations(queryAllStationsInLine);
                                    if (allTrainStationsInLine != null) {
                                        for (TrainStation trainStation : allTrainStationsInLine) {
                                            System.out.println("original"+trainStation.toString()+"\n");
                                        }

                                        Collections.sort(allTrainStationsInLine); //sort by ascending station num

                                        //add in stations in extension lines
                                        if (trainLine.equals("Circle Line")){
                                            String queryExtension = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                    + "环线延长线";
                                            List<TrainStation> allTrainStationsExtension = lookUpTrainStations(queryExtension);
                                            if (allTrainStationsExtension != null) {
                                                allTrainStationsInLine.add(0, allTrainStationsExtension.get(1));
                                                allTrainStationsInLine.add(0, allTrainStationsExtension.get(0));
                                            }
                                        }else if (trainLine.equals("East West Line")){
                                            String queryExtension = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                    + "Changi Airport Branch Line";
                                            List<TrainStation> allTrainStationsExtension = lookUpTrainStations(queryExtension);
                                            if (allTrainStationsExtension != null) {
                                                for (TrainStation trainStationExtension : allTrainStationsExtension) {
                                                    allTrainStationsInLine.add(0, trainStationExtension);
                                                }
                                            }
                                        }
                                        for(TrainStation trainStation: allTrainStationsInLine){
                                            System.out.println("sortedStation: "+trainStation.toString()+"\n");
                                        }
                                    }
                                    List<TrainStation> lrtEastLoop = new ArrayList<>();
                                    List<TrainStation> lrtWestLoop = new ArrayList<>();
                                    //separate east and west loop into 2 lists
                                    if (trainLine.equals("Sengkang LRT")){
                                        for(TrainStation trainStation: allTrainStationsInLine) {
                                            Log.i(TAG, "Sengkang LRT:"+trainStation.toString());
                                            if (trainStation.getStationCode().equals("STC")){
                                                //main sengkang stop is available from both loops
                                                Log.i(TAG,"STC?"+trainStation.getStationName());
                                                trainStation.setStationNum(0);
                                                lrtWestLoop.add(trainStation);
                                                lrtEastLoop.add(trainStation);
                                            } else if(trainStation.getStationCode().contains("SW")){
                                                //west loop
                                                lrtWestLoop.add(trainStation);
                                            }
                                            else{
                                                //east loop
                                                lrtEastLoop.add(trainStation);
                                            }
                                        }

                                        for(TrainStation trainStation: lrtEastLoop){
                                            Log.i(TAG,"east loop: "+trainStation.toString()+"\n");
                                        }
                                        for(TrainStation trainStation: lrtWestLoop){
                                            Log.i(TAG,"west loop: "+trainStation.toString()+"\n");
                                        }
                                    }else if (trainLine.equals("Punggol LRT")){
                                        for(TrainStation trainStation: allTrainStationsInLine) {
                                            if (trainStation.getStationCode().equals("PTC")){
                                                //main punggol stop is available from both loops
                                                trainStation.setStationNum(0);
                                                lrtWestLoop.add(trainStation);
                                                lrtEastLoop.add(trainStation);
                                            } else if(trainStation.getStationCode().contains("PW")){
                                                //west loop
                                                if (trainStation.getStationCode().equals("PW2")) {
                                                    //PW2 Teck Lee not in use
                                                    continue;
                                                }
                                                lrtWestLoop.add(trainStation);
                                            }
                                            else{
                                                //east loop
                                                lrtEastLoop.add(trainStation);
                                            }
                                        }

                                        Collections.sort(lrtEastLoop); //sort by ascending station num
                                        Collections.sort(lrtWestLoop); //sort by ascending station num

                                        for(TrainStation trainStation: lrtEastLoop){
                                            Log.i(TAG,"east loop: "+trainStation.toString()+"\n");
                                        }
                                        for(TrainStation trainStation: lrtWestLoop){
                                            Log.i(TAG,"west loop: "+trainStation.toString()+"\n");
                                        }
                                    }

                                    if(allTrainStationsInLine != null) {
                                        TrainStation departureTrainStation = null, arrivalTrainStation = null;
                                        String departureStation = cleanUpStationName(routeSteps.get(i).getDepartureStop());
                                        String arrivalStation = cleanUpStationName(routeSteps.get(i).getArrivalStop());
                                        Log.i(TAG,"departureStation "+departureStation);
                                        Log.i(TAG,"arrivalStation "+arrivalStation);
                                        //get departure and arrival stations
                                        for (TrainStation trainStationInList : allTrainStationsInLine) {
                                            if (trainStationInList.getStationName().equalsIgnoreCase(
                                                    departureStation)
                                                    || trainStationInList.getStationNameChinese()
                                                    .equalsIgnoreCase(departureStation)){
                                                departureTrainStation = trainStationInList;
                                                Log.i(TAG, "found departure train station");
                                            }
                                            if (trainStationInList.getStationName().equalsIgnoreCase(
                                                    arrivalStation)
                                                    || trainStationInList.getStationNameChinese()
                                                    .equalsIgnoreCase(arrivalStation)){
                                                arrivalTrainStation = trainStationInList;
                                                Log.i(TAG, "found arrival train station");
                                            }
                                        }
                                        if (departureTrainStation != null && arrivalTrainStation != null ){
                                            //if both stations found
                                            for (int j = 0; j < allTrainStationsInLine.size(); j++) {
                                                if (allTrainStationsInLine.get(j).getStationCode()
                                                        .equals(departureTrainStation.getStationCode())){
                                                    //j is position of departure train station
                                                    if (departureTrainStation.getStationNum()
                                                            < arrivalTrainStation.getStationNum()) {
                                                        //if departure comes before arrival in list
                                                        int numStops = routeSteps.get(i).getNumStops();
                                                        for (int k = 1; k < numStops; k++) {
                                                            //add until it reaches end of num of stops
                                                            if ((departureTrainStation.getStationCode().equals("CE1") ||
                                                                    departureTrainStation.getStationCode().equals("CE2")) &&
                                                                    allTrainStationsInLine.get(j+k).getStationCode().equals("CC1")
                                                                    ){
                                                                //extension line stops placed after stations in original line
                                                                Log.i(TAG,"EXTENSION LINE CCL");
                                                                k+=3;
                                                                numStops+=3;
                                                            }
                                                            if(trainLine.equals("Bukit Panjang LRT") &&
                                                                    arrivalTrainStation.getStationNum() >= 11 &&
                                                                    allTrainStationsInLine.get(j+k).getStationCode().equals("BP7")
                                                                    ){
                                                                Log.i(TAG,"BP LRT");
                                                                for (TrainStation ts : allTrainStationsInLine){
                                                                    if (ts.getStationCode().equals("BP14")){
                                                                        allTrainStationsInLine.remove(ts);
                                                                    }
                                                                }
                                                                for (int l=1; l < 3; l++){

                                                                    //add from last index, in reverse order
                                                                    trainStationNames.add(allTrainStationsInLine
                                                                            .get(allTrainStationsInLine.size()-l).getStationName());
                                                                    if (arrivalTrainStation.getStationNum() == 12 ){
                                                                        break;
                                                                    }
                                                                    Log.i(TAG, "l = "+l);
                                                                }
                                                                break;
                                                            }

                                                            if (trainLine.equals("Sengkang LRT")){
                                                                if (arrivalTrainStation.getStationCode().contains("SE")) {
                                                                    //east loop
                                                                    if(arrivalTrainStation.getStationNum() > 3 &&
                                                                            departureTrainStation.getStationNum() == 0){
                                                                        //go backwards from last station
                                                                        //only happens when from Sengkang (STC)
                                                                        for (int l = 1; l < numStops; l++) {
                                                                            trainStationNames.add(lrtEastLoop
                                                                                    .get(lrtEastLoop.size() - l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = 1; l < numStops; l++) {
                                                                        trainStationNames.add(lrtEastLoop
                                                                                .get(departureTrainStation.getStationNum() + l).getStationName());
                                                                    }
                                                                }else{
                                                                    //west loop
                                                                    if(arrivalTrainStation.getStationNum() > 5 &&
                                                                            departureTrainStation.getStationNum() == 0){
                                                                        //go backwards from last station
                                                                        //only happens when from Sengkang (STC)
                                                                        for (int l = 1; l < numStops; l++) {
                                                                            trainStationNames.add(lrtWestLoop
                                                                                    .get(lrtWestLoop.size() - l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = 1; l < numStops; l++) {
                                                                        trainStationNames.add(lrtWestLoop
                                                                                .get(departureTrainStation.getStationNum() + l).getStationName());
                                                                    }
                                                                }
                                                                break;
                                                            }

                                                            if (trainLine.equals("Punggol LRT")){
                                                                if (arrivalTrainStation.getStationCode().contains("PE")) {
                                                                    //east loop
                                                                    if(arrivalTrainStation.getStationNum() > 4 &&
                                                                            departureTrainStation.getStationNum() == 0){
                                                                        //go backwards from last station
                                                                        //only happens when from Punggol (PTC)
                                                                        for (int l = 1; l < numStops; l++) {
                                                                            trainStationNames.add(lrtEastLoop
                                                                                    .get(lrtEastLoop.size() - l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = 1; l < numStops; l++) {
                                                                        trainStationNames.add(lrtEastLoop
                                                                                .get(departureTrainStation.getStationNum() + l).getStationName());
                                                                    }
                                                                }else{
                                                                    //west loop
                                                                    if(arrivalTrainStation.getStationNum() > 4 &&
                                                                            departureTrainStation.getStationNum() == 0){
                                                                        //go backwards from last station
                                                                        //only happens when from Punggol (PTC)
                                                                        for (int l = 1; l < numStops; l++) {
                                                                            trainStationNames.add(lrtWestLoop
                                                                                    .get(lrtWestLoop.size() - l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = 1; l < numStops; l++) {
                                                                        trainStationNames.add(lrtWestLoop
                                                                                .get(departureTrainStation.getStationNum() + l).getStationName());
                                                                    }
                                                                }
                                                                break;
                                                            }

                                                            trainStationNames.add(allTrainStationsInLine
                                                                    .get(j + k).getStationName());
                                                        }
                                                    }else{
                                                        //if departure comes after arrival in list
                                                        for (int k = 1; k < routeSteps.get(i).getNumStops(); k++) {
                                                            //add until it reaches end of num of stops
                                                            if((arrivalTrainStation.getStationCode().equals("CG1") ||
                                                                    arrivalTrainStation.getStationCode().equals("CG2"))
                                                                    && departureTrainStation.getStationCode().equals("EW4")) {
                                                                //extension line stops placed before stations in original line
                                                                Log.i(TAG,"EXTENSION LINE EWL");
                                                                k+=3;
                                                            }

                                                            if ((arrivalTrainStation.getStationCode().equals("CE1") ||
                                                                    arrivalTrainStation.getStationCode().equals("CE2")) &&
                                                                    allTrainStationsInLine.get(j-k).getStationCode().equals("CC3")){
                                                                Log.i(TAG,"EXTENSION LINE CCL");
                                                                k+=3;
                                                            }
                                                            if(trainLine.equals("Bukit Panjang LRT") &&
                                                                    departureTrainStation.getStationNum() >= 11 &&
                                                                    arrivalTrainStation.getStationNum() < 7
                                                                    ){
                                                                for (TrainStation ts : allTrainStationsInLine){
                                                                    if (ts.getStationCode().equals("BP14")){
                                                                        allTrainStationsInLine.remove(ts);
                                                                    }
                                                                }
                                                                //add forward until end of LRT line
                                                                for (int l=departureTrainStation.getStationNum();
                                                                     l < allTrainStationsInLine.size(); l++) {
                                                                    trainStationNames.add(allTrainStationsInLine
                                                                            .get(l).getStationName());
                                                                }

                                                                //then continue adding from BP6 till BP1
                                                                for (int m=5;
                                                                     m > arrivalTrainStation.getStationNum()-1; m--) {
                                                                    Log.i(TAG,"m7");
                                                                    trainStationNames.add(allTrainStationsInLine
                                                                            .get(m).getStationName());
                                                                }
                                                                break;
                                                            }

                                                            if (trainLine.equals("Sengkang LRT")){
                                                                if (departureTrainStation.getStationCode().contains("SE")) {
                                                                    //east loop
                                                                    if(departureTrainStation.getStationNum() > 3 &&
                                                                            arrivalTrainStation.getStationNum() == 0){
                                                                        //only happens when going to Sengkang (STC)
                                                                        for (int l = departureTrainStation.getStationNum()+1;
                                                                             l < lrtEastLoop.size() ; l++) {
                                                                            trainStationNames.add(lrtEastLoop
                                                                                    .get(l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = departureTrainStation.getStationNum()-1;
                                                                         l > arrivalTrainStation.getStationNum(); l--) {
                                                                        trainStationNames.add(lrtEastLoop
                                                                                .get(l).getStationName());
                                                                    }
                                                                }else{
                                                                    //west loop
                                                                    if(departureTrainStation.getStationNum() > 5 &&
                                                                            arrivalTrainStation.getStationNum() == 0){
                                                                        //only happens when going to Sengkang (STC)
                                                                        for (int l = departureTrainStation.getStationNum()+1;
                                                                             l < lrtWestLoop.size() ; l++) {
                                                                            trainStationNames.add(lrtWestLoop
                                                                                    .get(l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = departureTrainStation.getStationNum()-1;
                                                                         l > arrivalTrainStation.getStationNum(); l--) {
                                                                        trainStationNames.add(lrtWestLoop
                                                                                .get(l).getStationName());
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            if (trainLine.equals("Punggol LRT")){
                                                                if (departureTrainStation.getStationCode().contains("PE")) {
                                                                    //east loop
                                                                    if(departureTrainStation.getStationNum() > 4 &&
                                                                            arrivalTrainStation.getStationNum() == 0){
                                                                        //only happens when going to Punggol (PTC)
                                                                        for (int l = departureTrainStation.getStationNum()+1;
                                                                             l < lrtEastLoop.size() ; l++) {
                                                                            trainStationNames.add(lrtEastLoop
                                                                                    .get(l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = departureTrainStation.getStationNum()-1;
                                                                         l > arrivalTrainStation.getStationNum(); l--) {
                                                                        trainStationNames.add(lrtEastLoop
                                                                                .get(l).getStationName());
                                                                    }
                                                                }else{
                                                                    //west loop
                                                                    if(departureTrainStation.getStationNum() > 4 &&
                                                                            arrivalTrainStation.getStationNum() == 0){
                                                                        //only happens when going to Punggol (STC)
                                                                        for (int l = departureTrainStation.getStationNum();
                                                                             l < lrtWestLoop.size() ; l++) {
                                                                            trainStationNames.add(lrtWestLoop
                                                                                    .get(l).getStationName());
                                                                        }
                                                                        break;
                                                                    }
                                                                    //as per normal
                                                                    for (int l = departureTrainStation.getStationNum()-2;
                                                                         l > arrivalTrainStation.getStationNum(); l--) {
                                                                        trainStationNames.add(lrtWestLoop
                                                                                .get(l).getStationName());
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            trainStationNames.add(allTrainStationsInLine
                                                                    .get(j - k).getStationName());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                stationDetails.add(routeSteps.get(i).getArrivalStop());
                                stationDetails.add(routeSteps.get(i).getDuration());
                                if (!busStopNames.isEmpty()){
                                    stationDetails.add(busStopNames);
                                    Log.i(TAG,"BUS STOP NAMES: "+busStopNames);
                                }else{
                                    stationDetails.add(trainStationNames);
                                    Log.i(TAG,"TRAIN STATION NAMES: "+trainStationNames);
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
                    String transitModeAndDistance = listOfTransitModeAndDistances.get(i).getDistance().replaceAll("[^.0-9]+", "");
                    transitDistance += Double.valueOf(transitModeAndDistance.replaceAll(".$", ""));
                }

                FareDetails fareDetails = new FareDetails();

                String price = "";

                if(transitDistance > 0.0) {
                    switch (fareTypes) {
                        case "Student":
                            for (int i = 0; i < fareDetails.getStudentFareDistance().size(); i++) {
                                if (i == 0) {
                                    if (transitDistance <= fareDetails.getStudentFareDistance().get(0)) {
                                        price = "$" + fareDetails.getStudentFaresMap().get(fareDetails.getStudentFareDistance().get(0)).getBusMrt();
                                    }
                                } else {
                                    if (transitDistance > fareDetails.getStudentFareDistance().get(i - 1) && transitDistance <= fareDetails.getStudentFareDistance().get(i)) {
                                        price = "$" + fareDetails.getStudentFaresMap().get(fareDetails.getStudentFareDistance().get(i)).getBusMrt();
                                    } else if (transitDistance > fareDetails.getStudentFareDistance().get(fareDetails.getStudentFareDistance().size() - 1)) {
                                        price = "$" + fareDetails.getStudentFaresMap().get(fareDetails.getStudentFareDistance().get(i)).getBusMrt();
                                    }
                                }

                            }
                            break;
                        case "Adult":
                            for (int i = 0; i < fareDetails.getAdultFareDistance().size(); i++) {

                                if (i == 0) {
                                    if (transitDistance <= fareDetails.getAdultFareDistance().get(0)) {
                                        price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(0)).getBusMrt();
                                    }
                                } else {
                                    if (transitDistance > fareDetails.getAdultFareDistance().get(i - 1) && transitDistance <= fareDetails.getAdultFareDistance().get(i)) {
                                        price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                                    } else if (transitDistance > fareDetails.getAdultFareDistance().get(fareDetails.getAdultFareDistance().size() - 1)) {
                                        price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                                    }
                                }
                            }
                            break;
                        case "Senior Citizens":
                            for (int i = 0; i < fareDetails.getSeniorFareDistance().size(); i++) {

                                if (i == 0) {
                                    if (transitDistance <= fareDetails.getSeniorFareDistance().get(0)) {
                                        price = "$" + fareDetails.getSeniorFaresMap().get(fareDetails.getSeniorFareDistance().get(0)).getBusMrt();
                                    }
                                } else {
                                    if (transitDistance > fareDetails.getSeniorFareDistance().get(i - 1) && transitDistance <= fareDetails.getSeniorFareDistance().get(i)) {
                                        price = "$" + fareDetails.getSeniorFaresMap().get(fareDetails.getSeniorFareDistance().get(i)).getBusMrt();
                                    } else if (transitDistance > fareDetails.getSeniorFareDistance().get(fareDetails.getSeniorFareDistance().size() - 1)) {
                                        price = "$" + fareDetails.getSeniorFaresMap().get(fareDetails.getSeniorFareDistance().get(i)).getBusMrt();
                                    }
                                }

                            }
                            break;
                        default: // ADULT
                            for (int i = 0; i < fareDetails.getAdultFareDistance().size(); i++) {

                                if (i == 0) {
                                    if (transitDistance <= fareDetails.getAdultFareDistance().get(0)) {
                                        price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(0)).getBusMrt();
                                    }
                                } else {
                                    if (transitDistance > fareDetails.getAdultFareDistance().get(i - 1) && transitDistance <= fareDetails.getAdultFareDistance().get(i)) {
                                        price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                                    } else if (transitDistance > fareDetails.getAdultFareDistance().get(fareDetails.getAdultFareDistance().size() - 1)) {
                                        price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                                    }
                                }
                            }
                            break;
                    }
                }
                card.setPolyLines(listOfPolyLines);
                card.setTotalWalkingDistance(totalWalkingDistance);
                Log.i(TAG,listOfPolyLines.toString());
                card.setCost(price);
                card.setTimeTaken(timeTakenList);
                card.setTransitStations(transitStations);
                card.setCondition(trafCon);
            }
        }else{
            card.setError(googleRoutesData.getError());
        }
        return card;
    }

    private static String cleanUpStationName(String stationName){
        switch(stationName){
            case "One North Station":
                stationName = stationName.replace(" Station","");
                stationName = stationName.replace(" ","-");
                break;
            case "Redhill (EW18)":
                stationName = stationName.replace(" (EW18)","");
                break;
            case "Tai Seng (CC11)":
                stationName = stationName.replace(" (CC11)","");
                break;
        }
        //if chinese used to query
        SharedPreferences prefs = MainActivity.context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        Log.i(TAG,"language? "+language);
        if (language.equals("zh")){
            Log.i(TAG, "chinese??");
            stationName = stationName.replaceAll("地鐵.*$","").trim();
            stationName = stationName.replaceAll("輕軌站.*$","").trim();
            stationName = stationName.replaceAll("轻轨站.*$","").trim();

            //convert station names returned in traditional chinese to simplified chinese
            switch(stationName) {
                //EWL
                case("樟宜機場"):
                    stationName = "樟宜机场";
                    break;
                case("景萬岸"):
                    stationName = "景万岸";
                    break;
                case("勞明達"):
                    stationName = "劳明达";
                    break;
                case("中峇魯"):
                    stationName = "中峇鲁";
                    break;
                case("紅山"):
                    stationName = "红山";
                    break;
                case("聯邦"):
                    stationName = "联邦";
                    break;
                case("波那維斯達"):
                    stationName = "波那维斯达";
                    break;
                case("裕廊東"):
                    stationName = "裕廊东";
                    break;
                //DTL
                case("海灣舫"):
                    stationName = "海湾舫";
                    break;
                case("寶門廊"):
                    stationName = "宝门廊";
                    break;
                case("陳嘉庚"):
                    stationName = "陈嘉庚";
                    break;
                case("凱秀"):
                    stationName = "凯秀";
                    break;
                case("武吉班讓"):
                    stationName = "武吉班让";
                    break;
                //NSL
                case("烏節"):
                    stationName = "乌节";
                    break;
                case("諾維娜"):
                    stationName = "诺维娜";
                    break;
                case("宏茂橋"):
                    stationName = "宏茂桥";
                    break;
                case("義順"):
                    stationName = "义顺";
                    break;
                case("克蘭芝"):
                    stationName = "克兰芝";
                    break;
                //CCL
                case("尼誥大道"):
                    stationName = "尼诰大道";
                    break;
                case("體育場"):
                    stationName = "体育场";
                    break;
                case("達科達"):
                    stationName = "达科达";
                    break;
                case("直落布蘭雅"):
                    stationName = "直落布兰雅";
                    break;
                //NEL
                case("後港"):
                    stationName = "后港";
                    break;
                case("文慶"):
                    stationName = "文庆";
                    break;
                case("花拉公園"):
                    stationName = "花拉公园";
                    break;
            }
        }else if(language.equals("ms")){
            stationName = stationName.replaceAll("^Stesen MRT ","").trim();
        }else if (language.equals("ta")){
            switch (stationName){
                //EWL
                case("ரெட்ஹில்"):
                    stationName = "Redhill";
                    break;
                case("குவீன்ஸ்டவுன்"):
                    stationName = "Queenstown";
                    break;
                case("காமன்வெல்த்"):
                    stationName = "Commonwealth";
                    break;
                case("புவன விஸ்தா"):
                    stationName = "Buona Vista";
                    break;
                case("சீனத் தோட்டம் தொடருந்து நிலையம்"):
                    stationName = "Chinese Garden";
                    break;
                case("ஏரிக்கரை"):
                    stationName = "Lakeside";
                    break;
                case("பூன் லே"):
                    stationName = "Boon Lay";
                    break;
                case("பயனியர் தொடருந்து நிலையம்"):
                    stationName = "Pioneer";
                    break;
                case("சீமெய் தொடருந்து நிலையம்"):
                    stationName = "Simei";
                    break;
                case("பாசிர் ரிஸ் தொடருந்து நிலையம்"):
                    stationName = "Pasir Ris";
                    break;
                //NSL
                case("யீஷூன் ரயில் நிலையம்"):
                    stationName = "Yishun";
                    break;
                case("கிராஞ்சி ரயில் நிலையம்"):
                    stationName = "Kranji";
                    break;
            }
        }
        stationName = stationName.replaceAll(MainActivity.context.getResources().getString(R.string.mrt_station)+".*$","").trim();
        stationName = stationName.replaceAll("MRT Station.*$","").trim(); //some stations in english regardless of language in query
        Log.i(TAG, "stationNameRegex "+stationName);

        return stationName;
    }

    private static List<TrainStation> lookUpTrainStations(String query){
        List<String> trainStationsQuery = new ArrayList<>();
        trainStationsQuery.add(query);
        Log.i(TAG,"lookUpTrainStations: "+trainStationsQuery.toString());
        JSONTrainStationParser trainStationParser = new JSONTrainStationParser(trainStationsQuery);
        List<TrainStation> result; //result from parser
        List<TrainStation> trainStations = new ArrayList<>();
        try {
            result = trainStationParser.execute().get();
            Log.d(TAG, query);
            if (result.size() <= 0) {
                Log.d(TAG, "trainStation: Data.gov returned no data");
                return null;
            }else{
                //got result
                return result;
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Comparator for sorting the list by total distance
    public static Comparator<NavigateTransitCard> distanceComparator = new Comparator<NavigateTransitCard>() {

        public int compare(NavigateTransitCard c1, NavigateTransitCard c2) {
            //Float c1TotalDistance = convertDistanceToKm(c1.getTotalDistance());
            //Float c2TotalDistance = convertDistanceToKm(c2.getTotalDistance());
            Float c1TotalDistance = c1.getTotalDistanceFloat();
            Float c2TotalDistance = c2.getTotalDistanceFloat();
            //ascending order
            //return c1TotalDistance.compareTo(c2TotalDistance);

            return Float.compare(c1TotalDistance,c2TotalDistance);
        }
    };

    //Comparator for sorting the list by total time
    public static Comparator<NavigateTransitCard> timeComparator = new Comparator<NavigateTransitCard>() {

        public int compare(NavigateTransitCard c1, NavigateTransitCard c2) {
            int c1TotalTime = c1.getTotalTimeInt();
            int c2TotalTime = c2.getTotalTimeInt();
            //ascending order
            return Integer.compare(c1TotalTime,c2TotalTime);
        }
    };

    //Comparator for sorting the list by total walking distance
    public static Comparator<NavigateTransitCard> walkingDistanceComparator = new Comparator<NavigateTransitCard>() {

        public int compare(NavigateTransitCard c1, NavigateTransitCard c2) {
            //need new walking only distance
            float c1TotalWalkingDistance = c1.getTotalWalkingDistance();
            float c2TotalWalkingDistance = c2.getTotalWalkingDistance();
            //ascending order
            return Float.compare(c1TotalWalkingDistance,c2TotalWalkingDistance);
        }
    };

    private static Float convertDistanceToKm(String distanceString){
        float distanceInKm;
        if (distanceString.contains(MainActivity.context.getResources().getString(R.string.m))){
            //convert m to km
            Float walkingDistanceInMetres = Float.parseFloat(distanceString.replaceAll("[^0-9]",""));
            distanceInKm = walkingDistanceInMetres / 1000;
        }else{
            //already in kilometres
            //removes anything that is not a . or number
            String distanceClean = distanceString.replaceAll("[^.0-9]","");
            distanceInKm = Float.parseFloat(distanceClean.replaceAll("[.]$",""));
        }

        Log.i(TAG, "distanceInKm"+ distanceInKm);
        return distanceInKm;
    }

    private static int convertTimeToMinutes(String timeString){
        int totalTimeInMinutes, timeFromHoursAndMinutes = 0;
        if (timeString.contains(MainActivity.context.getResources().getString(R.string.hour))){
            int timeFromMinutes = 0;

            //convert hour to minutes
            int hours = Integer.parseInt(timeString.replaceAll(" "+MainActivity.context.getResources().getString(R.string.hour)+".*$",""));
            int timeFromHours = hours * 60;

            if (timeString.contains(MainActivity.context.getResources().getString(R.string.minute))){
                Log.i(TAG,"originalTime " + timeString);
                //if time also contains min, get the minutes
                String removeHours = timeString.replaceFirst(".*"+MainActivity.context.getResources().getString(R.string.hours),"").trim();
                Log.i(TAG, "WHAT LANGUAGE?: "+MainActivity.context.getResources().getString(R.string.hour));
                String removeHour = removeHours.replaceFirst(".*"+MainActivity.context.getResources().getString(R.string.hour),"").trim();
                Log.i(TAG,"removeHours " + removeHours + " " + removeHour);
                String removeMins = removeHour.replaceAll(MainActivity.context.getResources().getString(R.string.minutes)+".*$","").trim();
                int minutes = Integer.parseInt(removeMins.replaceAll(MainActivity.context.getResources().getString(R.string.minute)+".*$","").trim());
                timeFromMinutes = minutes;
            }
            totalTimeInMinutes = timeFromHours + timeFromMinutes;
        }else {
            //already in min
            totalTimeInMinutes = Integer.parseInt(timeString.replaceAll(MainActivity.context.getResources().getString(R.string.minutes)+".*$", "").trim());
        }

        //totalTimeInMinutes = timeFromHoursAndMinutes +  timeFromMinutes;

        Log.i(TAG, "totalTimeInMinutes"+ totalTimeInMinutes);
        return totalTimeInMinutes;
    }
}