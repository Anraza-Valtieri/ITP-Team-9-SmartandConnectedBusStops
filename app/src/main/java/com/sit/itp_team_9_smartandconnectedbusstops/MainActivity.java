package com.sit.itp_team_9_smartandconnectedbusstops;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.CardAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONGoogleResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTALoadAll;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTAResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleBusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.MapMarkers;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONGoogleNearbySearchParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusStopParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private Toolbar toolbar;
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
    private ArrayList<BusStopCards> favCardList = new ArrayList<>(); // Favorite cards
    public ArrayList<BusStopCards> newCardList = new ArrayList<>(); // Use this to push into adapter

    // Map Markers
    private ClusterManager<MapMarkers> mClusterManager;
    private Map<String, MapMarkers> markerMap = new HashMap<>();

    // Recycler
    private CardAdapter adapter = null;
    private View rootView;

    private BottomNavigationView bottomNav;

    //Handler
    private final Handler handler = new Handler();

    //Pooling limit
    private boolean pooling = false;

    //Progress
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootView = findViewById(R.id.includeroot);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        navheaderbanner = navHeader.findViewById(R.id.headerbanner);
        // Toolbar :: Transparent
//        toolbar.setBackgroundColor(Color.TRANSPARENT);

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

        fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(view -> {
            newCardList.clear();
            FindNearbyBusStop();
            updateBottomSheet();
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        progressBar = findViewById(R.id.progressBar);

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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void clearCardsForUpdate(){
        adapter.Clear();
    }
    private void updateBottomSheet(){
        //TODO Adjust bottomsheet to card length.
        adapter.Clear();
//        adapter.addAllCard(newCardList);
    }
    private void prepareBottomSheet(){
        // Bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    // this part hides the button immediately and waits bottom sheet
                    // to collapse to show
                    if (BottomSheetBehavior.STATE_DRAGGING == newState) {
//                        fab.hide();
//                        fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                        fab.setVisibility(View.GONE);
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                        fab.show();
//                        fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
//                        fab.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                    } else if (BottomSheetBehavior.STATE_EXPANDED == newState){
//                        fab.hide();
//                        fab.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(true);
        adapter = new CardAdapter(getApplicationContext(), new ArrayList<>(), mMap, bottomSheetBehavior);
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

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_fav) {
                fab.hide();
                clearCardsForUpdate();
            } else if (id == R.id.action_nav) {
                fab.hide();
                clearCardsForUpdate();
            } else if (id == R.id.action_nearby) {
//                fab.show();
                if(!isPooling()) {
                    setPooling(true);
                    clearCardsForUpdate();
                    FindNearbyBusStop();
                    handler.postDelayed(() -> setPooling(false), 5000);
                }
            }
            return true;
        });
        bottomNav.setSelectedItemId(R.id.action_nearby);


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

    @SuppressWarnings("unchecked")
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(mDefaultLocation)      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
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
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            //toolbar.setBackgroundColor(Color.WHITE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    ltaReply.execute();
                    return null;
                }
            };
            asyncTask.execute();

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
                        findViewById(R.id.mapView), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
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
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //mMap.setTrafficEnabled(true);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        mMap.setOnPoiClickListener(this);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        prepareBottomSheet();
        PrepareLTAData();
    }

    private void snackbarNotice(String text){
        final Snackbar sb = Snackbar.make(findViewById(R.id.bottombar),text,Snackbar.LENGTH_SHORT);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)sb.getView().getLayoutParams();
        sb.getView().setLayoutParams(params);
        sb.show();

    }

    public boolean isPooling() {
        return pooling;
    }

    public void setPooling(boolean pooling) {
        this.pooling = pooling;
    }

    private void PrepareLTAData(){
        Log.d(TAG, "PrepareLTAData: Start");
//        snackbarNotice("Syncing data.");
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
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    @Override
    public void processFinishFromGoogle(List<GoogleBusStopData> result) {
        if(result.size() <= 0){
            Log.d(TAG, "processFinishFromLTA: Google returned no data");
            return;
        }
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                for(int i=0; i< result.size(); i++) {
                    GoogleBusStopData stop = result.get(i);
                    BusStopCards newStop = new BusStopCards();
                    Log.d(TAG, "processFinishFromLTA: Looking up "+stop.getName());
                    if(allBusStops.containsKey(stop.getName())) {
                        String id = allBusStops.get(stop.getName()).getBusStopCode();
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
                return null;
            }
        }.execute();
    }

    /*
    BUS TIMING from LTA
     */
    @SuppressWarnings("unchecked")
    @Override
    public void processFinishFromLTA(Map<String, Map> result) {
        if(result.size() < 1){
            Log.e(TAG, "processFinishFromLTA: LTA returned no data");
        }else{
            for (Map.Entry<String, Map> entry : result.entrySet()) {
                String key = entry.getKey(); // Bus stop ID
                Map value = entry.getValue(); // Map with Bus to Timings
                BusStopCards card = busStopMap.get(key);

                Map<String, List<String>> finalData = new TreeMap<>(value);
                for (List<String> newData : finalData.values()){
                    String toConvertID = newData.get(3);
                    Log.d(TAG, "processFinishFromLTA: toConvertID "+ toConvertID);
                    newData.set(3, allBusByID.get(toConvertID));
                }

                card.setBusServices(finalData);
                card.setLastUpdated(Calendar.getInstance().getTime().toString());
                Log.d(TAG, "processFinishFromLTA: Bus stop ID:"+key
                        +" Bus Stop Name: "+ card.getBusStopName()
                        +" - "+card.getBusServices() + " - Last Updated: "
                        + Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
                adapter.addCard(card);
                newCardList.add(card);

                LatLng ll = new LatLng(Double.parseDouble(card.getBusStopLat()), Double.parseDouble(card.getBusStopLong()));
//                Log.d(TAG, "processFinishFromLTA: "+Double.toString(ll.latitude)+","+Double.toString(ll.longitude));

                /*
                Create Map markers!
                 */
                if(markerMap.get(card.getBusStopName()) == null){
                MapMarkers infoWindowItem = new MapMarkers(ll.latitude, ll.longitude, card.getBusStopName(), key);
                    if(!mClusterManager.getClusterMarkerCollection().getMarkers().contains(infoWindowItem)) {
                        mClusterManager.addItem(infoWindowItem);
                        markerMap.put(card.getBusStopName(), infoWindowItem);
                        mClusterManager.setOnClusterItemClickListener(mapMarkers -> {
                            if (allBusStops.containsKey(mapMarkers.getTitle())) {
                                BusStopCards newStop = new BusStopCards();
                                // Clear old cards
                                adapter.Clear();
                                newCardList.clear();
                                String id = allBusStops.get(mapMarkers.getTitle()).getBusStopCode();
                                newStop.setBusStopID(id);
                                newStop.setBusStopName(mapMarkers.getTitle());
                                newStop.setBusStopLat(Double.toString(mapMarkers.getPosition().latitude));
                                newStop.setBusStopLong(Double.toString(mapMarkers.getPosition().longitude));
                                busStopMap.put(newStop.getBusStopID(), newStop);

                                List<String> urlsList = new ArrayList<>();
                                urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
//            Log.d(TAG, "Look up bus timings for : " + newStop.getBusStopID());
                                JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, newStop.getBusStopID());
                                ltaReply.delegate = MainActivity.this;
                                ltaReply.execute();
                            } else {
                                Log.e(TAG, "processFinishFromLTA: ERROR Missing data from LTA? : " + mapMarkers.getTitle());
                            }
                            return false;
                        });
                    }
                }
            }
            progressBar.setVisibility(View.GONE);
//            updateBottomSheet();
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
//        FindNearbyBusStop();
        adapter.Refresh();
//        refreshCardList();
    }

    private void LinkIDtoName(){
        @SuppressLint("StaticFieldLeak")
        @SuppressWarnings("unchecked")
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
                Log.d(TAG, "onPostExecute: LinkIDtoName completed");
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private void FindNearbyBusStop(){
            try {
                if (mLocationPermissionGranted) {
                    progressBar.setVisibility(View.VISIBLE);
                    Task locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
//                            snackbarNotice("Looking around you..");
                            new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] objects) {
                                    List<String> urlsList = new ArrayList<>();
                                    urlsList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude() + "&rankby=distance&type=transit_station&key=AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s");
                                    Log.d(TAG, "FindNearbyBusStop: " + urlsList.get(0));
                                    JSONGoogleNearbySearchParser googleReply = new JSONGoogleNearbySearchParser(MainActivity.this, urlsList);
                                    googleReply.delegate = MainActivity.this;
                                    googleReply.execute();

                                    Point size = new Point();
                                    getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    super.onPostExecute(o);
                                    // Set the map's camera position to the current location of the device.
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                            .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                            .build();                   // Creates a CameraPosition from the builder
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }
                            }.execute();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(mDefaultLocation)      // Sets the center of the map to Mountain View
                                    .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
                return;
            }
//        fab.show();
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
        anim.addUpdateListener(animation -> {
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
        });

        anim.setDuration(700).start();
    }

    @SuppressWarnings("unchecked")
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
