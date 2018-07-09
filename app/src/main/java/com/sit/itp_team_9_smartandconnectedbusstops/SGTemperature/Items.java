package com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature;

public class Items {

    private String timestamp;

    private Readings[] readings;

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    public Readings[] getReadings ()
    {
        return readings;
    }

    public void setReadings (Readings[] readings)
    {
        this.readings = readings;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timestamp = "+timestamp+", readings = "+readings+"]";
    }
}
