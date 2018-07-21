package com.sit.itp_team_9_smartandconnectedbusstops.Model.MRT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MRTJson {
    @SerializedName("lines")
    @Expose
    private List<Line> lines = null;

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
