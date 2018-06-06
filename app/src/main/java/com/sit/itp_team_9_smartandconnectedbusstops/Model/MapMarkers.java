package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapMarkers implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public MapMarkers(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmSnippet(String mSnippet) {
        this.mSnippet = mSnippet;
    }
}
