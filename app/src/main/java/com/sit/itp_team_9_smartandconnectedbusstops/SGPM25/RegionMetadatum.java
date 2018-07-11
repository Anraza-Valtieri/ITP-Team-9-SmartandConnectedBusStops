
package com.sit.itp_team_9_smartandconnectedbusstops.SGPM25;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegionMetadatum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("label_location")
    @Expose
    private LabelLocation labelLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LabelLocation getLabelLocation() {
        return labelLocation;
    }

    public void setLabelLocation(LabelLocation labelLocation) {
        this.labelLocation = labelLocation;
    }

}
