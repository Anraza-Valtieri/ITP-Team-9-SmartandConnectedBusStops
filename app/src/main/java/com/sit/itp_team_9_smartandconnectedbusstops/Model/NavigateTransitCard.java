package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.graphics.Color;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.JSONLTABusRoute;
import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.Value;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONTrainStationParser;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.FareDetails;

import java.util.ArrayList;
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
    private final static int WALKING_COLOR = Color.argb(255,120, 120, 120);
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
    private String startingStation;
    private String startingStationTimeTaken;
    private int imageViewStartingStation;
    private int imageViewStartingStationColor;
    private String departureStationCode;
    private String numStops;
    private List<String> inBetweenStops;
    private List<String> polyLines;
    private boolean isFavorite;
    //private List<String> transitStations;
    private Map<String,List<Object>> transitStations; //arrival stop, List<image resource(int),color(int),
                                                        // lineName(string), arrivalStop (string)>
    private String error;

    private List<String> inBetweenTrainStations;

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

    public List<String> getPolyLines() {
        return polyLines;
    }

    public void setPolyLines(List<String> polyLines) {
        this.polyLines = polyLines;
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

    /*public int getImageViewStartingStation() {
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /**
     * Sets and returns a NavigateTransitCard card
     * <p>
     * This method always returns immediately
     *
     * @param googleRoutesData GoogleRoutesData
     * @return card NavigateTransitCard
     */
    public static NavigateTransitCard getRouteData(GoogleRoutesData googleRoutesData) {
        NavigateTransitCard card = new NavigateTransitCard();
        card.setType(Card.NAVIGATE_TRANSIT_CARD);
        if (googleRoutesData.getError() == null || googleRoutesData.getError().isEmpty()){
            card.setID(googleRoutesData.getID());
            card.setTotalDistance(googleRoutesData.getTotalDistance());
            card.setTotalTime(googleRoutesData.getTotalDuration());
            
            //in Steps
            List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
            if (routeSteps != null) {
                Map<String,List<Object>> transitStations = new LinkedHashMap<>();
                List<List<Object>> timeTakenList = new ArrayList<>();
                List<TransitModeDistances> listOfTransitModeAndDistances = new ArrayList<>();
                List<String> listOfPolyLines = new ArrayList<>();
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
                    listOfPolyLines.add(routeSteps.get(i).getPolyline());
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
                            transitStations.put(routeSteps.get(i).getDistance(),walkingDetails);
                            break;

                        case "TRANSIT":
                            //train, lrt and bus
                            String trainLine = routeSteps.get(i).getTrainLine();
                            String lineName = null;
                            String lineShortForm = null;
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
                                        lineShortForm = "DT";
                                        break;
                                    case "North East Line":
                                        imageViewColor = NavigateTransitCard.NEL_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.NEL_COLOR);
                                        lineName = NavigateTransitCard.NORTH_EAST_LINE;
                                        lineShortForm = "NE";
                                        break;
                                    case "East West Line":
                                        imageViewColor = NavigateTransitCard.EWL_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.EWL_COLOR);
                                        lineName = NavigateTransitCard.EAST_WEST_LINE;
                                        lineShortForm = "EW";
                                        break;
                                    case "North South Line":
                                        imageViewColor = NavigateTransitCard.NSL_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.NSL_COLOR);
                                        lineName = NavigateTransitCard.NORTH_SOUTH_LINE;
                                        lineShortForm = "NS";
                                        break;
                                    case "Circle Line":
                                        imageViewColor = NavigateTransitCard.CCL_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.CCL_COLOR);
                                        lineName = NavigateTransitCard.CIRCLE_LINE;
                                        lineShortForm = "CC";
                                        break;
                                    case "Changi Airport Branch Line":
                                        imageViewColor = NavigateTransitCard.EWL_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.EWL_COLOR);
                                        lineName = NavigateTransitCard.CHANGI_AIRPORT_BRANCH_LINE;
                                        lineShortForm = "CG";
                                        break;
                                    case "Circle Line Extension":
                                        imageViewColor = NavigateTransitCard.CCL_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.CCL_COLOR);
                                        lineName = NavigateTransitCard.CIRCLE_LINE_EXTENSION;
                                        lineShortForm = "CE";
                                        break;
                                    default:
                                        //if lrt
                                        imageViewColor = NavigateTransitCard.LRT_COLOR;
                                        timeTakenEachStep.add(NavigateTransitCard.LRT_COLOR);
                                        lineName = NavigateTransitCard.LRT_LINE;
                                        trainLine = "LRT"; //for finding in between stations
                                        lineShortForm = "LRT";
                                        break;
                                }
                            }else{
                                //if bus
                                lineName = routeSteps.get(i).getBusNum();
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
                                        if (routeSteps.get(i).getDepartureStop().equalsIgnoreCase(busStopName)){
                                            //departureBusStopCode = busStopIDCode;
                                            departureBusStopCodeList.add(busStopIDCode);
                                            Log.i(TAG,"departure code list "+ departureBusStopCodeList);
                                        }
                                        if (routeSteps.get(i).getArrivalStop().equalsIgnoreCase(busStopName)){
                                            //arrivalBusStopCode = busStopIDCode;
                                            arrivalBusStopCodeList.add(busStopIDCode);
                                            Log.i(TAG,"arrival code list"+ arrivalBusStopCodeList);
                                        }
                                    }

                                    //bus route
                                    JSONLTABusRoute busRoute = new JSONLTABusRoute();
                                    Map<String, LinkedList<Value>> busMap = busRoute.getBusRouteMap();
                                    List<String> inBetweenBusStopCodes = new ArrayList<>();
                                    //Log.i(TAG,"busMap? " + busMap);
                                    for (Map.Entry<String, LinkedList<Value>> busMapEntry : busMap.entrySet()) {
                                        String busServiceNumber = busMapEntry.getKey();
                                        LinkedList<Value> busMapValue = busMapEntry.getValue();
                                        //int departureBusStopDirection = 0, arrivalBusStopDirection = 0;
                                        if (busServiceNumber.equalsIgnoreCase(lineName)) {
                                            //if found the correct bus service no.
                                            Log.i(TAG,"found the correct bus num!");
                                            Log.i(TAG,busMapValue.toString());

                                            OUTER_LOOP:
                                            for (int j = 0; j < busMapValue.size(); j++) {
                                                //loop through the bus service's bus stops (going through the bus route)
                                                for (String potentialArrivalBusStopCode : arrivalBusStopCodeList) {
                                                    Log.i(TAG, "arrival codes: "
                                                            + " " + potentialArrivalBusStopCode);
                                                    if (busMapValue.get(j).getBusStopCode()
                                                            .equals(potentialArrivalBusStopCode)) {
                                                        Log.i(TAG, "ONLY NEED ARRIVAL");
                                                        int arrivalBusDirection =
                                                                busMapValue.get(j).getDirection();
                                                        if (busMapValue.get(j - routeSteps.get(i)
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
                                                    Log.i(TAG,"no arrival");
                                                    //only departureBusStopCode found
                                                    if (busMapValue.get(j).getBusStopCode()
                                                            .equals(potentialDepartureBusStopCode)){
                                                        Log.i(TAG, "ONLY NEED DEPARTURE");
                                                        int departureBusDirection = busMapValue.get(j).getDirection();
                                                        if (busMapValue.get(j + routeSteps.get(i).getNumStops()) != null
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
                                    Log.i(TAG, "allTrainStationsInLine "+allTrainStationsInLine);

                                    //get departure station code
                                    String queryDeparture = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                            + routeSteps.get(i).getDepartureStop().replace("MRT Station","");
                                    List<TrainStation> departureTrainStation = lookUpTrainStations(queryDeparture);
                                    Log.i(TAG, "departureTrainStationCode "+departureTrainStation);

                                    //get arrival station code
                                    String queryArrival = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                            + routeSteps.get(i).getArrivalStop().replace("MRT Station","");
                                    List<TrainStation> arrivalTrainStation = lookUpTrainStations(queryArrival);
                                    Log.i(TAG,"arrivalStopStationCode "+arrivalTrainStation);

                                    if(allTrainStationsInLine != null){
                                        OUTER_TRAIN_LOOP:
                                        for (int j = 0; j < allTrainStationsInLine.size(); j++) {
                                            String confirmedDepartureStationCode = null, confirmedArrivalStationCode = null;
                                            int departureStationNum = 0 ,arrivalStationNum = 0;
                                            if (allTrainStationsInLine.get(j).getStationName().equals(routeSteps.get(i).getDepartureStop())){
                                                //if departureStationName found in list of all stations in that MRT line
                                                confirmedDepartureStationCode = allTrainStationsInLine.get(j).getStationCode();
                                                String departureStationNumString = confirmedDepartureStationCode.replaceAll("[^0-9]", "");
                                                if (!departureStationNumString.isEmpty()) {
                                                    departureStationNum = Integer.parseInt(departureStationNumString);
                                                }
                                            }
                                            if (allTrainStationsInLine.get(j).getStationName().equals(routeSteps.get(i).getArrivalStop())){
                                                //if arrivalStationName found in list of all stations in that MRT line
                                                confirmedArrivalStationCode = allTrainStationsInLine.get(j).getStationCode();
                                                String arrivalStationNumString = confirmedArrivalStationCode.replaceAll("[^0-9]", "");
                                                if (!arrivalStationNumString.isEmpty()) {
                                                    arrivalStationNum = Integer.parseInt(arrivalStationNumString);
                                                }
                                            }

                                            if (confirmedDepartureStationCode != null && confirmedArrivalStationCode != null){
                                                if (departureStationNum < arrivalStationNum){
                                                    //++ to add to trainStations
                                                    Log.i(TAG, "departureStationNum < arrivalStationNum");
                                                }
                                                else{
                                                    //-- to add to trainStations
                                                    Log.i(TAG, "departureStationNum > arrivalStationNum");
                                                }
                                            }else if(confirmedDepartureStationCode != null){
                                                Log.i(TAG, "departureStationNum != null");
                                                departureStationNum = Integer.parseInt(confirmedDepartureStationCode.replaceAll("[^0-9]", ""));
                                                int largerArrivalStationNum = departureStationNum + routeSteps.get(i).getNumStops();
                                                int smallerArrivalStationNum = departureStationNum - routeSteps.get(i).getNumStops();

                                                //get arrival station code
                                                String queryFindArrival = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                        + lineShortForm +largerArrivalStationNum;
                                                List<TrainStation> largerArrivalTrainStation = lookUpTrainStations(queryFindArrival);
                                                Log.i(TAG,"arrivalStopStationCode "+largerArrivalTrainStation);
                                                if (largerArrivalTrainStation != null){
                                                    //if larger arrival train station exists, get in between stops
                                                    for (int k = 1; k < routeSteps.get(i).getNumStops(); k++) {
                                                        String queryFindInBetween = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                                + lineShortForm + (departureStationNum + k);
                                                        List<TrainStation> inBetweenTrainStations = lookUpTrainStations(queryFindInBetween);
                                                        if (inBetweenTrainStations != null) {
                                                            trainStationNames.add(inBetweenTrainStations.get(0).getStationName());
                                                            Log.i(TAG, "confirmedDepartureStationCode != null + largerArrivalStop"+trainStationNames.toString());
                                                        }
                                                    }
                                                }else{
                                                    //if smaller arrival train station exists, get in between stops
                                                    for (int k = 0; k < routeSteps.get(i).getNumStops(); k++) {
                                                        String queryFindInBetween = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                                + lineShortForm + (departureStationNum - k);
                                                        List<TrainStation> inBetweenTrainStations = lookUpTrainStations(queryFindInBetween);
                                                        if (inBetweenTrainStations != null) {
                                                            trainStationNames.add(inBetweenTrainStations.get(0).getStationName());
                                                            Log.i(TAG, "confirmedDepartureStationCode != null + smallerArrivalStop"+trainStationNames.toString());
                                                        }
                                                    }
                                                }
                                                if (!trainStationNames.isEmpty()) {
                                                    break OUTER_TRAIN_LOOP;
                                                }
                                            }else{
                                                //confirmedArrivalStationCode != null
                                                if (confirmedArrivalStationCode != null) {
                                                    Log.i(TAG, "arrivalStationNum != null");
                                                    arrivalStationNum = Integer.parseInt(confirmedArrivalStationCode.replaceAll("[^0-9]", ""));
                                                    int largerDepartureStationNum = arrivalStationNum + routeSteps.get(i).getNumStops();
                                                    int smallerDepartureStationNum = arrivalStationNum - routeSteps.get(i).getNumStops();

                                                    //get arrival station code
                                                    String queryFindDeparture = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                            + lineShortForm + largerDepartureStationNum;
                                                    List<TrainStation> largerDepartureStation = lookUpTrainStations(queryFindDeparture);
                                                    Log.i(TAG, "arrivalStopStationCode " + largerDepartureStation);
                                                    if (largerDepartureStation != null) {
                                                        //if larger departure train station exists, get in between stops
                                                        for (int k = 1; k < routeSteps.get(i).getNumStops(); k++) {
                                                            String queryFindInBetween = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                                    + lineShortForm + (arrivalStationNum + k);
                                                            List<TrainStation> inBetweenTrainStations = lookUpTrainStations(queryFindInBetween);
                                                            if (inBetweenTrainStations != null) {
                                                                trainStationNames.add(0,inBetweenTrainStations.get(0).getStationName());
                                                                Log.i(TAG, "arrivalStationNum != null + largerDepartureStation" + trainStationNames.toString());

                                                            }
                                                        }
                                                    } else {
                                                        //if smaller departure train station exists, get in between stops
                                                        for (int k = 1; k < routeSteps.get(i).getNumStops(); k++) {
                                                            String queryFindInBetween = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                                    + lineShortForm + (arrivalStationNum - k);
                                                            List<TrainStation> inBetweenTrainStations = lookUpTrainStations(queryFindInBetween);
                                                            if (inBetweenTrainStations != null) {
                                                                trainStationNames.add(0,inBetweenTrainStations.get(0).getStationName());
                                                                Log.i(TAG, "arrivalStationNum != null" + "smallDepartureStation" + trainStationNames.toString());
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }

                                    /*if (departureTrainStation != null) {
                                        for (TrainStation potentialDepartureTrainStation : departureTrainStation) {
                                            String mrtLineName = potentialDepartureTrainStation.getLineName();
                                            if (mrtLineName.equalsIgnoreCase(trainLine)) {
                                                //if found the correct MRT line
                                                Log.i(TAG, "found the correct mrt line:" + mrtLineName);
                                                //query trainLine
                                                String queryLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                        + mrtLineName;
                                                List<TrainStation> allStationsInLine = lookUpTrainStations(queryArrival);
                                                if (allStationsInLine != null) {
                                                    for (int j = 0; j < allStationsInLine.size(); j++) {
                                                        //loop through all stations in line
                                                        if (allStationsInLine.get(j).getStationCode()
                                                                .equals(potentialDepartureTrainStation.getStationCode())) {
                                                            //found departureStop
                                                            for (int k = 0; k < routeSteps.get(j).getNumStops(); k++) {
                                                                if (allStationsInLine.get(j + k).getStationCode().equals()) {
                                                                    trainStationNames.add(allStationsInLine.get(j + k).getStationName());
                                                                    Log.i(TAG, "inBetweenTrainStationNames"
                                                                            + trainStationNames);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }else {
                                        //only arrival train found
                                        if (allStationsInLine.get(j).getBusStopCode()
                                                .equals(potentialDepartureTrainStation)) {
                                            Log.i(TAG, "ONLY NEED ARRIVAL");
                                            int departureBusDirection = busMapValue.get(j).getDirection();
                                            if (busMapValue.get(j + routeSteps.get(i).getNumStops()) != null
                                                    && busMapValue.get(j + routeSteps.get(i).getNumStops())
                                                    .getDirection() == departureBusDirection) {
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
                                    }*/
                                    /*if (departureTrainStationCode != null && arrivalTrainStationCode != null) {
                                        OUTER_TRAIN_LOOP:
                                        for (TrainStation potentialDepartureTrainStation : departureTrainStationCode) {
                                            String departureLine = potentialDepartureTrainStation.getLineName();
                                            for (TrainStation potentialArrivalTrainStation : arrivalTrainStationCode) {
                                                String arrivalLine = potentialArrivalTrainStation.getLineName();
                                                if (departureLine.equals(arrivalLine)) {
                                                    //find MRT line
                                                    String trainLineName = departureLine;
                                                    String queryAllTrainStationsInLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                            + trainLineName;

                                                    int departureTrainStationNum = Integer.parseInt(potentialDepartureTrainStation.getStationCode().replaceAll("[^0-9]", ""));
                                                    int arrivalTrainStationNum = Integer.parseInt(potentialDepartureTrainStation.getStationCode().replaceAll("[^0-9]", ""));

                                                    //save all train stations in that MRT line
                                                    List<TrainStation> allTrainStationsCode = lookUpTrainStations(queryAllTrainStationsInLine);
                                                    if (allTrainStationsCode != null) {
                                                        if (departureTrainStationNum < arrivalTrainStationNum) {
                                                            for (int j = 0; j < allTrainStationsCode.size(); j++) {
                                                                trainStationNames.add(allTrainStationsCode.get(j).getStationName());
                                                                Log.i(TAG, "trainStationNames: " + trainStationNames);
                                                            }
                                                        } else {
                                                            for (int j = allTrainStationsCode.size() - 1; j > 0; j--) {
                                                                trainStationNames.add(allTrainStationsCode.get(j).getStationName());
                                                                Log.i(TAG, "trainStationNames: " + trainStationNames);
                                                            }
                                                        }

                                                    }
                                                }
                                                break OUTER_TRAIN_LOOP;
                                            }
                                        }
                                    }*/
                                            //no arrival
                                            /*String queryAllTrainStationsInLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                    + departureLine;
                                            List<TrainStation> allTrainStationsCode = lookUpTrainStations(queryAllTrainStationsInLine);
                                            if (allTrainStationsCode.get(j).getBusStopCode()
                                                    .equals(potentialDepartureTrainStation)){
                                                Log.i(TAG, "ONLY NEED DEPARTURE");
                                                if (busMapValue.get(j + routeSteps.get(i).getNumStops()) != null)
                                                {
                                                    //if arrival stop is not null
                                                    for (int k = 1; k < routeSteps.get(i).getNumStops(); k++) {
                                                        inBetweenBusStopCodes.add(busMapValue
                                                                .get(j + k).getBusStopCode());
                                                        Log.i(TAG, "inBetweenBusStopCodes"
                                                                + inBetweenBusStopCodes);
                                                    }
                                                    //break;
                                                }

                                            }*/

                                        //get train line
                                        //String trainLineName = departureTrainStationCode.replaceAll("[0-9]", "");
                                        /*String queryAllTrainStationsInLine = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                                + trainLineName;

                                        //save all train stations in that MRT line
                                        List<TrainStation> allTrainStationsCode = lookUpTrainStations(queryAllTrainStationsInLine);

                                        int departureTrainStationNum = Integer.parseInt(departureTrainStationCode.replaceAll("[^0-9]", ""));
                                        int arrivalTrainStationNum = Integer.parseInt(departureTrainStationCode.replaceAll("[^0-9]", ""));

                                        //if departure < arrival
                                        if (allTrainStationsCode != null) {
                                            if (departureTrainStationNum < arrivalTrainStationNum) {
                                                for (int j = 0; j < allTrainStationsCode.size(); j++) {
                                                    trainStationNames.add(allTrainStationsCode.get(j).getStationName());
                                                }
                                            } else {
                                                //if departure > arrival
                                                for (int j = allTrainStationsCode.size()-1; j > 0; j--) {
                                                    trainStationNames.add(allTrainStationsCode.get(j).getStationName());
                                                }
                                            }
                                        }*/

                                    /*String queryDeparture = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                            + routeSteps.get(i).getDepartureStop();
                                    lookUpTrainStationCode(queryDeparture);
                                    String departureStationCodeRef = NavigateTransitCard.getDepartureStationCode();
                                    Log.d(TAG,"departureTrainStationCode " + departureStationCodeRef);

                                    String queryArrival = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                            + routeSteps.get(i).getArrivalStop();

                                    int intValueDeparture = Integer.parseInt(departureStationCodeRef.replaceAll("[^0-9]", ""));
                                    int intValueArrival = Integer.parseInt(queryArrival.replaceAll("[^0-9]", ""));
                                    String trainLineName = departureStationCodeRef.replaceAll("[0-9]", "");

                                    String trainLineQuery = "https://data.gov.sg/api/action/datastore_search?resource_id=65c1093b-0c34-41cf-8f0e-f11318766298&q="
                                            + trainLineName; //get all stops in that line

                                    List<TrainStation> allTrainStationsInLine = lookUpAllStationsInLine(trainLineQuery);
                                    //List<String> inBetweenStationsNames = new ArrayList<>();
                                    if (intValueDeparture < intValueArrival){
                                        for (int j = intValueDeparture; j < allTrainStationsInLine.size(); j++){
                                            trainStationNames.add(allTrainStationsInLine.get(j).getStationName());
                                        }
                                    }else {
                                        for (int j = intValueArrival; j <allTrainStationsInLine.size(); j--) {
                                            trainStationNames.add(allTrainStationsInLine.get(j).getStationName());
                                        }
                                    }
                                    //using trainStationCode, either ++ or -- to arrivalStop*/
                                    //trainStationNames.add("Hello");
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

                    transitDistance += Double.valueOf(listOfTransitModeAndDistances.get(i).getDistance().replaceAll("[^.0-9]+", ""));
                }

                FareDetails fareDetails = new FareDetails();
                fareDetails.populateAdultFareDistance();
                fareDetails.populateAdultFaresMap();

                String price = "";

                if(transitDistance > 0.0) {
                    for(int i = 0; i < fareDetails.getAdultFareDistance().size(); i++) {

                        if(i == 0) {
                            if(transitDistance <= fareDetails.getAdultFareDistance().get(0)) {
                                price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(0)).getBusMrt();
                            }
                        }
                        else {
                            if(transitDistance > fareDetails.getAdultFareDistance().get(i-1) && transitDistance <= fareDetails.getAdultFareDistance().get(i)) {
                                price = "$" + fareDetails.getAdultFaresMap().get(fareDetails.getAdultFareDistance().get(i)).getBusMrt();
                            }
                        }

                    }
                }
                card.setPolyLines(listOfPolyLines);
                Log.i(TAG,listOfPolyLines.toString());
                card.setCost(price);
                card.setTimeTaken(timeTakenList);
                card.setTransitStations(transitStations);
            }
        }else{
            card.setError(googleRoutesData.getError());
        }
        return card;
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

    private static List<String> lookUpTrainStationCode(String query){
        List<String> trainStationsQuery = new ArrayList<>();
        trainStationsQuery.add(query);
        Log.i(TAG,"lookUpTrainStationCode: "+trainStationsQuery.toString());
        JSONTrainStationParser trainStationParser = new JSONTrainStationParser(trainStationsQuery);

        List<TrainStation> result; //result from parser
        List<String> trainStationCode = new ArrayList<>();
        try {
            result = trainStationParser.execute().get();
            Log.d(TAG, query);
            if (result.size() <= 0) {
                Log.d(TAG, "trainStation: Data.gov returned no data");
                return null;
            }else{
                //got result
                Log.d(TAG, result.get(0).getStationCode());
                for (TrainStation trainStation : result){
                    trainStationCode.add(trainStation.getStationCode());
                }
                return trainStationCode;
                //NavigateTransitCard navigateTransitCard = new NavigateTransitCard();
                //navigateTransitCard.setDepartureStationCode(result.get(0).getStationCode()); //departure train station code
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private static List<TrainStation> lookUpAllStationsInLine(String query){
        List<String> trainStationsQuery = new ArrayList<>();
        List<TrainStation> trainStationsResult = new ArrayList<>();
        trainStationsQuery.add(query);
        Log.i(TAG,trainStationsQuery.toString());
        JSONTrainStationParser trainStationParser = new JSONTrainStationParser(trainStationsQuery);
        List<TrainStation> result; //result from parser
        try {
            result = trainStationParser.execute().get();
            Log.d(TAG, query);
            if (result.size() <= 0) {
                Log.d(TAG, "trainStation: Data.gov returned no data");
                return null;
            }else{
                //got result
                for (int i = 0; i < result.size(); i++) {
                    trainStationsResult.add(result.get(i));
                }
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return trainStationsResult;
    }*/
}
