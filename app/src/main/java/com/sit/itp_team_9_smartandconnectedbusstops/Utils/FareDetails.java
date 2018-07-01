package com.sit.itp_team_9_smartandconnectedbusstops.Utils;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.AdultFares;

import java.util.HashMap;
import java.util.Map;

public class FareDetails {

    HashMap<Double, AdultFares> AdultFareDetails = new HashMap();

    public void populateMap() {
        AdultFareDetails.put(3.2,new AdultFares(0.77,1.37,0.27));
        AdultFareDetails.put(4.2,new AdultFares(0.87,1.47,0.37));
        AdultFareDetails.put(5.2,new AdultFares(0.97,1.57,0.47));
        AdultFareDetails.put(6.2,new AdultFares(1.07,1.67,0.57));
        AdultFareDetails.put(7.2,new AdultFares(1.16,1.76,0.66));
        AdultFareDetails.put(8.2,new AdultFares(1.23,1.83,0.73));
        AdultFareDetails.put(9.2,new AdultFares(1.29,1.89,0.79));
        AdultFareDetails.put(10.2,new AdultFares(1.33,1.93,0.83));
        AdultFareDetails.put(11.2,new AdultFares(1.37,1.97,0.87));
        AdultFareDetails.put(12.2,new AdultFares(1.41,2.01,0.91));
        AdultFareDetails.put(13.2,new AdultFares(1.45,2.05,0.95));
        AdultFareDetails.put(14.2,new AdultFares(1.49,2.09,0.99));
        AdultFareDetails.put(15.2,new AdultFares(1.53,2.13,1.03));
        AdultFareDetails.put(16.2,new AdultFares(1.57,2.17,1.07));
        AdultFareDetails.put(17.2,new AdultFares(1.61,2.21,1.11));
        AdultFareDetails.put(18.2,new AdultFares(1.65,2.25,1.15));
        AdultFareDetails.put(19.2,new AdultFares(1.69,2.29,1.19));
        AdultFareDetails.put(20.2,new AdultFares(1.72,2.32,1.22));
        AdultFareDetails.put(21.2,new AdultFares(1.75,2.35,1.25));
        AdultFareDetails.put(22.2,new AdultFares(1.78,2.38,1.28));
        AdultFareDetails.put(23.2,new AdultFares(1.81,2.41,1.31));
        AdultFareDetails.put(24.2,new AdultFares(1.83,2.43,1.33));
        AdultFareDetails.put(25.2,new AdultFares(1.85,2.45,1.35));
        AdultFareDetails.put(26.2,new AdultFares(1.87,2.47,1.37));
        AdultFareDetails.put(27.2,new AdultFares(1.88,2.48,1.38));
        AdultFareDetails.put(28.2,new AdultFares(1.89,2.49,1.39));
        AdultFareDetails.put(29.2,new AdultFares(1.90,2.50,1.40));
        AdultFareDetails.put(30.2,new AdultFares(1.91,2.51,1.41));
        AdultFareDetails.put(31.2,new AdultFares(1.92,2.52,1.42));
        AdultFareDetails.put(32.2,new AdultFares(1.93,2.53,1.43));
        AdultFareDetails.put(33.2,new AdultFares(1.94,2.54,1.44));
        AdultFareDetails.put(34.2,new AdultFares(1.95,2.55,1.45));
        AdultFareDetails.put(35.2,new AdultFares(1.96,2.56,1.46));
        AdultFareDetails.put(36.2,new AdultFares(1.97,2.57,1.47));
        AdultFareDetails.put(37.2,new AdultFares(1.98,2.58,1.48));
        AdultFareDetails.put(38.2,new AdultFares(1.99,2.59,1.49));
        AdultFareDetails.put(39.2,new AdultFares(2.00,2.60,1.50));
        AdultFareDetails.put(40.2,new AdultFares(2.01,2.61,1.51));
        AdultFareDetails.put(40.21,new AdultFares(2.02,2.62,1.52));
    }

    public HashMap<Double, AdultFares> getAdultFaresMap() {
        return AdultFareDetails;
    }
}
