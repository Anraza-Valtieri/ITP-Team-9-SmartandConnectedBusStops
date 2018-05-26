package com.sit.itp_team_9_smartandconnectedbusstops;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.CardAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONGoogleResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTALoadAll;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTAResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleBusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONGoogleNearbySearchParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusStopParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnPoiClickListener, JSONGoogleResponse, JSONLTAResponse, JSONLTALoadAll {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private View mapView;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Singapore, Singapore) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(1.3139991, 103.7740386);
    private static final int DEFAULT_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Header
    private NavigationView navigationView;
    private View navHeader;
    private LinearLayout navheaderbanner;

    // FAB
    FloatingActionButton fab;

    // Bottom sheet
    protected BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerView;

    // Bus stop
    // Key Roadname Value LTABusStopData Object
    private Map<String, LTABusStopData> allBusStops = new HashMap<>();
    // Key: Bus stop ID Value: Bus stop name
    private Map<String, String> allBusByID = new HashMap<>();
    // Key: Bus stop ID Value BusStopCards Object
    private Map<String, BusStopCards> busStopMap = new HashMap<>();

    // Bus cards
    private ArrayList<BusStopCards> cardList = new ArrayList<>();
    public ArrayList<BusStopCards> newCardList = new ArrayList<>();

    // Map Markers
    private Map<String, Marker> markerMap = new HashMap<>();

    // Recycler
    private CardAdapter adapter = null;
    private View rootView;

    //Handler
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootView = findViewById(R.id.includeroot);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        navheaderbanner = navHeader.findViewById(R.id.headerbanner);
        // Toolbar :: Transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Status bar :: Transparent
        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCardList.clear();
                FindNearbyBusStop();
                updateBottomSheetLength();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        /*
        (0.6.6-dev) [Firestore]: The behavior for java.util.Date objects stored in Firestore is going to change AND YOUR APP MAY BREAK.
             To hide this warning and ensure your app does not break, you need to add the following code to your app before calling any other Cloud Firestore methods:

        With this change, timestamps stored in Cloud Firestore will be read back as com.google.firebase.Timestamp objects instead of as system java.util.Date objects. So you will also need to update code expecting a java.util.Date to instead expect a Timestamp. For example:

             // Old:
             java.util.Date date = snapshot.getDate("created_at");
             // New:
             Timestamp timestamp = snapshot.getTimestamp("created_at");
             java.util.Date date = timestamp.toDate();
         */
        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
        db.disableNetwork();*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
    }

    private void updateBottomSheetLength(){
        //TODO Adjust bottomsheet to card length.
        adapter.Clear();
        adapter.addAllCard(newCardList);
//        FrameLayout parentThatHasBottomSheetBehavior = (FrameLayout) recyclerView.getParent().getParent();
//        parentThatHasBottomSheetBehavior.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
//                recyclerView.getHeight()));
    }
    private void prepareBottomSheet(){
        // Bottom sheet
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(true);
        adapter = new CardAdapter(getApplicationContext(), cardList, mMap);
        adapter.doAutoRefresh();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if(animator instanceof SimpleItemAnimator){
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
//        CoordinatorLayout parentThatHasBottomSheetBehavior = (CoordinatorLayout)recyclerView.getParent();
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    // this part hides the button immediately and waits bottom sheet
                    // to collapse to show
                    if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                        fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                        fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.getItemId() == R.id.option_get_place) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        BusStopCards newStop = new BusStopCards();
        Log.d(TAG, "processFinishFromLTA: Looking up "+poi.name);
        if(allBusStops.containsKey(poi.name)) {
            // Clear old cards
            adapter.Clear();
            newCardList.clear();
            String id = allBusStops.get(poi.name).getBusStopCode();
            newStop.setBusStopID(id);
            newStop.setBusStopName(poi.name);
            newStop.setBusStopLat(Double.toString(poi.latLng.latitude));
            newStop.setBusStopLong(Double.toString(poi.latLng.longitude));
            busStopMap.put(newStop.getBusStopID(), newStop);

            List<String> urlsList = new ArrayList<>();
            urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
//            Log.d(TAG, "Look up bus timings for : " + newStop.getBusStopID());
            JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, newStop.getBusStopID());
            ltaReply.delegate = MainActivity.this;
            ltaReply.execute();
        }else{
            Log.e(TAG, "processFinishFromLTA: ERROR Missing data from LTA? : "+poi.name);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.mapView), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }

        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //Disable Map Toolbar:
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        //mMap.setTrafficEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }

        mMap.setOnPoiClickListener((GoogleMap.OnPoiClickListener) this);
        prepareBottomSheet();
        PrepareLTAData();
    }

    private void SnackbarNotice(String text){
//        final Snackbar sb = Snackbar.make(rootView,text,Snackbar.LENGTH_SHORT)
//                            .setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    sb.dismiss();
//                                }
//                            });
        final Snackbar sb = Snackbar.make(rootView,text,Snackbar.LENGTH_SHORT);
        sb.show();

    }


    private void PrepareLTAData(){
        Log.d(TAG, "PrepareLTAData: Start");
        SnackbarNotice("Syncing data.");
        List<String> urlsList = new ArrayList<>();
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=500");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=1000");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=1500");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=2000");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=2500");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=3000");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=3500");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=4000");
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=4500");
        JSONLTABusStopParser ltaData = new JSONLTABusStopParser(MainActivity.this, urlsList);
        ltaData.delegate = MainActivity.this;
        ltaData.execute();

    }

    /*
    ALL BUS STOPS FROM LTA
     */
    @Override
    public void processFinishAllStops(Map<String, LTABusStopData> result) {
        Log.d(TAG, "processFinishAllStops: Complete");
        //allBusStops = result;
        allBusStops.putAll(result);
        result.clear();
        LinkIDtoName();
        FillBusData();
    }
    /*
    Nearby BusStops from Google
     */
    @Override
    public void processFinishFromGoogle(List<GoogleBusStopData> result) {
        if(result.size() <= 0){
            Log.d(TAG, "processFinishFromLTA: Google returned no data");
            return;
        }
        for(int i=0; i< result.size(); i++) {
            GoogleBusStopData stop = result.get(i);
            BusStopCards newStop = new BusStopCards();
            Log.d(TAG, "processFinishFromLTA: Looking up "+stop.getName());
            if(allBusStops.containsKey(stop.getName().toString())) {
                String id = allBusStops.get(stop.getName().toString()).getBusStopCode();
                newStop.setBusStopID(id);
                newStop.setBusStopName(stop.getName());
                newStop.setBusStopLat(stop.getLat());
                newStop.setBusStopLong(stop.getLng());
                busStopMap.put(newStop.getBusStopID(), newStop);

                List<String> urlsList = new ArrayList<>();
                urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
//                Log.d(TAG, "Look up bus timings for : " + newStop.getBusStopID());
                JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, newStop.getBusStopID());
                ltaReply.delegate = MainActivity.this;
                ltaReply.execute();
            }else{
                Log.e(TAG, "processFinishFromLTA: ERROR Missing data from LTA? : "+stop.getName());
            }
        }
    }

    /*
    BUS TIMING from LTA
     */
    @Override
    public void processFinishFromLTA(Map<String, Map> result) {
        if(result.size() < 1){
            Log.e(TAG, "processFinishFromLTA: LTA returned no data");
            return;
        }else{
            for (Map.Entry<String, Map> entry : result.entrySet()) {
                String key = entry.getKey(); // Bus stop ID
                Map value = entry.getValue(); // Map with Bus to Timings
                BusStopCards card = busStopMap.get(key);

                Map<String, List<String>> finalData = new HashMap<>();
                finalData.putAll(value);
                for (Map.Entry<String, List<String>> newData : finalData.entrySet()){
                    String key2 = newData.getKey();
                    List<String> schedule = newData.getValue();
                    String toConvertID = schedule.get(3);
                    Log.d(TAG, "processFinishFromLTA: toConvertID "+ toConvertID);
                    schedule.set(3, allBusByID.get(toConvertID));
                }
                card.setBusServices(finalData);
                card.setLastUpdated(Calendar.getInstance().getTime().toString());
                Log.d(TAG, "processFinishFromLTA: Bus stop ID:"+key
                        +" Bus Stop Name: "+ card.getBusStopName()
                        +" - "+card.getBusServices() + " - Last Updated: "
                        + Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
//                adapter.addCard(card);
                newCardList.add(card);

                LatLng ll = new LatLng(Double.parseDouble(card.getBusStopLat()), Double.parseDouble(card.getBusStopLong()));
//                Log.d(TAG, "processFinishFromLTA: "+Double.toString(ll.latitude)+","+Double.toString(ll.longitude));

                /*
                Create Map markers!
                 */
                if(markerMap.get(card.getBusStopName()) == null) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(ll)
                            .title(card.getBusStopName())
                            .snippet(key)
                            .visible(true)
                            .alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            BusStopCards newStop = new BusStopCards();
                            Log.d(TAG, "processFinishFromLTA: Looking up " + marker.getTitle());
                            if (allBusStops.containsKey(marker.getTitle())) {
                                // Clear old cards
                                adapter.Clear();
                                newCardList.clear();
                                String id = allBusStops.get(marker.getTitle()).getBusStopCode();
                                newStop.setBusStopID(id);
                                newStop.setBusStopName(marker.getTitle());
                                newStop.setBusStopLat(Double.toString(marker.getPosition().latitude));
                                newStop.setBusStopLong(Double.toString(marker.getPosition().longitude));
                                busStopMap.put(newStop.getBusStopID(), newStop);

                                List<String> urlsList = new ArrayList<>();
                                urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
//            Log.d(TAG, "Look up bus timings for : " + newStop.getBusStopID());
                                JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, newStop.getBusStopID());
                                ltaReply.delegate = MainActivity.this;
                                ltaReply.execute();
                            } else {
                                Log.e(TAG, "processFinishFromLTA: ERROR Missing data from LTA? : " + marker.getTitle());
                            }
                            return false;
                        }
                    });
                }
            }
            updateBottomSheetLength();
        }
    }

    /*
    ALL BUS FROM LTA
     */
    @Override
    public void processFinishAllBuses(Map<String, List<LTABusStopData>> result) {
        Log.d(TAG, "processFinishAllBuses: Complete");
    }

    private void FillBusData(){
        // TODO PARAS BUS SERVICE DATA INTO BUS STOP HERE

        // Once data is in we can start looking around us!
        FindNearbyBusStop();
        adapter.Refresh();
//        refreshCardList();
    }

    private void LinkIDtoName(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                for (Map.Entry<String, LTABusStopData> newData : allBusStops.entrySet()) {
                    String key = newData.getKey();
                    LTABusStopData value = newData.getValue();
                    allBusByID.put(value.getBusStopCode(),key);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.d(TAG, "onPostExecute: LinkIDtoName completed");
            }
        }.execute();
    }
    private void FindNearbyBusStop(){
        try {
            if (mLocationPermissionGranted) {

                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            // Clear old cards
                            //refreshCardList();
                            //adapter.Clear();
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            List<String> urlsList = new ArrayList<>();
                            urlsList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&rankby=distance&type=transit_station&key=AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s");
                            Log.d(TAG, "FindNearbyBusStop: "+urlsList.get(0));
                            SnackbarNotice("Searching Nearby.");
                            JSONGoogleNearbySearchParser googleReply = new JSONGoogleNearbySearchParser(MainActivity.this, urlsList);
                            googleReply.delegate = MainActivity.this;
                            googleReply.execute();

//                            View peakView = findViewById(R.id.cardlist);
                            Point size = new Point();
                            getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                            int height = size.y;
//                            bottomSheetBehavior.setPeekHeight(height/6);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                            View peakView = findViewById(R.id.drag_me);
//                            bottomSheetBehavior.setPeekHeight(peakView.getHeight());
//                            peakView.requestLayout();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            return;
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
            return;
        }
        fab.show();
    }

//    public void refreshCardList(){
//        adapter.Clear();
//        adapter.addAllCard(newCardList);
//
//        Log.d(TAG, "run: refreshCardList");
//        refreshCardList();
//    }

    private void tintSystemBars(int fromColorLight, int fromColorDark, int toColorLight, int toColorDark) {
        // Initial colors of each system bar.
        final int statusBarColor = ContextCompat.getColor(this,fromColorDark);
        final int toolbarColor = ContextCompat.getColor(this,fromColorLight);

        // Desired final colors of each bar.
        final int statusBarToColor = ContextCompat.getColor(this,toColorDark);
        final int toolbarToColor = ContextCompat.getColor(this,toColorLight);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, statusBarToColor, position);
                getWindow().setStatusBarColor(blended);
                getWindow().setNavigationBarColor(blended);
                navheaderbanner.setBackgroundColor(blended);

                // Apply blended color to the ActionBar.
                int blended2 = blendColors(toolbarColor, toolbarToColor, position);
                ColorDrawable background = new ColorDrawable(blended2);
                getSupportActionBar().setBackgroundDrawable(background);
            }
        });

        anim.setDuration(700).start();
    }

    private int blendColors(int from, int to, float ratio) {
        Object[] var = {from, to, ratio};
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Integer doInBackground(Object[] objects) {
                final float inverseRatio = 1f - ((float)objects[2]);

                final float r = Color.red(((int)objects[1])) * ((float)objects[2]) + Color.red(((int)objects[0])) * inverseRatio;
                final float g = Color.green(((int)objects[1])) * ((float)objects[2]) + Color.green(((int)objects[0])) * inverseRatio;
                final float b = Color.blue(((int)objects[1])) * ((float)objects[2]) + Color.blue(((int)objects[0])) * inverseRatio;
                return Color.rgb((int) r, (int) g, (int) b);
//				return null;
            }
        };
        int result = 0;
        try {
            result = ((int)asyncTask.execute(var).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

}
