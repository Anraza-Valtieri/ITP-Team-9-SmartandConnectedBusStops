package com.sit.itp_team_9_smartandconnectedbusstops;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.CardAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.MapMarkers;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.UserData;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusStopParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnPoiClickListener, GoogleMap.OnCameraMoveListener{

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
    private ArrayList<BusStopCards> favCardList = new ArrayList<>(); // Favorite cards
    private ArrayList<BusStopCards> singleCardList = new ArrayList<>(); // single cards (POI)
    public ArrayList<BusStopCards> nearbyCardList = new ArrayList<>(); // NearbyList
    public ArrayList<String> favBusStopID;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
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
    }

    @Override
    protected void onRestart() {
        if(adapter != null)
            adapter.resumeHandlers();
        super.onRestart();
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mLocationPermissionGranted) {
            getDeviceLocation();
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        if(adapter != null)
            adapter.pauseHandlers();
        super.onPause();

        if (mLocationPermissionGranted) {
            // pausing location updates
            stopLocationUpdates();
        }
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
//        adapter.addAllCard(nearbyCardList);
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
                        fab.setVisibility(View.GONE);
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                        getSupportActionBar().show();
                        fab.setVisibility(View.GONE);
                        layer.setVisibility(View.GONE);
                    } else if (BottomSheetBehavior.STATE_EXPANDED == newState){
                        getSupportActionBar().hide();
                        fab.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    layer.setVisibility(View.VISIBLE);
                    layer.setAlpha(slideOffset);
                    getSupportActionBar().hide();
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

        loadFavoritesFromDB();
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (id == R.id.action_fav) {
                if (adapter != null)
                    setFavBusStopID(adapter.getFavBusStopID());

                if(favBusStopID.size() > 0) {
                    clearCardsForUpdate();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    progressBar.setVisibility(View.VISIBLE);
                    prepareFavoriteCards(getFavBusStopID());
                }
            } else if (id == R.id.action_nav) {
                fab.hide();
                clearCardsForUpdate();
            } else if (id == R.id.action_nearby) {
//                fab.show();
                if(mCurrentLocation==null){
                    getDeviceLocation();
                    getLocationPermission();
                    return false;
                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                        .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                if(!isPooling()) {
                    setPooling(true);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    lookUpNearbyBusStops();
                    clearCardsForUpdate();
                    progressBar.setVisibility(View.VISIBLE);
                    updateAdapterList(nearbyCardList);
                    handler.postDelayed(runnable, 3000);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        for(int i=0; i<permissions.length; i++) {
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
                if(!firstLocationUpdate){
                    if(mCurrentLocation!=null){
                        firstLocationUpdate = true;
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }
                try {
                    if (mLocationPermissionGranted) {
                        mMap.setMyLocationEnabled(true);
                    } else {
                        mMap.setMyLocationEnabled(false);
                    }
                } catch (SecurityException e)  {
                    Log.e("Exception: %s", e.getMessage());
                }
//                updateLocationUI();
                lookUpNearbyBusStops();
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
        mClusterManager.setAlgorithm(new PreCachingAlgorithmDecorator<>(new GridBasedAlgorithm<>()));

        mMap.setOnPoiClickListener(this);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setMaxZoomPreference(MAX_ZOOM);
        mMap.setMinZoomPreference(MIN_ZOOM);
        prepareBottomSheet();
        PrepareLTAData();
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

    public ArrayList<BusStopCards> getFavCardList() {
        return favCardList;
    }

    public void setFavCardList(ArrayList<BusStopCards> favCardList) {
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
                bottomNav.setSelectedItemId(R.id.action_fav);
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

                Map<String, List<String>> finalData = new HashMap<>(value);
                for (List<String> newData : finalData.values()) {
                    String toConvertID = newData.get(3);
                    Log.d(TAG, "getBusStopData: toConvertID " + toConvertID);
                    if(allBusStops.get(toConvertID).getRoadName() != null)
                        newData.set(3, allBusStops.get(toConvertID).getRoadName());
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
        List<LTABusStopData> result = sortLocations(sortedLTABusStopData, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if(result.size() <= 0){
            Log.d(TAG, "lookUpNearbyBusStops: Google returned no data");
            return;
        }
        nearbyCardList.clear();
        for(int i=0; i< 10; i++) {
            BusStopCards card = getBusStopData(result.get(i).getBusStopCode());
            nearbyCardList.add(card);
//            Log.d(TAG, "lookUpNearbyBusStops: adding "+card.getBusStopID()+ " to nearbyCardList");
            Log.d(TAG, "lookUpNearbyBusStops: "+card.toString());
        }
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

    /**
     * Clears and adds the list into the adapter to be displayed
     * <p>
     * This method returns nothing
     *
     * @param list ArrayList<BusStopCards>
     */
    private void updateAdapterList(ArrayList<BusStopCards> list){
        clearCardsForUpdate();
        adapter.notifyDataSetChanged();
        adapter.addAllCard(list);
        adapter.doAutoRefresh();
        progressBar.setVisibility(View.GONE);
    }

    private void prepareFavoriteCards(ArrayList<String> list){
        if(list.size() < 1){
            Log.e(TAG, "prepareFavoriteCards: list is empty!");
            return;
        }

        for(int i=0; i< list.size(); i++) {
                BusStopCards card = getBusStopData(list.get(i));
                favCardList.add(card);
                Log.d(TAG, "prepareFavoriteCards: adding "+card.getBusStopID()+ " to favCardList");
//            }
        }
        updateAdapterList(favCardList);
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
