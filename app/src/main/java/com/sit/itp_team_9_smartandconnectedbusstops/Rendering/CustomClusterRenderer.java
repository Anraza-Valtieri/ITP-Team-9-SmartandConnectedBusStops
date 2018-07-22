package com.sit.itp_team_9_smartandconnectedbusstops.Rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.MapMarkers;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

public class CustomClusterRenderer extends DefaultClusterRenderer<MapMarkers> {
    private Context context;
    private final IconGenerator mClusterIconGenerator;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MapMarkers> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        mClusterIconGenerator = new IconGenerator(context);
    }

    @Override
    protected void onBeforeClusterItemRendered(MapMarkers item,
                                               MarkerOptions markerOptions) {

        BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

        markerOptions.icon(markerDescriptor);
    }

    @Override
    protected void onClusterItemRendered(MapMarkers clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MapMarkers> cluster, MarkerOptions markerOptions){

        /*final Drawable clusterIcon = context.getResources().getDrawable(R.drawable.no_image);
        clusterIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);

        mClusterIconGenerator.setBackground(clusterIcon);

        //modify padding for one or two digit numbers
        if (cluster.getSize() < 10) {
            mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
        }
        else {
            mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
        }

        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));*/
        markerOptions.visible(false);
    }
}
