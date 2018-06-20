package com.sit.itp_team_9_smartandconnectedbusstops.Model;

public class Card {
    String ID;
    int type;

    public final static int BUS_STOP_CARD = 0;
    public final static int NAVIGATE_TRANSIT_CARD = 1;
    public final static int NAVIGATE_WALKING_CARD = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
