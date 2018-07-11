package com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast;

public class Items {
    private String timestamp;

    private Forecasts[] forecasts;

    private String update_timestamp;

    private Valid_period valid_period;

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    public Forecasts[] getForecasts ()
    {
        return forecasts;
    }

    public void setForecasts (Forecasts[] forecasts)
    {
        this.forecasts = forecasts;
    }

    public String getUpdate_timestamp ()
    {
        return update_timestamp;
    }

    public void setUpdate_timestamp (String update_timestamp)
    {
        this.update_timestamp = update_timestamp;
    }

    public Valid_period getValid_period ()
    {
        return valid_period;
    }

    public void setValid_period (Valid_period valid_period)
    {
        this.valid_period = valid_period;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timestamp = "+timestamp+", forecasts = "+forecasts+", update_timestamp = "+update_timestamp+", valid_period = "+valid_period+"]";
    }
}
