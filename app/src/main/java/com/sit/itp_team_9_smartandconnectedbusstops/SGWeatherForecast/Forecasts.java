package com.sit.itp_team_9_smartandconnectedbusstops.SGWeatherForecast;

public class Forecasts {
    private String area;

    private String forecast;

    public String getArea ()
    {
        return area;
    }

    public void setArea (String area)
    {
        this.area = area;
    }

    public String getForecast ()
    {
        return forecast;
    }

    public void setForecast (String forecast)
    {
        this.forecast = forecast;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [area = "+area+", forecast = "+forecast+"]";
    }
}
