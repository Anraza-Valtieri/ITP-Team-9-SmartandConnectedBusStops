package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStopCards {
    String busStopID;
    String busStopName;
    String busStopDesc;
    String busStopLat;
    String busStopLong;
    String lastUpdated;
    boolean isFavorite;

    // Create map to store
    Map<String, List<String>> busServices = new HashMap<>();
    // create list one and store values
    //2018-05-23T15:13:44+08:00
    List<String> busTiming = new ArrayList<>();

    // yyyy-MM-dd'T'HH:mm:ss.SSSXXX
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getBusStopID() {
        return busStopID;
    }

    public void setBusStopID(String busStopID) {
        this.busStopID = busStopID;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public String getBusStopDesc() {
        return busStopDesc;
    }

    public void setBusStopDesc(String busStopDesc) {
        this.busStopDesc = busStopDesc;
    }

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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    /*
    So with this we will store bus timings into the list.
    Map the bus service number to the list in Bus Services

    busTiming.add("2min");
    busTiming.add("4min");
    busTiming.add("6min");
    busServices.put("113", busTiming);

    To read it
    // iterate and display values
        System.out.println("Fetching Keys and corresponding [Multiple] Values n");
        for (Map.Entry<String, List<String>> entry : busServices.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            System.out.println("Key = " + key);
            System.out.println("Values = " + values + "n");
        }
     */
}
