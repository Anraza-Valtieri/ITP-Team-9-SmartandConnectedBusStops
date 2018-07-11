package com.sit.itp_team_9_smartandconnectedbusstops.Interfaces;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesData;

import java.util.List;

public interface JSONGoogleResponseRoute {
    void processFinishFromGoogle(List<GoogleRoutesData> result);
}
