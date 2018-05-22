package com.sit.itp_team_9_smartandconnectedbusstops;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LTABusStopData {

    /*
    List of Results of Busstops
     */
    @SerializedName("value")
    public List<LTABusStopData> results;

    @SerializedName("BusStopCode")
    public String busStopCode;
    @SerializedName("RoadName")
    public String roadName;
    @SerializedName("Description")
    public String description;

//    public String query;

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

//    public String getQuery() {
//        return query;
//    }
//
//    public void setQuery(String query) {
//        this.query = query;
//    }
}
