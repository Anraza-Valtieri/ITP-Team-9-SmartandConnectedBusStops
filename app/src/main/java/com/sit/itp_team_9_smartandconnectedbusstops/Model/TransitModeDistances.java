package com.sit.itp_team_9_smartandconnectedbusstops.Model;

public class TransitModeDistances {

    private String BusSubWay;
    private String BusNoMrtLine;
    private String Distance;

    public TransitModeDistances(String busSubWay, String busNoMrtLine, String distance) {
        this.BusSubWay = busSubWay;
        BusNoMrtLine = busNoMrtLine;
        Distance = distance;
    }

    public String getBusSubWay() {
        return BusSubWay;
    }

    public String getBusNoMrtLine() {
        return BusNoMrtLine;
    }

    public String getDistance() {
        return Distance;
    }
}
