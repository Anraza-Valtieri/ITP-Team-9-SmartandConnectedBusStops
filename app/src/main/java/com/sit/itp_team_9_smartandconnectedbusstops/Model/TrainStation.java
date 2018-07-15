package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.support.annotation.NonNull;

import org.json.JSONArray;

import java.util.List;

public class TrainStation implements Comparable<TrainStation> {
    private List<TrainStation> trainStations;
    private String stationName;
    private String stationNameChinese;
    private String stationCode;
    private String lineName;
    private String lineNameChinese;
    private int stationNum; //only numerical part of stationCode


    public List<TrainStation> getTrainStations() {
        return trainStations;
    }

    public void setTrainStations(List<TrainStation> trainStations) {
        this.trainStations = trainStations;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationNameChinese() {
        return stationNameChinese;
    }

    public void setStationNameChinese(String stationNameChinese) {
        this.stationNameChinese = stationNameChinese;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineNameChinese() {
        return lineNameChinese;
    }

    public void setLineNameChinese(String lineNameChinese) {
        this.lineNameChinese = lineNameChinese;
    }

    public int getStationNum() {
        return stationNum;
    }

    public void setStationNum(int stationNum) {
        this.stationNum = stationNum;
    }

    @Override
    public int compareTo(@NonNull TrainStation trainStation) {
        int compareStationNum= trainStation.getStationNum();
        return this.stationNum-compareStationNum; //ascending order
    }

    @Override
    public String toString() {
        return "TrainStation{" +
                //"trainStations=" + trainStations +
                ", stationName='" + stationName + '\'' +
                ", stationNameChinese='" + stationNameChinese + '\'' +
                ", stationCode='" + stationCode + '\'' +
                ", lineName='" + lineName + '\'' +
                ", lineNameChinese='" + lineNameChinese + '\'' +
                ", stationNum=" + stationNum +
                '}';
    }
}
