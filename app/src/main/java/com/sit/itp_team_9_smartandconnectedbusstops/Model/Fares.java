package com.sit.itp_team_9_smartandconnectedbusstops.Model;

public class Fares {

    private String BusMrt;
    private String ExpressBus;
    private String MrtBefore745;

    public Fares(String busMrt, String expressBus, String mrtBefore745) {
        this.BusMrt = busMrt;
        this.ExpressBus = expressBus;
        this.MrtBefore745 = mrtBefore745;
    }

    public String getBusMrt() {
        return BusMrt;
    }

    public String getExpressBus() {
        return ExpressBus;
    }

    public String getMrtBefore745() {
        return MrtBefore745;
    }
}
