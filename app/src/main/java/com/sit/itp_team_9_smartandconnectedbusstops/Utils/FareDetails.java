package com.sit.itp_team_9_smartandconnectedbusstops.Utils;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.Fares;

import java.util.ArrayList;
import java.util.HashMap;

public class FareDetails {

    ArrayList<Double> StudentFareDistance = new ArrayList<>();
    ArrayList<Double> AdultFareDistance = new ArrayList<>();
    ArrayList<Double> SeniorFareDistance = new ArrayList<>();

    HashMap<Double, Fares> StudentFareDetails = new HashMap();
    HashMap<Double, Fares> AdultFareDetails = new HashMap();
    HashMap<Double, Fares> SeniorFareDetails = new HashMap();

    public ArrayList<Double> getStudentFareDistance() {
        return StudentFareDistance;
    }

    public ArrayList<Double> getAdultFareDistance() {
        return AdultFareDistance;
    }

    public ArrayList<Double> getSeniorFareDistance() {
        return SeniorFareDistance;
    }

    public HashMap<Double, Fares> getStudentFaresMap() {
        return StudentFareDetails;
    }

    public HashMap<Double, Fares> getAdultFaresMap() {
        return AdultFareDetails;
    }

    public HashMap<Double, Fares> getSeniorFaresMap() {
        return SeniorFareDetails;
    }

    public void populateStudentFareDistance() {
        StudentFareDistance.add(3.2);
        StudentFareDistance.add(4.2);
        StudentFareDistance.add(5.2);
        StudentFareDistance.add(6.2);
        StudentFareDistance.add(7.2);
        StudentFareDistance.add(7.3);
    }

    public void populateAdultFareDistance() {
        AdultFareDistance.add(3.2);
        AdultFareDistance.add(4.2);
        AdultFareDistance.add(5.2);
        AdultFareDistance.add(6.2);
        AdultFareDistance.add(7.2);
        AdultFareDistance.add(8.2);
        AdultFareDistance.add(9.2);
        AdultFareDistance.add(10.2);
        AdultFareDistance.add(11.2);
        AdultFareDistance.add(12.2);
        AdultFareDistance.add(13.2);
        AdultFareDistance.add(14.2);
        AdultFareDistance.add(15.2);
        AdultFareDistance.add(16.2);
        AdultFareDistance.add(17.2);
        AdultFareDistance.add(18.2);
        AdultFareDistance.add(19.2);
        AdultFareDistance.add(20.2);
        AdultFareDistance.add(21.2);
        AdultFareDistance.add(22.2);
        AdultFareDistance.add(23.2);
        AdultFareDistance.add(24.2);
        AdultFareDistance.add(25.2);
        AdultFareDistance.add(26.2);
        AdultFareDistance.add(27.2);
        AdultFareDistance.add(28.2);
        AdultFareDistance.add(29.2);
        AdultFareDistance.add(30.2);
        AdultFareDistance.add(31.2);
        AdultFareDistance.add(32.2);
        AdultFareDistance.add(33.2);
        AdultFareDistance.add(34.2);
        AdultFareDistance.add(35.2);
        AdultFareDistance.add(36.2);
        AdultFareDistance.add(37.2);
        AdultFareDistance.add(38.2);
        AdultFareDistance.add(39.2);
        AdultFareDistance.add(40.2);
        AdultFareDistance.add(40.3);
    }

    public void populateSeniorFareDistance() {
        SeniorFareDistance.add(3.2);
        SeniorFareDistance.add(4.2);
        SeniorFareDistance.add(5.2);
        SeniorFareDistance.add(6.2);
        SeniorFareDistance.add(7.2);
        SeniorFareDistance.add(7.3);
    }

    public void populateStudentFaresMap() {
        StudentFareDetails.put(3.2, new Fares("0.37", "0.67", "0.00"));
        StudentFareDetails.put(4.2, new Fares("0.42", "0.72", "0.00"));
        StudentFareDetails.put(5.2, new Fares("0.47", "0.77", "0.00"));
        StudentFareDetails.put(6.2, new Fares("0.52", "0.82", "0.02"));
        StudentFareDetails.put(7.2, new Fares("0.55", "0.85", "0.05"));
        StudentFareDetails.put(7.3, new Fares("0.58", "0.88", "0.08"));
    }

    public void populateAdultFaresMap() {
        AdultFareDetails.put(3.2,new Fares("0.77","1.37","0.27"));
        AdultFareDetails.put(4.2,new Fares("0.87","1.47","0.37"));
        AdultFareDetails.put(5.2,new Fares("0.97","1.57","0.47"));
        AdultFareDetails.put(6.2,new Fares("1.07","1.67","0.57"));
        AdultFareDetails.put(7.2,new Fares("1.16","1.76","0.66"));
        AdultFareDetails.put(8.2,new Fares("1.23","1.83","0.73"));
        AdultFareDetails.put(9.2,new Fares("1.29","1.89","0.79"));
        AdultFareDetails.put(10.2,new Fares("1.33","1.93","0.83"));
        AdultFareDetails.put(11.2,new Fares("1.37","1.97","0.87"));
        AdultFareDetails.put(12.2,new Fares("1.41","2.01","0.91"));
        AdultFareDetails.put(13.2,new Fares("1.45","2.05","0.95"));
        AdultFareDetails.put(14.2,new Fares("1.49","2.09","0.99"));
        AdultFareDetails.put(15.2,new Fares("1.53","2.13","1.03"));
        AdultFareDetails.put(16.2,new Fares("1.57","2.17","1.07"));
        AdultFareDetails.put(17.2,new Fares("1.61","2.21","1.11"));
        AdultFareDetails.put(18.2,new Fares("1.65","2.25","1.15"));
        AdultFareDetails.put(19.2,new Fares("1.69","2.29","1.19"));
        AdultFareDetails.put(20.2,new Fares("1.72","2.32","1.22"));
        AdultFareDetails.put(21.2,new Fares("1.75","2.35","1.25"));
        AdultFareDetails.put(22.2,new Fares("1.78","2.38","1.28"));
        AdultFareDetails.put(23.2,new Fares("1.81","2.41","1.31"));
        AdultFareDetails.put(24.2,new Fares("1.83","2.43","1.33"));
        AdultFareDetails.put(25.2,new Fares("1.85","2.45","1.35"));
        AdultFareDetails.put(26.2,new Fares("1.87","2.47","1.37"));
        AdultFareDetails.put(27.2,new Fares("1.88","2.48","1.38"));
        AdultFareDetails.put(28.2,new Fares("1.89","2.49","1.39"));
        AdultFareDetails.put(29.2,new Fares("1.90","2.50","1.40"));
        AdultFareDetails.put(30.2,new Fares("1.91","2.51","1.41"));
        AdultFareDetails.put(31.2,new Fares("1.92","2.52","1.42"));
        AdultFareDetails.put(32.2,new Fares("1.93","2.53","1.43"));
        AdultFareDetails.put(33.2,new Fares("1.94","2.54","1.44"));
        AdultFareDetails.put(34.2,new Fares("1.95","2.55","1.45"));
        AdultFareDetails.put(35.2,new Fares("1.96","2.56","1.46"));
        AdultFareDetails.put(36.2,new Fares("1.97","2.57","1.47"));
        AdultFareDetails.put(37.2,new Fares("1.98","2.58","1.48"));
        AdultFareDetails.put(38.2,new Fares("1.99","2.59","1.49"));
        AdultFareDetails.put(39.2,new Fares("2.00","2.60","1.50"));
        AdultFareDetails.put(40.2,new Fares("2.01","2.61","1.51"));
        AdultFareDetails.put(40.3,new Fares("2.02","2.62","1.52"));
    }

    public void populateSeniorFaresMap() {
        SeniorFareDetails.put(3.2, new Fares("0.54", "0.99", "0.04"));
        SeniorFareDetails.put(4.2, new Fares("0.61", "1.06", "0.11"));
        SeniorFareDetails.put(5.2, new Fares("0.68", "1.13", "0.18"));
        SeniorFareDetails.put(6.2, new Fares("0.75", "1.20", "0.25"));
        SeniorFareDetails.put(7.2, new Fares("0.81", "1.26", "0.31"));
        SeniorFareDetails.put(7.3, new Fares("0.87", "1.32", "0.37"));
    }
}
