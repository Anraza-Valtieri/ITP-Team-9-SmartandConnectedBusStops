
package com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("ServiceNo")
    @Expose
    private String serviceNo;
    @SerializedName("Operator")
    @Expose
    private String operator;
    @SerializedName("Direction")
    @Expose
    private Integer direction;
    @SerializedName("StopSequence")
    @Expose
    private Integer stopSequence;
    @SerializedName("BusStopCode")
    @Expose
    private String busStopCode;
    @SerializedName("Distance")
    @Expose
    private Double distance;
    @SerializedName("WD_FirstBus")
    @Expose
    private String wDFirstBus;
    @SerializedName("WD_LastBus")
    @Expose
    private String wDLastBus;
    @SerializedName("SAT_FirstBus")
    @Expose
    private String sATFirstBus;
    @SerializedName("SAT_LastBus")
    @Expose
    private String sATLastBus;
    @SerializedName("SUN_FirstBus")
    @Expose
    private String sUNFirstBus;
    @SerializedName("SUN_LastBus")
    @Expose
    private String sUNLastBus;

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getWDFirstBus() {
        return wDFirstBus;
    }

    public void setWDFirstBus(String wDFirstBus) {
        this.wDFirstBus = wDFirstBus;
    }

    public String getWDLastBus() {
        return wDLastBus;
    }

    public void setWDLastBus(String wDLastBus) {
        this.wDLastBus = wDLastBus;
    }

    public String getSATFirstBus() {
        return sATFirstBus;
    }

    public void setSATFirstBus(String sATFirstBus) {
        this.sATFirstBus = sATFirstBus;
    }

    public String getSATLastBus() {
        return sATLastBus;
    }

    public void setSATLastBus(String sATLastBus) {
        this.sATLastBus = sATLastBus;
    }

    public String getSUNFirstBus() {
        return sUNFirstBus;
    }

    public void setSUNFirstBus(String sUNFirstBus) {
        this.sUNFirstBus = sUNFirstBus;
    }

    public String getSUNLastBus() {
        return sUNLastBus;
    }

    public void setSUNLastBus(String sUNLastBus) {
        this.sUNLastBus = sUNLastBus;
    }

    @Override
    public String toString() {
        return "Value{" +
                "serviceNo='" + serviceNo + '\'' +
                ", operator='" + operator + '\'' +
                ", direction=" + direction +
                ", stopSequence=" + stopSequence +
                ", busStopCode='" + busStopCode + '\'' +
                ", distance=" + distance +
                ", wDFirstBus='" + wDFirstBus + '\'' +
                ", wDLastBus='" + wDLastBus + '\'' +
                ", sATFirstBus='" + sATFirstBus + '\'' +
                ", sATLastBus='" + sATLastBus + '\'' +
                ", sUNFirstBus='" + sUNFirstBus + '\'' +
                ", sUNLastBus='" + sUNLastBus + '\'' +
                '}';
    }
}
