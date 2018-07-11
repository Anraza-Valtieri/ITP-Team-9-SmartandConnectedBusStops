package com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast;

public class Area_metadata {
    private String name;

    private Label_location label_location;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Label_location getLabel_location ()
    {
        return label_location;
    }

    public void setLabel_location (Label_location label_location)
    {
        this.label_location = label_location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", label_location = "+label_location+"]";
    }
}
