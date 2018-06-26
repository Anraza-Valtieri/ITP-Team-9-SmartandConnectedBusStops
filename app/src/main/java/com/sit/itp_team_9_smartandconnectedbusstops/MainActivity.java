package com.sit.itp_team_9_smartandconnectedbusstops;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.maps.android.clustering.ClusterManager;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.CardAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.Card;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.DistanceData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesSteps;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.MapMarkers;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateTransitCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.UserData;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONDistanceMatrixParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONGoogleDirectionsParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusStopParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Services.NetworkSchedulerService;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils.haveNetworkConnection;
import static com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils.showNoNetworkDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnPoiClickListener, GoogleMap.OnCameraMoveListener {

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
    private static final float MAX_ZOOM = 20.0f;
    private static final float MIN_ZOOM = 15.0f;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation = null;
    // Location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // Fastest updates interval - 5 sec
    // Location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private boolean firstLocationUpdate = false;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Header
    private Toolbar toolbar;
    private Toolbar toolbarNavigate; //for navigate tab
    private NavigationView navigationView;
    private View navHeader;
    private LinearLayout navheaderbanner;

    // FAB
    FloatingActionButton fab;

    // Bottom sheet
    protected BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerView;
    View layer;

    //Database
    FirebaseFirestore db;

    // Bus stop
    // Key Roadname Value LTABusStopData Object
    private Map<String, LTABusStopData> allBusStops = new HashMap<>();
    // Key: Bus stop ID Value: Bus stop name
    private Map<String, String> allBusByID = new HashMap<>();
    // Key: Bus stop ID Value BusStopCards Object
    private Map<String, BusStopCards> busStopMap = new HashMap<>();
    // Sorted LTABusStopData
    private List<LTABusStopData> sortedLTABusStopData = new ArrayList<>();

    // Bus cards
    private ArrayList<Card> favCardList = new ArrayList<>(); // Favorite cards
    private ArrayList<Card> singleCardList = new ArrayList<>(); // single cards (POI)
    public ArrayList<Card> nearbyCardList = new ArrayList<>(); // NearbyList
    public ArrayList<String> favBusStopID;

    //Route cards
    private ArrayList<Card> transitCardList = new ArrayList<>(); // Public transport cards
    private ArrayList<Card> walkingCardList = new ArrayList<>(); // Walking cards

    // UserData
    UserData userData;

    // Map Markers
    private ClusterManager<MapMarkers> mClusterManager;
    private Map<String, MapMarkers> markerMap = new HashMap<>();

    // Recycler
    private CardAdapter adapter = null;
    private View rootView;

    private BottomNavigationView bottomNav;

    //Handler
    private final Handler handler = new Handler();
    private final Runnable runnable = () -> {
        setPooling(false);
    };

    private final Runnable runnable2 = () -> {
        bottomNav.setSelectedItemId(R.id.action_fav);
    };

    //Pooling limit
    private boolean pooling = false;
    private int receivedCards = 0;

    //Progress
    private ProgressBar progressBar;

    //Navigate
    boolean optionMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbarNavigate = findViewById(R.id.navigate_toolbar);
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
        getSupportActionBar().show();

        // Status bar :: Transparent
        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            window.setSustainedPerformanceMode(true);
        }

        fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(view -> {
            nearbyCardList.clear();
            updateBottomSheet();
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        progressBar = findViewById(R.id.progressBar);
        layer = findViewById(R.id.bg);

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
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
        db.disableNetwork();

        userData = new UserData();

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
        scheduleJob();
    }

    @Override
    protected void onRestart() {
        if (adapter != null)
            adapter.resumeHandlers();
        super.onRestart();
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mLocationPermissionGranted) {
            getDeviceLocation();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Window window = this.getWindow();
            window.setSustainedPerformanceMode(true);
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        if (adapter != null)
            adapter.pauseHandlers();
        super.onPause();

        if (mLocationPermissionGranted) {
            // pausing location updates
            stopLocationUpdates();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Window window = this.getWindow();
            window.setSustainedPerformanceMode(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent startServiceIntent = new Intent(this, NetworkSchedulerService.class);
        startService(startServiceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, NetworkSchedulerService.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setRequiresCharging(true)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void clearCardsForUpdate() {
        adapter.Clear();
    }

    private void updateBottomSheet() {
        //TODO Adjust bottomsheet to card length.
        adapter.Clear();
//        adapter.addAllCard(nearbyCardList);
    }

    private void prepareBottomSheet() {
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
//                        fab.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                        getSupportActionBar().show();
//                        fab.setVisibility(View.GONE);
                        layer.setVisibility(View.GONE);
                    } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                        getSupportActionBar().hide();
//                        fab.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    layer.setVisibility(View.VISIBLE);
                    layer.setAlpha(slideOffset);
//                    getSupportActionBar().hide();
                }
            });
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(true);
        adapter = new CardAdapter(getApplicationContext(), new ArrayList(), mMap, bottomSheetBehavior);
        adapter.doAutoRefresh();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
//        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 0);
//        recyclerView.setItemViewCacheSize(300000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        loadFavoritesFromDB();
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_fav) {
                toolbarNavigate.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar);
                getSupportActionBar().show();
                if (adapter != null)
                    setFavBusStopID(adapter.getFavBusStopID());

                if (favBusStopID.size() > 0 && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    progressBar.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    prepareFavoriteCards(getFavBusStopID());
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            } else if (id == R.id.action_nav) {
                //TODO fix searchview's icon covering search button
                fab.hide();
                toolbar.setVisibility(View.GONE);
                getSupportActionBar().hide();
                toolbarNavigate.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbarNavigate);
                getSupportActionBar().show();
                MultiAutoCompleteTextView startingPointTextView = findViewById(R.id.textViewStartingPoint);
                MultiAutoCompleteTextView destinationTextView = findViewById(R.id.textViewDestination);
                ImageButton optionButton = findViewById(R.id.optionButton);
                ImageButton searchButton = findViewById(R.id.searchButton);
                optionButton.setOnClickListener(view -> {
                    if (optionMode) {
                        optionButton.setBackgroundResource(R.drawable.ic_directions_bus_black_24dp); //transit
                        optionMode = false;
                    } else{
                        optionButton.setBackgroundResource(R.drawable.ic_baseline_directions_walk_24px); //walking
                        optionMode = true;
                    }
                });
                searchButton.setOnClickListener(v -> {
                Log.i(TAG,"onClickListener!");
                    if (!startingPointTextView.getText().toString().isEmpty() && !destinationTextView.getText().toString().isEmpty()) {
                        Log.i(TAG,"lookUpRoutes!");
                        String mode;
                        if (optionMode){
                            mode = "transit";
                        }else{
                            mode = "walking";
                        }
                        String query = "https://maps.googleapis.com/maps/api/directions/json?origin="
                                + startingPointTextView.getText().toString() + "&destination="
                                + destinationTextView.getText().toString()
                                + "&mode=" + mode + "&departure_time=1529577013" //for testing
                                + "&alternatives=true&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM";
                        Log.i(TAG,query);
                        lookUpRoutes(query);
                        hideKeyboard();

                    }else{
                        Toast.makeText(MainActivity.this,"Starting point and Destination cannot be empty!",Toast.LENGTH_LONG).show();
                    }
                });
                /*if (adapter != null){
                    transitCardList.clear();
                }*/
                //lookUpRoutes("https://maps.googleapis.com/maps/api/directions/json?origin=ClarkeQuay&destination=DhobyGhautMRT&mode=transit&alternatives=true&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM");

                if(walkingCardList.size() > 0 || transitCardList.size() > 0) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    updateAdapterList(transitCardList);
                    //handler.postDelayed(runnable, 3000);
                    //progressBar.setVisibility(View.VISIBLE);
                    //prepareFavoriteCards(getFavBusStopID());
                    //TODO prepare routes cards
                    //TODO default is transitCardList (childFragmentManager also here?)
                    //updateAdapterList(transitCardList);
                }
            } else if (id == R.id.action_nearby) {
                toolbarNavigate.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar);
                getSupportActionBar().show();
//                fab.show();
                if(mCurrentLocation==null){
                    getDeviceLocation();
                    getLocationPermission();
                    return false;
                }

                @SuppressLint("StaticFieldLeak")
                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        return cameraPosition;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition((CameraPosition) o));
                    }
                };

                asyncTask.execute();
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (!isPooling() && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    setPooling(true);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    lookUpNearbyBusStops();
                    handler.postDelayed(runnable, 3000);
                }
                else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            return true;
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantedResult = grantResults[i];
            if (permission.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantedResult == PackageManager.PERMISSION_GRANTED) {
                    getLocationPermission();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SettingsClient mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                if (!firstLocationUpdate) {
                    if (mCurrentLocation != null) {
                        firstLocationUpdate = true;
//                        if(bottomNav.getSelectedItemId() == R.id.action_fav)
//                            prepareFavoriteCards(getFavBusStopID());

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.setMaxZoomPreference(MAX_ZOOM);
                        mMap.setMinZoomPreference(MIN_ZOOM);
                    }
                }
                try {
                    if (mLocationPermissionGranted) {
                        mMap.setMyLocationEnabled(true);
                    } else {
                        mMap.setMyLocationEnabled(false);
                    }
                } catch (SecurityException e) {
                    Log.e("Exception: %s", e.getMessage());
                }
//                updateLocationUI();
//                lookUpNearbyBusStops();
            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();
        if (mLocationPermissionGranted) {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            Log.i(TAG, "All location settings are satisfied.");

//                            Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                            //noinspection MissingPermission
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                            "location settings ");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);

                                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }

                        }
                    });
//            lookUpNearbyBusStops();
            /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            long GPSLocationTime = 0;
            if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
//                    mCurrentLocation.setLatitude(location.getLatitude());
//                    mCurrentLocation.setLongitude(location.getLongitude());
                    mCurrentLocation = location;
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            if ( 0 < GPSLocationTime - NetLocationTime ) {
//                return locationGPS;
                Log.d(TAG, "getDeviceLocation: GPS is more accurate");
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            else {
//                return locationNet;
                Log.d(TAG, "getDeviceLocation: Network is more accurate");
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }*/
        }
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, task ->
                        Log.d(TAG, "stopLocationUpdates: called")
                );
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
        /*getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);*/
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (bottomNav.getSelectedItemId()==R.id.action_nav){
            toolbar.setVisibility(View.GONE);
            toolbar.setEnabled(false);

        }else{
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setEnabled(true);
        }
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bus) {
            // Handle the camera action
        } else if (id == R.id.nav_bus_stops) {

        } else if (id == R.id.nav_trainstations) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Log.d(TAG, "processFinishFromLTA: Looking up "+poi.name);
        /*if(allBusStops.containsKey(poi.name)) {
            String id = allBusStops.get(poi.name).getBusStopCode();
            BusStopCards card = getBusStopData(id);
            singleCardList.clear();
            singleCardList.add(card);
            updateAdapterList(singleCardList);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else{
            Log.e(TAG, "processFinishFromLTA: ERROR Missing data from LTA? : "+poi.name);
        }*/
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

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(1.3521, 103.8198))      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();

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

        mMap.setTrafficEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setAnimation(false);
//        mClusterManager.setAlgorithm(new PreCachingAlgorithmDecorator<>(new GridBasedAlgorithm<>()));

        mMap.setOnPoiClickListener(this);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMarkerClickListener(mClusterManager);

//        LatLngBounds SINGAPORE_BOUNDS = new LatLngBounds(new LatLng(1.22989115, 104.12058673),new LatLng(1.48525137, 103.57401691));


//        mMap.setLatLngBoundsForCameraTarget(SINGAPORE_BOUNDS);

        prepareBottomSheet();
        if(haveNetworkConnection(this)) {
            PrepareLTAData();
        }else{
            showNoNetworkDialog(this);
        }
    }
    @Override
    public void onCameraMove() {
//        layer.animate().alpha(0).setDuration(1000);
    }

    public boolean isPooling() {
        return pooling;
    }

    public void setPooling(boolean pooling) {
        this.pooling = pooling;
    }

    public ArrayList<Card> getFavCardList() {
        return favCardList;
    }

    public void setFavCardList(ArrayList<Card> favCardList) {
        this.favCardList = favCardList;
    }

    public ArrayList<String> getFavBusStopID() {
        return favBusStopID;
    }

    public void setFavBusStopID(ArrayList<String> favBusStopID) {
        this.favCardList.clear();
        this.favBusStopID = favBusStopID;
        userData.setFavBusStopID(favBusStopID);
        db.collection("user").document("userdata").set(userData);
    }

    private void loadFavoritesFromDB(){
        DocumentReference docRef = db.collection("user").document("userdata");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userData = document.toObject(UserData.class);
                        favBusStopID = userData.getFavBusStopID();
                        adapter.setFavBusStopID(favBusStopID);
//                        favBusStopID = (ArrayList) document.getData().get("favBusStopID");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void PrepareLTAData(){
        Log.d(TAG, "PrepareLTAData: Start");

//        CollectionReference citiesRef = db.collection("data");

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
//        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=5000");
        JSONLTABusStopParser ltaData = new JSONLTABusStopParser(MainActivity.this, urlsList);
        try {
            allBusStops.putAll(ltaData.execute().get());
            for (Map.Entry<String,LTABusStopData> newData : allBusStops.entrySet()){
                sortedLTABusStopData.add(newData.getValue());
            }
//            LinkIDtoName();
            FillBusData();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
                    sortedLTABusStopData.add(value);
                    Log.d(TAG, "doInBackground: LinkIDtoName");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.d(TAG, "onPostExecute: LinkIDtoName completed");
            }
        }.execute();
    }

    /**
     * <p>
     * This method creates and fills busStopMap with BusStopCard objects
     * busStopMap should be used to obtain the object you require.
     * DO NOT create more busstopcard objects
     */
    private void FillBusData(){
        ProgressDialog dialog = new ProgressDialog(this);
        /*
        Create Map markers!
         */
        @SuppressLint("StaticFieldLeak")
        @SuppressWarnings("unchecked")
        AsyncTask asyncTask = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading..");
                dialog.setIndeterminate(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dialog.dismiss();
//                bottomNav.setSelectedItemId(R.id.action_fav);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                for (Map.Entry<String, LTABusStopData> newData : allBusStops.entrySet()) {
//                    String key = newData.getKey();
                    LTABusStopData value = newData.getValue();

                    BusStopCards newStop = new BusStopCards();
                    String id = newData.getKey();
//                    String id = allBusStops.get(value.getDescription()).getBusStopCode();
                    newStop.setBusStopID(id);
                    newStop.setBusStopName(value.getDescription());
                    newStop.setBusStopLat(value.getBusStopLat());
                    newStop.setBusStopLong(value.getBusStopLong());
                    busStopMap.put(newStop.getBusStopID(), newStop);

                    MapMarkers infoWindowItem = new MapMarkers(Double.parseDouble(value.getBusStopLat()),
                            Double.parseDouble(value.getBusStopLong()), value.getDescription(), id);
//                    if (!mClusterManager.getClusterMarkerCollection().getMarkers().contains(infoWindowItem)) {
                    mClusterManager.addItem(infoWindowItem);
                    markerMap.put(value.getDescription(), infoWindowItem);
                    mClusterManager.setOnClusterItemClickListener(mapMarkers -> {
                        if (allBusStops.containsKey(mapMarkers.getSnippet())) {
                            Log.d(TAG, "FillBusData: Get Bus stop Data for "+mapMarkers.getTitle()+" "+mapMarkers.getSnippet());
                            BusStopCards card = getBusStopData(mapMarkers.getSnippet());
                            card.setType(card.BUS_STOP_CARD);
                            singleCardList.clear();
                            singleCardList.add(card);
                            updateAdapterList(singleCardList);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            Log.e(TAG, "FillBusData: ERROR Missing data from LTA? : " + mapMarkers.getTitle());
                        }
                        return false;
                    });
//                    }
                }

                return null;
            }
        }.execute();

        lookUpNearbyBusStops();
//        handler.postDelayed(runnable2, 5000);
    }

    /**
     * Returns an BusStopCard object
     * <p>
     * This method always returns immediately, whether or not the
     * card exists.
     *
     * @param  id busstopID
     * @return BusStopCards - BusStopCard object or null
     */
    private BusStopCards getBusStopData(String id){
        BusStopCards result = busStopMap.get(id);
        if(result == null){
            Log.e(TAG, "getBusStopData: No busStopMap!");
            return null;
        }

        // Bus stop favorites
        if(favBusStopID != null && favBusStopID.size() > 0 &&favBusStopID.contains(result.getBusStopID()))
            result.setFavorite(true);
        else
            result.setFavorite(false);

        // Pull bus stop data
        List<String> urlsList = new ArrayList<>();
        urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
        Log.d(TAG, "Look up bus timings for : " + result.getBusStopID());
        JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, result.getBusStopID());
        Map<String, Map> entry;
        try {
            entry = ltaReply.execute().get();
            for (Map.Entry<String, Map> entryData : entry.entrySet()) {
                String key = entryData.getKey(); // Bus stop ID
                Map value = entryData.getValue(); // Map with Bus to Timings
                BusStopCards card = busStopMap.get(key);
                card.setType(card.BUS_STOP_CARD);
                card.setMajorUpdate(true);
                Map<String, List<String>> finalData = new HashMap<>(value);
                for (List<String> newData : finalData.values()) {
                    String toConvertID = newData.get(0);
                    Log.d(TAG, "getBusStopData: toConvertID " + toConvertID);
                    if(allBusStops.get(toConvertID).getRoadName() != null)
                        newData.set(3, allBusStops.get(toConvertID).getDescription());
                }
                result.setBusServices(finalData);
                result.setLastUpdated(Calendar.getInstance().getTime().toString());

                Log.d(TAG, "getBusStopData: Bus stop ID:" + key
                        + " Bus Stop Name: " + card.getBusStopName()
                        + " - " + card.getBusServices() + " - Last Updated: "
                        + Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Creates and fills up nearbyCardList array
     * <p>
     * This method always returns immediately, whether or not the
     * card exists.
     * nearbyCardList array is used to swap into adapter to display nearby bus stop
     */
    private void lookUpNearbyBusStops(){
        if(mCurrentLocation==null)
            return;

        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
//                sortLocations(sortedLTABusStopData, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                return sortLocations(sortedLTABusStopData, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                List<LTABusStopData> toProcess = (List<LTABusStopData>) o;
                if(toProcess.size() <= 0){
                    Log.d(TAG, "lookUpNearbyBusStops: Google returned no data");
                    return;
                }
                nearbyCardList.clear();
                for(int i=0; i< 11; i++) {
                    BusStopCards card = getBusStopData(toProcess.get(i).getBusStopCode());
                    card.setType(card.BUS_STOP_CARD);
                    card.setMajorUpdate(true);
                    nearbyCardList.add(card);
//            Log.d(TAG, "lookUpNearbyBusStops: adding "+card.getBusStopID()+ " to nearbyCardList");
                    assert card != null;
                    Log.d(TAG, "lookUpNearbyBusStops: "+card.toString());
                }
                updateAdapterList(nearbyCardList);
            }
        };
        asyncTask.execute();
    }

    public static List<LTABusStopData> sortLocations(List<LTABusStopData> locations, final double myLatitude,final double myLongitude) {

        Comparator comp = (Comparator<LTABusStopData>) (o, o2) -> {
            float[] result1 = new float[3];
            Location.distanceBetween(myLatitude, myLongitude, Double.parseDouble(o.getBusStopLat()), Double.parseDouble(o.getBusStopLong()), result1);
            Float distance1 = result1[0];

            float[] result2 = new float[3];
            Location.distanceBetween(myLatitude, myLongitude, Double.parseDouble(o2.getBusStopLat()), Double.parseDouble(o2.getBusStopLong()), result2);
            Float distance2 = result2[0];
            return distance1.compareTo(distance2);
        };
        long start = System.currentTimeMillis();
        Log.d(TAG, "sortLocations: BEGIN SORTING!");
        Collections.sort(locations, comp);
        long elapsedTime = System.currentTimeMillis() - start;
        Log.d(TAG, "sortLocations: COMPLETED SORTING! "+elapsedTime+"ms");
        return locations;
    }

    public static List<Card> sortCardsByLocation(List<Card> locations, final double myLatitude,final double myLongitude) {
        Comparator comp = (Comparator<BusStopCards>) (o, o2) -> {
            float[] result1 = new float[3];
            Location.distanceBetween(myLatitude, myLongitude, Double.parseDouble(o.getBusStopLat()), Double.parseDouble(o.getBusStopLong()), result1);
            Float distance1 = result1[0];

            float[] result2 = new float[3];
            Location.distanceBetween(myLatitude, myLongitude, Double.parseDouble(o2.getBusStopLat()), Double.parseDouble(o2.getBusStopLong()), result2);
            Float distance2 = result2[0];
            return distance1.compareTo(distance2);
        };
        long start = System.currentTimeMillis();
        Log.d(TAG, "sortLocations: BEGIN SORTING!");
        Collections.sort(locations, comp);
        long elapsedTime = System.currentTimeMillis() - start;
        Log.d(TAG, "sortLocations: COMPLETED SORTING! "+elapsedTime+"ms");
        return locations;
    }

    /**
     * Clears and adds the list into the adapter to be displayed
     * <p>
     * This method returns nothing
     *
     * @param list ArrayList<Card>
     */
    private void updateAdapterList(ArrayList<? extends Card> list){
        clearCardsForUpdate();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        adapter.notifyDataSetChanged();
        adapter.addAllCard(list);
        adapter.doAutoRefresh();
        progressBar.setVisibility(View.GONE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void prepareFavoriteCards(ArrayList<String> list){
        if(list.size() < 1){
            Log.e(TAG, "prepareFavoriteCards: list is empty!");
            return;
        }

        for(int i=0; i< list.size(); i++) {
            BusStopCards card = getBusStopData(list.get(i));
            card.setType(card.BUS_STOP_CARD);
            card.setMajorUpdate(true);
            favCardList.add(card);
            Log.d(TAG, "prepareFavoriteCards: adding "+card.getBusStopID()+ " to favCardList");
//            }
        }

        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                clearCardsForUpdate();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                if(mCurrentLocation != null)
//                sortLocations(sortedLTABusStopData, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    return sortCardsByLocation(favCardList, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                else
                    return favCardList;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                updateAdapterList(favCardList);
            }
        };
        asyncTask.execute();

//        updateAdapterList(favCardList);
    }

    private void lookUpRoutes(String query){
        List<String> directionsQuery = new ArrayList<>();
        directionsQuery.add(query);
        Log.i(TAG,directionsQuery.toString());
        JSONGoogleDirectionsParser directionsParser = new JSONGoogleDirectionsParser(MainActivity.this,directionsQuery);
        List<GoogleRoutesData> result; //= new ArrayList<GoogleRoutesData>(); //result from parser
        try {
            result = directionsParser.execute().get();
            Log.d(TAG,query);
            if(result.size() <= 0){
                Log.d(TAG, "lookUpRoute: Google returned no data");
                return;
            }
            transitCardList.clear();
            for(int i=0; i< result.size(); i++) {
                NavigateTransitCard card = getRouteData(result.get(i));
                card.setType(card.NAVIGATE_TRANSIT_CARD);
                transitCardList.add(card);
//            Log.d(TAG, "lookUpNearbyBusStops: adding "+card.getBusStopID()+ " to nearbyCardList");
                Log.d(TAG, "lookUpRoute: "+card.toString());
            }

            lookUpTrafficDuration("https://maps.googleapis.com/maps/api/distancematrix/json?origins=1.2996781,103.8557064&destinations=1.3164326,103.8829187&departure_time=now&key=AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s", "https://maps.googleapis.com/maps/api/directions/json?origin=825+tampines&destination=dhoby+ghaut&departure_time=1529577013&mode=transit&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM");
            updateAdapterList(transitCardList);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void lookUpTrafficDuration(String queryMatrix, String queryDir){
        List<String> durationQuery = new ArrayList<>();
        durationQuery.add(queryMatrix);
        List<String> directionsQuery = new ArrayList<>();
        directionsQuery.add(queryDir);
        Log.i(TAG,durationQuery.toString());
        JSONDistanceMatrixParser durationParser = new JSONDistanceMatrixParser(MainActivity.this,durationQuery);
        List<DistanceData> result1;  //result from parser
        JSONGoogleDirectionsParser directionsParser = new JSONGoogleDirectionsParser(MainActivity.this,directionsQuery);
        List<GoogleRoutesData> result; //= new ArrayList<GoogleRoutesData>(); //result from parser
        try {
            result = directionsParser.execute().get();
            result1 = durationParser.execute().get();
            Log.d(TAG,queryMatrix);
            if(result1.size() <= 0){
                Log.d(TAG, "lookUpTrafficDuration: Google returned no data");
                return;
            }
            Log.d(TAG, "lookUpTrafficDuration: Google returned DM " + result1.size() + " data.");
            Log.d(TAG, "lookUpTrafficDuration: Google returned DG " + result.size() + " data.");
            for(int i=0; i< result.size(); i++) {
                NavigateTransitCard card = getDistanceMatrix(result1.get(i), result.get(i));
                card.setType(card.NAVIGATE_TRANSIT_CARD);
                transitCardList.add(card);
                Log.d(TAG, "lookUpTrafficDuration: " + card.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private NavigateTransitCard getDistanceMatrix(DistanceData distanceData, GoogleRoutesData googleRoutesData) {
        //TODO set card details here (route ID?)
        NavigateTransitCard card = new NavigateTransitCard();
        card.setType(card.NAVIGATE_TRANSIT_CARD);
        //card.setTotalDistance(googleRoutesData.getTotalDistance());
        //card.setTotalTime(googleRoutesData.getTotalDuration());
        //need loop to get
        List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();

        Log.d(TAG, "distanceData start add: "+distanceData.getStartAdd());
        Log.d(TAG, "distanceData distance: "+distanceData.getDistance());
        Log.d(TAG, "distanceData duration: "+distanceData.getDuration());
        Log.d(TAG, "routeSteps duration: "+ routeSteps.get(0).getDuration());
        int duration = Integer.parseInt(distanceData.getDuration().replaceAll("[^0-9]", ""));
        int duration_in_traffic = Integer.parseInt(distanceData.getDuration_in_traffic().replaceAll("[^0-9]", ""));
        Log.d(TAG, "duration int: "+ duration );
        if ((duration_in_traffic - duration) < 10){
            Log.d(TAG, "No congestion" );
        }
        if (routeSteps != null) {
            for (int i = 0; i < routeSteps.size(); i++) {
                if (routeSteps.get(i).getTravelMode().equals("TRANSIT") && routeSteps.get(i).getTrainLine()!= null ) {
                    Log.d(TAG, "IS A TRAIN" );
                }
                else if (routeSteps.get(i).getTravelMode().equals("TRANSIT") && routeSteps.get(i).getBusNum()!= null ) {
                    Log.d(TAG, "IS A BUS" );
                    
                }
            }
        }
        else{
            Log.d(TAG, "routeSteps EMPTY" );
        }
        return card;
    }

    private NavigateTransitCard getRouteData(GoogleRoutesData googleRoutesData){
        //TODO set card details here (route ID?)
        NavigateTransitCard card = new NavigateTransitCard();
        card.setType(card.NAVIGATE_TRANSIT_CARD);
        card.setTotalDistance(googleRoutesData.getTotalDistance());
        card.setTotalTime(googleRoutesData.getTotalDuration());
        //need loop to get
        List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
        if (routeSteps != null) {
            for (int i = 0; i < routeSteps.size(); i++) {
                if (routeSteps.get(i).getTravelMode().equals("TRANSIT") && i < 2 &&
                        (!routeSteps.get(i).getTravelMode().equals("TRANSIT") ||
                                routeSteps.get(i) == null)) {
                    //first public transport station
                    card.setStartingStation(routeSteps.get(i).getDepartureStop());
                    card.setNumStops("( "+String.valueOf(routeSteps.get(i).getNumStops())+" stops)");
                    card.setTimeTaken(routeSteps.get(i).getDuration());
                    card.setImageViewStartingStation(R.drawable.ic_directions_bus_black_24dp);
                    String trainLine = routeSteps.get(i).getTrainLine();
                    if (trainLine != null) {
                        switch (trainLine) {
                            case "Downtown Line":
                                card.setImageViewStartingStationColor(Color.argb(255,1, 87, 155));
                                break;
                            case "North East Line":
                                card.setImageViewStartingStationColor(Color.argb(255,72, 35, 175));
                                break;
                            case "East West Line":
                                card.setImageViewStartingStationColor(Color.argb(255,36, 130, 37));
                                break;
                            case "North South Line":
                                card.setImageViewStartingStationColor(Color.argb(255,244, 65, 65));
                                break;
                            case "Circle Line":
                                card.setImageViewStartingStationColor(Color.argb(255,244, 226, 66));
                                break;
                        }
                    }
                    //card.setTransferStation(routeSteps.get(i).getArrivalStop());

                }
                if (routeSteps.get(i).getTravelMode().equals("TRANSIT")) {
                    //last public transport station
                    card.setEndingStation(routeSteps.get(i).getArrivalStop());
                    /*if (card.getTransferStation().equals(card.getEndingStation())){
                        card.setTransferStation(null);
                    }*/
                    String trainLineEnd = routeSteps.get(i).getTrainLine();
                    if (trainLineEnd != null) {
                        switch (trainLineEnd) {
                            case "Downtown Line":
                                card.setImageViewEndingStationColor(Color.argb(255,1, 87, 155));
                                break;
                            case "North East Line":
                                card.setImageViewEndingStationColor(Color.argb(255,72, 35, 175));
                                break;
                            case "East West Line":
                                card.setImageViewEndingStationColor(Color.argb(255,36, 130, 37));
                                break;
                            case "North South Line":
                                card.setImageViewEndingStationColor(Color.argb(255,244, 65, 65));
                                break;
                            case "Circle Line":
                                card.setImageViewEndingStationColor(Color.argb(255,244, 226, 66));
                                break;
                        }
                    }

                }
                card.setCost("$X.XX");
            }
        }
        return card;
    }
    //TODO getRouteData for NavigateWalkingCard


    public void hideKeyboard(){
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*
    ALL BUS STOPS FROM LTA

    // Pull bus stop data
    List<String> urlsList = new ArrayList<>();
    urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
    Log.d(TAG, "Look up bus timings for : " + newStop.getBusStopID());
    JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, newStop.getBusStopID());
    ltaReply.delegate = MainActivity.this;
    ltaReply.execute();



    //Update bus time
    for (Map.Entry<String, Map> entry : result.entrySet()) {
                String key = entry.getKey(); // Bus stop ID
                Map value = entry.getValue(); // Map with Bus to Timings
                BusStopCards card = busStopMap.get(key);

                Map<String, List<String>> finalData = new HashMap<>(value);
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


    // Get nearby
    List<GoogleBusStopData> result = new AsyncTask() {
    @Override
    protected Object doInBackground(Object[] objects) {
        List<String> urlsList = new ArrayList<>();
        urlsList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude() + "&rankby=distance&type=transit_station&key=AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s");
        Log.d(TAG, "FindNearbyBusStop: " + urlsList.get(0));
        JSONGoogleNearbySearchParser googleReply = new JSONGoogleNearbySearchParser(MainActivity.this, urlsList);
        googleReply.execute();
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
}.execute().get();
     */



}
