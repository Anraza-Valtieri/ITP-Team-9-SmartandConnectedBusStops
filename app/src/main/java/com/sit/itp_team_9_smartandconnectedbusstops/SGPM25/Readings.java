
package com.sit.itp_team_9_smartandconnectedbusstops.SGPM25;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Readings {

    @SerializedName("pm25_one_hourly")
    @Expose
    private Pm25OneHourly pm25OneHourly;

    public Pm25OneHourly getPm25OneHourly() {
        return pm25OneHourly;
    }

    public void setPm25OneHourly(Pm25OneHourly pm25OneHourly) {
        this.pm25OneHourly = pm25OneHourly;
    }

}
