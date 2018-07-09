package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.graphics.Color;
import android.util.Log;

import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.FareDetails;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public static NavigateTransitCard getRouteData(GoogleRoutesData googleRoutesData) {
        NavigateTransitCard card = new NavigateTransitCard();
        card.setType(Card.NAVIGATE_TRANSIT_CARD);
        if (googleRoutesData.getError() == null || googleRoutesData.getError().isEmpty()){
            card.setID(googleRoutesData.getID());
            card.setTotalDistance(googleRoutesData.getTotalDistance());
            card.setTotalTime(googleRoutesData.getTotalDuration());

            //Jeremy's part, do not remove first
            //can work
        /*db.collection("adult")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String cost = "a";
                if (task.isSuccessful()) {
                    //Log.d(TAG, "WE ARE HERE");
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        //String intValue = googleRoutesData.getTotalDistance().replaceAll("[^0-9]", "");
                        if(Double.valueOf(googleRoutesData.getTotalDistance().substring(0, googleRoutesData.getTotalDistance().length() - 3)) < Double.valueOf(doc.getId())) {
                            Log.d(TAG, "HElo" + googleRoutesData.getTotalDistance().substring(0, googleRoutesData.getTotalDistance().length() - 3));
                            Log.d(TAG, "HElo" + doc.getId());
                            Log.d(TAG, "HElo" + doc.getDouble("BusMrt"));
                            cost = String.valueOf(doc.getDouble("BusMrt"));

                            break;
                        }

                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                card.setCost(cost);
            }
        });*/

            FareDetails fareDetails = new FareDetails();
            fareDetails.populateMap();

            for(Map.Entry<Double, AdultFares> entry : fareDetails.getAdultFaresMap().entrySet()) {
                if(Double.valueOf(googleRoutesData.getTotalDistance().substring(0, googleRoutesData.getTotalDistance().length() - 3)) < entry.getKey()) {
                    card.setCost("$".concat(String.valueOf(entry.getValue().getBusMrt())));
                    break;
                }
            }

            //in Steps
            List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
            if (routeSteps != null) {
                Map<String,List<Object>> transitStations = new LinkedHashMap<>();
                List<List<Object>> timeTakenList = new ArrayList<>();

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
                            String trainLine = routeSteps.get(i).getTrainLine();
                            String lineName = null;
                            int imageViewTransit;
                            int imageViewColor;
                            if (trainLine != null) {
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
                                List<Object> stationDetails = new ArrayList<>();
                                stationDetails.add(imageViewTransit);
                                stationDetails.add(imageViewColor);
                                stationDetails.add(lineName);
                                stationDetails.add(routeSteps.get(i).getArrivalStop());
                                transitStations.put(routeSteps.get(i).getDepartureStop(),stationDetails);
                            }
                            break;
                    }
                    timeTakenList.add(timeTakenEachStep);
                }
                card.setTimeTaken(timeTakenList);
                card.setTransitStations(transitStations);
            }
        }else{
            card.setError(googleRoutesData.getError());
        }
        return card;
    }
}
