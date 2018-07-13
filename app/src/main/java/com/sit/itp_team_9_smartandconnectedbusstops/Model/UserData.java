package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.ArrayList;

public class UserData {
    public ArrayList<String> favBusStopID;
    public ArrayList<String> favRoute;

    public UserData() {
    }

    public ArrayList<String> getFavBusStopID() {
        return favBusStopID;
    }

    public void setFavBusStopID(ArrayList<String> favBusStopID) {
        this.favBusStopID = favBusStopID;
    }

    public ArrayList<String> getFavRoute() {
        return favRoute;
    }

    public void setFavRoute(ArrayList<String> favRoute) {
        this.favRoute = favRoute;
    }
}
