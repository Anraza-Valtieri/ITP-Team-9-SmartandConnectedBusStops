package com.sit.itp_team_9_smartandconnectedbusstops.Interfaces;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleBusStopData;

import java.util.List;

public interface JSONGoogleResponse {
    void processFinish(List<GoogleBusStopData> result);
}
