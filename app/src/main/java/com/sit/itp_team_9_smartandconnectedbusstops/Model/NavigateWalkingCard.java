package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NavigateWalkingCard extends Card {
    private int ID;
    private String totalTime;
    private String totalDistance;
    private String remark;
    private List<String> description;
    private boolean isFavorite;
    private List<String> summary;
    private HashMap<String, List<String>> detailedSteps;
    private String placeidStart, placeidEnd, routeID,mode;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public HashMap<String, List<String>> getDetailedSteps() {
        return detailedSteps;
    }

    public void setDetailedSteps(HashMap<String, List<String>> detailedSteps) {
        this.detailedSteps = detailedSteps;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getStartPlaceId() {
        return placeidStart;
    }

    public void setStartPlaceId(String placeidStart) {
        this.placeidStart = placeidStart;
    }

    public String geEndPlaceId() {
        return placeidEnd;
    }

    public void setEndPlaceId(String placeidEnd) {
        this.placeidEnd = placeidEnd;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ArrayList<String> getFavRouteID() {
        return favRouteID;
    }

    public void setFavRouteID(ArrayList<String> newFavRoute) {
        favRouteID.clear();
        favRouteID = newFavRoute;
    }

    public ArrayList<String> favRouteID = new ArrayList<>();
    /**
     * Sets and returns a NavigateWalkingCard card
     * <p>
     * This method always returns immediately
     *
     * @param googleRoutesData GoogleRoutesData
     * @return card NavigateWalkingCard
     */
    public static NavigateWalkingCard getRouteDataWalking(GoogleRoutesData googleRoutesData, String umbrella) {
        NavigateWalkingCard card = new NavigateWalkingCard();
        card.setType(Card.NAVIGATE_WALKING_CARD);
        List<String> walkingDescription = new ArrayList<>();
        LinkedHashMap<String, List<String>> walkingDetailedSteps = new LinkedHashMap<>();
        List<String> walkingDetailedStepsChildren = new ArrayList<>();

        String translatedDistance = "";

        if(googleRoutesData.getTotalDistance().contains("km")) {
            translatedDistance = googleRoutesData.getTotalDistance().replace("km", MainActivity.context.getResources().getString(R.string.km));
        }
        else if(googleRoutesData.getTotalDistance().contains("m")) {
            translatedDistance = googleRoutesData.getTotalDistance().replace("m", MainActivity.context.getResources().getString(R.string.m));
        }

        card.setTotalDistance(translatedDistance);

        String translatedDuration = "";
        String hourTranslate = "";
        if(!(googleRoutesData.getTotalDuration().contains("hour")) && googleRoutesData.getTotalDuration().contains("mins")) {
            translatedDuration = googleRoutesData.getTotalDuration().replace("mins", MainActivity.context.getResources().getString(R.string.minutes));
        }
        else if(googleRoutesData.getTotalDuration().contains("hour") && (googleRoutesData.getTotalDuration().contains("mins")|| googleRoutesData.getTotalDuration().contains("min"))) {
            hourTranslate = googleRoutesData.getTotalDuration().replace("hour", MainActivity.context.getResources().getString(R.string.hour));

            if(hourTranslate.contains("mins")) {
                translatedDuration = hourTranslate.replace("mins", MainActivity.context.getResources().getString(R.string.minutes));
            }
            else if(hourTranslate.contains("min")) {
                translatedDuration = hourTranslate.replace("min", MainActivity.context.getResources().getString(R.string.minute));
            }
        }
        else if(googleRoutesData.getTotalDuration().contains("hours") && (googleRoutesData.getTotalDuration().contains("mins") || googleRoutesData.getTotalDuration().contains("min"))) {
            hourTranslate = googleRoutesData.getTotalDuration().replace("hours", MainActivity.context.getResources().getString(R.string.hours));

            if(hourTranslate.contains("mins")) {
                translatedDuration = hourTranslate.replace("mins", MainActivity.context.getResources().getString(R.string.minutes));
            }
            else if(hourTranslate.contains("min")) {
                translatedDuration = hourTranslate.replace("min", MainActivity.context.getResources().getString(R.string.minute));
            }
        }

        card.setID(googleRoutesData.getID());
        card.setStartPlaceId(googleRoutesData.getStartPlaceId());
        card.setEndPlaceId(googleRoutesData.geEndPlaceId());
        card.setRouteID(googleRoutesData.getRouteID());
        card.setMode("walking");

        card.setTotalTime(translatedDuration);
        card.setRemark(umbrella);
        String description = "Via " + googleRoutesData.getSummary();
        walkingDescription.add(description);
        for (int i = 0; i < googleRoutesData.getSteps().size(); i++){
            walkingDetailedStepsChildren.add(googleRoutesData.getSteps().get(i).getHtmlInstructions());
        }
        walkingDetailedSteps.put(description,walkingDetailedStepsChildren);
//        card.setID(googleRoutesData.getID());
        card.setDescription(walkingDescription);
        card.setDetailedSteps(walkingDetailedSteps);
        return card;
    }
}
