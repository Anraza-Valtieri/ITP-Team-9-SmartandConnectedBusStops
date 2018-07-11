package com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature;

public class JSONTemperatureParser {

    private Api_info api_info;

    private Items[] items;

    private Metadata metadata;

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

    public Metadata getMetadata ()
    {
        return metadata;
    }

    public void setMetadata (Metadata metadata)
    {
        this.metadata = metadata;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [api_info = "+api_info+", items = "+items+", metadata = "+metadata+"]";
    }

}
