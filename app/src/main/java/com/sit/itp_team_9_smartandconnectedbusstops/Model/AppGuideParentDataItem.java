package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class AppGuideParentDataItem {

    private String parentName;
    private ArrayList<AppGuideChildDataItem> childDataItems;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public ArrayList<AppGuideChildDataItem> getChildDataItems() {
        return childDataItems;
    }

    public void setChildDataItems(ArrayList<AppGuideChildDataItem> childDataItems) {
        this.childDataItems = childDataItems;
    }
}
