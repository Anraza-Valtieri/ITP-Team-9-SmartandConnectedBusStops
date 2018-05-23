package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.List;

public class GoogleBusStopData {
    public List<GoogleBusStopData> results;

    public String lat;

    public String lng;

    public String name;

    public String placeId;

    public String icon;

    public String busStopID;

    public String getBusStopID() {
        return busStopID;
    }

    public void setBusStopID(String busStopID) {
        this.busStopID = busStopID;
    }

    public List<GoogleBusStopData> getResults() {
        return results;
    }

    public void setResults(List<GoogleBusStopData> results) {
        this.results = results;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
