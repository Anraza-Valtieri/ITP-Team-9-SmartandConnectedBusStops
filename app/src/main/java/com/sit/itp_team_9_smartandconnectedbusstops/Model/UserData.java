package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.ArrayList;

public class UserData {
    public ArrayList<String> favBusStopID;
    public ArrayList<String> favRouteID;

    public UserData() {
    }

    public ArrayList<String> getFavBusStopID() {
        return favBusStopID;
    }

    public void setFavBusStopID(ArrayList<String> favBusStopID) {
        this.favBusStopID = favBusStopID;
    }

    public ArrayList<String> getFavRouteID() {
        return favRouteID;
    }

    public void setFavRouteID(ArrayList<String> favRouteID) {
        this.favRouteID = favRouteID;
    }
}
