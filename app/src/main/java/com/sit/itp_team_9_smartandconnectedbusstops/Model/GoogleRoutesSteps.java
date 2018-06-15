package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.List;

public class GoogleRoutesSteps {
    private List<String> steps;

    //within steps
    private String distance;
    private String duration;
    private String htmlInstructions;
    private String travelMode;

    //in expandable list
    private List<GoogleRoutesSteps> detailedSteps; //WALKING, step-by-step turns to take etc.
    private String trainLine; //TRANSIT, MRT line
    private String departureStop;
    private String arrivalStop;
    private String busNum;
    private Integer numStops; //TRANSIT, number of stops

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    //WALKING
    public List<GoogleRoutesSteps> getDetailedSteps() {
        return detailedSteps;
    }

    public void setDetailedSteps(List<GoogleRoutesSteps> detailedSteps) {
        this.detailedSteps = detailedSteps;
    }

    //TRANSIT
    //train
    public String getTrainLine() {
        return trainLine;
    }

    public void setTrainLine(String trainLine){
        this.trainLine = trainLine;
    }

    public void setDepartureStop(String departureStop){
        this.departureStop = departureStop;
    }

    public String getDepartureStop(){
        return departureStop;
    }

    public void setArrivalStop(String arrivalStop){
        this.arrivalStop = arrivalStop;
    }

    public String getArrivalStop(){
        return arrivalStop;
    }

    //bus
    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum){
        this.busNum = busNum;
    }

    public Integer getNumStops() {
        return numStops;
    }

    public void setNumStops(Integer numStops){
        this.numStops = numStops;
    }

}
