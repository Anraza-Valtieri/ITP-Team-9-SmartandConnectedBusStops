package com.sit.itp_team_9_smartandconnectedbusstops.Interfaces;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;

import java.util.List;
import java.util.Map;

public interface JSONLTALoadAll {
    void processFinishAllStops(Map<String, LTABusStopData> result);
    void processFinishAllBuses(Map<String, List<LTABusStopData>> result);
}
