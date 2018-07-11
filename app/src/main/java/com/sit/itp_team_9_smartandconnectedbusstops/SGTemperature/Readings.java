package com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature;

public class Readings {
    private String value;

    private String station_id;

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getStation_id ()
    {
        return station_id;
    }

    public void setStation_id (String station_id)
    {
        this.station_id = station_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [value = "+value+", station_id = "+station_id+"]";
    }
}
