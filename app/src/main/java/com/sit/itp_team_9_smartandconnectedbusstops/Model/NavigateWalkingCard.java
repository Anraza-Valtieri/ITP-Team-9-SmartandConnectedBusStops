package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NavigateWalkingCard extends Card {
    private int ID;
    private String totalTime;
    private String totalDistance;
    private List<String> description;
    private boolean isFavorite;
    private List<String> summary;
    private HashMap<String, List<String>> detailedSteps;

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

    /**
     * Sets and returns a NavigateWalkingCard card
     * <p>
     * This method always returns immediately
     *
     * @param googleRoutesData GoogleRoutesData
     * @return card NavigateWalkingCard
     */
    public static NavigateWalkingCard getRouteDataWalking(GoogleRoutesData googleRoutesData) {
        NavigateWalkingCard card = new NavigateWalkingCard();
        card.setType(Card.NAVIGATE_WALKING_CARD);
        List<String> walkingDescription = new ArrayList<>();
        LinkedHashMap<String, List<String>> walkingDetailedSteps = new LinkedHashMap<>();
        List<String> walkingDetailedStepsChildren = new ArrayList<>();

        card.setTotalDistance(googleRoutesData.getTotalDistance());
        card.setTotalTime(googleRoutesData.getTotalDuration());
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
