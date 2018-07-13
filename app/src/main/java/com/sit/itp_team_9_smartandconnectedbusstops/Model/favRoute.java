package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.List;

public class favRoute {
    public List<favRoute> routes;
    private String query;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<favRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<favRoute> routes) {
        this.routes = routes;
    }
}
