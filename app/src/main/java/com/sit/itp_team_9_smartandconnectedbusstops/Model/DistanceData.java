package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.List;

public class DistanceData {
    private List<DistanceData> results;

    private String startAdd;
    private String endLat;
    private String endLng;
    private String endAdd;
    private String distance;
    private String duration;
    private String duration_in_traffic;

    public List<DistanceData> getInfo() {
        return results;
    }

    public void setInfo(List<DistanceData> results) {
        this.results = results;
    }

    public String getEndAdd() {
        return endAdd;
    }

    public void setEndAdd(String endAdd) {
        this.endAdd = endAdd;
    }

    public String getStartAdd() {
        return startAdd;
    }

    public void setStartAdd(String startAdd) {
        this.startAdd = startAdd;
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

    public String getDuration_in_traffic() {
        return duration_in_traffic;
    }

    public void setDuration_in_traffic(String duration_in_traffic) {
        this.duration_in_traffic = duration_in_traffic;
    }

    public List<DistanceData> getDetails(){ return results;}

    }
