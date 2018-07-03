package com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast;

public class JSONWeatherParser {
    private Area_metadata[] area_metadata;

    private Api_info api_info;

    private Items[] items;

    public Area_metadata[] getArea_metadata ()
    {
        return area_metadata;
    }

    public void setArea_metadata (Area_metadata[] area_metadata)
    {
        this.area_metadata = area_metadata;
    }

    public Api_info getApi_info ()
    {
        return api_info;
    }

    public void setApi_info (Api_info api_info)
    {
        this.api_info = api_info;
    }

    public Items[] getItems ()
    {
        return items;
    }

    public void setItems (Items[] items)
    {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [area_metadata = "+area_metadata+", api_info = "+api_info+", items = "+items+"]";
    }
}
