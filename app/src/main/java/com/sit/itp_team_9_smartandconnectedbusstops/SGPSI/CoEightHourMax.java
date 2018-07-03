
package com.sit.itp_team_9_smartandconnectedbusstops.SGPSI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoEightHourMax {

    @SerializedName("west")
    @Expose
    private Double west;
    @SerializedName("national")
    @Expose
    private Double national;
    @SerializedName("east")
    @Expose
    private Double east;
    @SerializedName("central")
    @Expose
    private Double central;
    @SerializedName("south")
    @Expose
    private Double south;
    @SerializedName("north")
    @Expose
    private Double north;

    public Double getWest() {
        return west;
    }

    public void setWest(Double west) {
        this.west = west;
    }

    public Double getNational() {
        return national;
    }

    public void setNational(Double national) {
        this.national = national;
    }

    public Double getEast() {
        return east;
    }

    public void setEast(Double east) {
        this.east = east;
    }

    public Double getCentral() {
        return central;
    }

    public void setCentral(Double central) {
        this.central = central;
    }

    public Double getSouth() {
        return south;
    }

    public void setSouth(Double south) {
        this.south = south;
    }

    public Double getNorth() {
        return north;
    }

    public void setNorth(Double north) {
        this.north = north;
    }

}
