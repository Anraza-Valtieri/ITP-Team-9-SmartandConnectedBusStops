package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import org.json.JSONArray;

import java.util.List;

public class GoogleRoutesData {
    public List<GoogleRoutesData> routes;

    private String copyrights;

    private JSONArray warnings;

    //public JSONArray legs;

    //within legs
    private String totalDistance;
    private String totalDuration;
    //end_address, start_address of whole journey?
    private List<GoogleRoutesSteps> steps;

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
}
