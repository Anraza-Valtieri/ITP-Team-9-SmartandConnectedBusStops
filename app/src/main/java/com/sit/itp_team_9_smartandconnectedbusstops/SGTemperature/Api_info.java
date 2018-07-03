package com.sit.itp_team_9_smartandconnectedbusstops.SGTemperature;

public class Api_info {
    private String status;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + "]";
    }
}
