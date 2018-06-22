package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class NavigateTransitCard extends Card {
    private int ID;
    private String totalTime;
    private String cost;
    private String totalDistance;
    private String startingStation;
    private String timeTaken; //breakdown bar based on this
    private String numStops;
    private String transferStation;
    private String endingStation;
    private int imageViewStartingStation;
    private int imageViewStartingStationColor;
    private int imageViewTransferStation;
    private int imageViewEndingStation;
    private int imageViewEndingStationColor;



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

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getNumStops() {
        return numStops;
    }

    public void setNumStops(String numStops) {
        this.numStops = numStops;
    }

    public String getTransferStation() {
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
    }

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

    public int getImageViewTransferStation() {
        return imageViewTransferStation;
    }

    public void setImageViewTransferStation(int imageViewTransferStation) {
        this.imageViewTransferStation = imageViewTransferStation;
    }

    public int getImageViewEndingStation() {
        return imageViewEndingStation;
    }

    public void setImageViewEndingStation(int imageViewEndingStation) {
        this.imageViewEndingStation = imageViewEndingStation;
    }

    public int getImageViewEndingStationColor() {
        return imageViewEndingStationColor;
    }

    public void setImageViewEndingStationColor(int imageViewEndingStationColor) {
        this.imageViewEndingStationColor = imageViewEndingStationColor;
    }
}
