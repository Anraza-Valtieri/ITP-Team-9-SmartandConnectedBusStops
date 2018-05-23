package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LTABusStopData {

    /*
    List of Results of Busstops
     */
    private List<LTABusStopData> results;

    private String busStopCode;
    private String roadName;
    private String description;
    private String busStopLat;
    private String busStopLong;

    // Create map to store
    private Map<String, List<String>> busServices = new HashMap<>();
    // create list one and store values
    private List<String> busTiming = new ArrayList<>();

    // Create map to store
    private Map<String, List<String>> busFirstLastServices = new HashMap<>();
    // create list one and store values
    private List<String> busFirstLastTiming = new ArrayList<>();

    public String getBusStopLat() {
        return busStopLat;
    }

    public void setBusStopLat(String busStopLat) {
        this.busStopLat = busStopLat;
    }

    public String getBusStopLong() {
        return busStopLong;
    }

    public void setBusStopLong(String busStopLong) {
        this.busStopLong = busStopLong;
    }

    public Map<String, List<String>> getBusServices() {
        return busServices;
    }

    public void setBusServices(Map<String, List<String>> busServices) {
        this.busServices = busServices;
    }

    public List<String> getBusTiming() {
        return busTiming;
    }

    public void setBusTiming(List<String> busTiming) {
        this.busTiming = busTiming;
    }

    public Map<String, List<String>> getBusFirstLastServices() {
        return busFirstLastServices;
    }

    public void setBusFirstLastServices(Map<String, List<String>> busFirstLastServices) {
        this.busFirstLastServices = busFirstLastServices;
    }

    public List<String> getBusFirstLastTiming() {
        return busFirstLastTiming;
    }

    public void setBusFirstLastTiming(List<String> busFirstLastTiming) {
        this.busFirstLastTiming = busFirstLastTiming;
    }

    public List<LTABusStopData> getResults() {
        return results;
    }

    public void setResults(List<LTABusStopData> results) {
        this.results = results;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LTABusStopData(String busStopCode, String roadName, String description) {
        this.busStopCode = busStopCode;
        this.roadName = roadName;
        this.description = description;
    }
}
