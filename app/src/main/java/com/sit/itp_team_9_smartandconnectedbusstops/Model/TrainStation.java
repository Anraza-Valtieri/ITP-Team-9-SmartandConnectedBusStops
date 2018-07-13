package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import org.json.JSONArray;

import java.util.List;

public class TrainStation {
    private List<TrainStation> trainStations;
    private String stationName;
    private String stationNameChinese;
    private String stationCode;
    private String lineName;
    private String lineNameChinese;


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
}
