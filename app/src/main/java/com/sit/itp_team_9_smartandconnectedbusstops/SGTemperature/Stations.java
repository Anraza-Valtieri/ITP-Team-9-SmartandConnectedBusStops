package com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature;

public class Stations {
    private String id;

    private Location location;

    private String name;

    private String device_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDevice_id ()
    {
        return device_id;
    }

    public void setDevice_id (String device_id)
    {
        this.device_id = device_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", location = "+location+", name = "+name+", device_id = "+device_id+"]";
    }
}
