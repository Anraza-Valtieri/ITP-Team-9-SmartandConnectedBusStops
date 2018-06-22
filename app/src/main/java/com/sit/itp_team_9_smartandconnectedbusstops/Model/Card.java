package com.sit.itp_team_9_smartandconnectedbusstops.Model;

public class Card {
    public final static int BUS_STOP_CARD = 0;
    public final static int NAVIGATE_TRANSIT_CARD = 1;
    public final static int NAVIGATE_WALKING_CARD = 2;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                '}';
    }
}

