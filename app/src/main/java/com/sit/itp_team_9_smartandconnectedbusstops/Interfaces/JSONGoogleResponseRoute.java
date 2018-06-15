package com.sit.itp_team_9_smartandconnectedbusstops.Interfaces;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesData;

import java.util.List;

//TODO change original interface to accomodate different data types
public interface JSONGoogleResponseRoute {
    void processFinishFromGoogle(List<GoogleRoutesData> result);
}
