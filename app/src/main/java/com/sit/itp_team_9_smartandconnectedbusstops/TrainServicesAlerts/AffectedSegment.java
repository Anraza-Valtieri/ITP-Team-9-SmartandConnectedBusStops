
package com.sit.itp_team_9_smartandconnectedbusstops.TrainServicesAlerts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffectedSegment {

    @SerializedName("Line")
    @Expose
    private String line;
    @SerializedName("Direction")
    @Expose
    private String direction;
    @SerializedName("Stations")
    @Expose
    private String stations;
    @SerializedName("FreePublicBus")
    @Expose
    private String freePublicBus;
    @SerializedName("FreeMRTShuttle")
    @Expose
    private String freeMRTShuttle;
    @SerializedName("MRTShuttleDirection")
    @Expose
    private String mRTShuttleDirection;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStations() {
        return stations;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    public String getFreePublicBus() {
        return freePublicBus;
    }

    public void setFreePublicBus(String freePublicBus) {
        this.freePublicBus = freePublicBus;
    }

    public String getFreeMRTShuttle() {
        return freeMRTShuttle;
    }

    public void setFreeMRTShuttle(String freeMRTShuttle) {
        this.freeMRTShuttle = freeMRTShuttle;
    }

    public String getMRTShuttleDirection() {
        return mRTShuttleDirection;
    }

    public void setMRTShuttleDirection(String mRTShuttleDirection) {
        this.mRTShuttleDirection = mRTShuttleDirection;
    }

}
