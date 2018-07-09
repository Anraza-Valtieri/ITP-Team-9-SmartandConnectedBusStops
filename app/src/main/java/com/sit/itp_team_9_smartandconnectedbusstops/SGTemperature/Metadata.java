package com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature;

public class Metadata {
    private String reading_unit;

    private Stations[] stations;

    private String reading_type;

    public String getReading_unit ()
    {
        return reading_unit;
    }

    public void setReading_unit (String reading_unit)
    {
        this.reading_unit = reading_unit;
    }

    public Stations[] getStations ()
    {
        return stations;
    }

    public void setStations (Stations[] stations)
    {
        this.stations = stations;
    }

    public String getReading_type ()
    {
        return reading_type;
    }

    public void setReading_type (String reading_type)
    {
        this.reading_type = reading_type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [reading_unit = "+reading_unit+", stations = "+stations+", reading_type = "+reading_type+"]";
    }
}
