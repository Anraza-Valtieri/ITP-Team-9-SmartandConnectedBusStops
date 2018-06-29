package com.sit.itp_team_9_smartandconnectedbusstops.Model;

public class AdultFares {

    private double BusMrt;
    private double ExpressBus;
    private double MrtBefore745;

    public AdultFares(double busMrt, double expressBus, double mrtBefore745) {
        BusMrt = busMrt;
        ExpressBus = expressBus;
        MrtBefore745 = mrtBefore745;
    }

    public double getBusMrt() {
        return BusMrt;
    }

    public double getExpressBus() {
        return ExpressBus;
    }

    public double getMrtBefore745() {
        return MrtBefore745;
    }
}
