package com.sit.itp_team_9_smartandconnectedbusstops.Model.MRT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Line {
    @SerializedName("colour")
    @Expose
    private String colour;
    @SerializedName("coords")
    @Expose
    private String coords;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }
}
