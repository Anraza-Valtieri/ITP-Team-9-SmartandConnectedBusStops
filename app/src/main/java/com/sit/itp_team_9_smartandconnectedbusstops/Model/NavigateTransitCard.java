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
    private static String departureStationCode;
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

    public static String getDepartureStationCode() {
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
                                List<String> trainStationNames = new ArrayList<>();
                                if (imageViewTransit == R.drawable.ic_directions_bus_black_24dp) {
                                    Log.i(TAG,"is this a bus?");

                                    //get departure stop's code
                                    Map<String, String> allBusByIdMap = MainActivity.allBusByID;
                                    String departureBusStopCode = null, arrivalBusStopCode = null;
                                    for (Map.Entry<String, String> entry : allBusByIdMap.entrySet()) {
                                        String busStopIDCode = entry.getKey();
                                        Log.i(TAG,"busStopIDCode "+busStopIDCode);
                                        String busStopName = entry.getValue();
                                        //Log.i(TAG,"busStopName "+busStopName);
                                        if (busStopName.equals(routeSteps.get(i).getDepartureStop())){
                                            departureBusStopCode = busStopIDCode;
                                            Log.i(TAG,"departure code "+ departureBusStopCode);
                                        } else if (busStopName.equals(routeSteps.get(i).getArrivalStop())){
                                            arrivalBusStopCode = busStopIDCode;
                                            Log.i(TAG,"departure code "+ arrivalBusStopCode);
                                        }
                                    }

                                    //go into linkedlist to get the next X stops
                                    List<String> busStopCodes = new ArrayList<>();
                                    JSONLTABusRoute busRoute = new JSONLTABusRoute();
                                    Map<String, LinkedList<Value>> busMap = busRoute.getBusRouteMap();
                                    //Log.i(TAG,"busMap? " + busMap);
                                    for (Map.Entry<String, LinkedList<Value>> busMapEntry : busMap.entrySet()) {
                                        String busServiceNumber = busMapEntry.getKey();
                                        LinkedList<Value> busMapValue = busMapEntry.getValue();
                                        if (busServiceNumber.equals(lineName)) {
                                            //get direction from departureStop Value
                                            for (int busMapIndex = 0; busMapIndex < busMapValue.size(); busMapIndex++) {
                                                //int stopSequence = busMapValue.get(busMapIndex).getStopSequence();
                                                //Log.i(TAG,"stopSequence: "+ stopSequence);
                                                //LinkedList<Value> busValue = busMap.get(lineName);
                                                if (busMapValue.get(busMapIndex).getBusStopCode().equals(departureBusStopCode)) {
                                                    //if departure stop is found, save the next X num of stops
                                                    int departureStopDirection = busMapValue.get(busMapIndex).getDirection();
                                                    int numStopInt = Integer.parseInt(card.getNumStops());
                                                    for (int k = 1; k < Integer.parseInt(card.getNumStops()); k++){
                                                        int direction = busMapValue.get(busMapIndex+k).getDirection();
                                                        if (direction == departureStopDirection){
                                                            busStopCodes.add(busMapValue.get(busMapIndex+k).getBusStopCode());
                                                            //add until arrivalStop
                                                        }else{
                                                            //error?
                                                        }
                                                    }
                                                    /*if ((busMapIndex+numStopInt) < busMapValue.size()
                                                            && busMapValue.get(busMapIndex+numStopInt)
                                                            .getBusStopCode().equals(arrivalBusStopCode)) {
                                                        //if arrivalStop is found before end of busMapValue
                                                        for (int k = 1; k < Integer.parseInt(card.getNumStops()); k++) {
                                                            busStopCodes.add(busMapValue.get(busMapIndex+k).getBusStopCode());
                                                            Log.i(TAG,"+k = "+(busMapIndex+k));

                                                        }
                                                    }/*else{
                                                        for (int k = 1; k < numStopInt; k++) {
                                                            busStopCodes.add(busMapValue.get(busMapIndex-k).getBusStopCode());
                                                            Log.i(TAG,"-k = "+(busMapIndex-k));
                                                        }
                                                        //busStopCodes.add(busMapValue.get(busMapIndex+k).getBusStopCode());
                                                        Log.i(TAG,"numStops="+card.getNumStops());
                                                        Log.i(TAG,"busMapValue = "+busMapValue.size());
                                                        //TODO sometimes Index out of bounds exception: direction?
                                                        //TODO busRoute cannot be static
                                                        //check for arrivalStop, if null, use direction 2?
                                                        //Log.d(TAG, "busStopCodes:" + busStopCodes);
                                                    }*/
                                                }
                                            }
                                        }
                                    }

                                    //get bus stop names
                                    for (int k = 0; k < busStopCodes.size(); k++){
                                        //Find names of all in busStopCodes
                                        // Key: Bus stop ID Value: Bus stop name
                                        String busStopName = allBusByIdMap.get(busStopCodes.get(k));
                                        busStopNames.add(busStopName);
                                        Log.i(TAG,"Bus stop name (add 1): "+busStopName);
                                    }
                                }else{
                                    //train stations
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
                                    trainStationNames.add("Hello");
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

    private void lookUpTrainStationNames(String query){
        List<String> trainStationsQuery = new ArrayList<>();
        trainStationsQuery.add(query);
        Log.i(TAG,trainStationsQuery.toString());
        JSONTrainStationParser trainStationParser = new JSONTrainStationParser(trainStationsQuery);
        List<TrainStation> result; //result from parser
        try {
            result = trainStationParser.execute().get();
            Log.d(TAG, query);
            if (result.size() <= 0) {
                Log.d(TAG, "trainStation: Data.gov returned no data");
                return;
            }else{
                //got result
                for (int i = 0; i < result.size(); i++) {
                    inBetweenTrainStations.add(result.get(i).getStationName());
                }
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void lookUpTrainStationCode(String query){
        List<String> trainStationsQuery = new ArrayList<>();
        trainStationsQuery.add(query);
        Log.i(TAG,trainStationsQuery.toString());
        JSONTrainStationParser trainStationParser = new JSONTrainStationParser(trainStationsQuery);
        List<TrainStation> result; //result from parser
        try {
            result = trainStationParser.execute().get();
            Log.d(TAG, query);
            if (result.size() <= 0) {
                Log.d(TAG, "trainStation: Data.gov returned no data");
                return;
            }else{
                //got result
                Log.d(TAG, result.get(0).getStationCode());
                NavigateTransitCard navigateTransitCard = new NavigateTransitCard();
                navigateTransitCard.setDepartureStationCode(result.get(0).getStationCode()); //departure train station code
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static List<TrainStation> lookUpAllStationsInLine(String query){
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
    }
}
