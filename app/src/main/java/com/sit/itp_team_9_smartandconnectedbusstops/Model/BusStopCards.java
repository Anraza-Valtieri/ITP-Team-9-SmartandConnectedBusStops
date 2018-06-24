package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStopCards extends Card {
    private static final String TAG = BusStopCards.class.getSimpleName();
    String busStopID;
    String busStopName;
    String busStopDesc;
    String busStopLat;
    String busStopLong;
    String lastUpdated;
    boolean isFavorite;
    boolean majorUpdate = true;

    ArrayList<String> sortedKeys;

    // Create map to store
    Map<String, List<String>> busServices = new HashMap<>();
    // create list one and store values
    //2018-05-23T15:13:44+08:00
    List<String> busTiming = new ArrayList<>();

    public boolean isMajorUpdate() {
        return majorUpdate;
    }

    public void setMajorUpdate(boolean majorUpdate) {
        this.majorUpdate = majorUpdate;
    }

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
        sortByKeys();
    }

    public List<String> getBusTiming() {
        return busTiming;
    }

    public void setBusTiming(List<String> busTiming) {
        this.busTiming = busTiming;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public ArrayList<String> getSortedKeys() { return sortedKeys; }

    public void setSortedKeys(ArrayList<String> sortedKeys) { this.sortedKeys = sortedKeys; }

    public void sortByKeys(){
//        Log.d(TAG, "sortByKeys: Called");
        sortedKeys = new ArrayList<>(busServices.keySet());
        Collections.sort(sortedKeys);
    }

    @Override
    public String toString() {
        return "BusStopCards{" +
                "busStopID='" + busStopID + '\'' +
                ", busStopName='" + busStopName + '\'' +
                ", busStopDesc='" + busStopDesc + '\'' +
                ", busStopLat='" + busStopLat + '\'' +
                ", busStopLong='" + busStopLong + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", isFavorite=" + isFavorite +
                ", sortedKeys=" + sortedKeys +
                ", busServices=" + busServices +
                ", busTiming=" + busTiming +
                '}';
    }
}
