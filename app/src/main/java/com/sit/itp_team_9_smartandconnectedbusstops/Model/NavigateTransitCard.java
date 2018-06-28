package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

public class NavigateTransitCard extends Card {
    public final static int CCL_COLOR = Color.argb(255,244, 226, 66);
    public final static int NEL_COLOR = Color.argb(255,72, 35, 175);
    public final static int DTL_COLOR = Color.argb(255,1, 87, 155);
    public final static int EWL_COLOR = Color.argb(255,36, 130, 37);
    public final static int NSL_COLOR = Color.argb(255,244, 65, 65);
    public final static int WALKING_COLOR = Color.argb(255,120, 120, 120);
    public final static int BUS_COLOR = Color.argb(255,0, 0, 0);

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
    //private List<String> transitStations;
    private Map<String,List<Integer>> transitStations; //arrival stop, List<image resource(int),color(int)>
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

    public Map<String, List<Integer>> getTransitStations() {
        return transitStations;
    }

    public void setTransitStations(Map<String, List<Integer>> transitStations) {
        this.transitStations = transitStations;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
