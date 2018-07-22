package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import org.json.JSONArray;

import java.util.List;

public class GoogleRoutesData {
    public List<GoogleRoutesData> routes;
    private int ID;
    private String copyrights;
    private JSONArray warnings;
    private String summary;
    private String placeidStart, placeidEnd, routeID;
    boolean isFavorite;

    //public JSONArray legs;

    //within legs
    private String totalDistance;
    private String totalDuration;
    //end_address, start_address of whole journey?
    private List<GoogleRoutesSteps> steps;

    private double totalBusDistance;
    private double totalTrainDistance;

    //for no results found
    private String error;

    public List<GoogleRoutesData> getRoutes() {
        return routes;
    }

    public void setRoutes(List<GoogleRoutesData> routes) {
        this.routes = routes;
    }


    /*public JSONArray getLegs() {
        return legs;
    }

    public void setLegs(JSONArray legs) {
        this.legs = legs;
    }*/

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public JSONArray getWarnings() {
        return warnings;
    }

    public void setWarnings(JSONArray warnings) {
        this.warnings = warnings;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<GoogleRoutesSteps> getSteps() {
        return steps;
    }

    public void setSteps(List<GoogleRoutesSteps> steps) {
        this.steps = steps;
    }

    public double getTotalBusDistance() {
        return totalBusDistance;
    }

    public void setTotalBusDistance(double totalBusDistance) {
        this.totalBusDistance = totalBusDistance;
    }

    public double getTotalTrainDistance() {
        return totalTrainDistance;
    }

    public void setTotalTrainDistance(double totalTrainDistance) {
        this.totalTrainDistance = totalTrainDistance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
