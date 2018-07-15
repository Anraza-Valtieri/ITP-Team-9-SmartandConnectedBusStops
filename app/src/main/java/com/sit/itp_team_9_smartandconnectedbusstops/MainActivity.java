package com.sit.itp_team_9_smartandconnectedbusstops;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.CardAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.PlaceAutoCompleteAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes.JSONLTABusRoute;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.Authenticated;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.Card;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.DistanceData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.GoogleRoutesSteps;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.LTABusStopData;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.MapMarkers;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateTransitCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateWalkingCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.SGWeather;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.UserData;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONDistanceMatrixParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONGoogleDirectionsParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusStopParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.Services.NetworkSchedulerService;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils.haveNetworkConnection;
import static com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils.showNoNetworkDialog;

//import com.sit.itp_team_9_smartandconnectedbusstops.Model.AdultFares;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnPoiClickListener, GoogleMap.OnCameraMoveListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;

    // A default location (Singapore, Singapore) and default zoom to use when location permission is
    // not granted.
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
    private static final String UUID_ID = "uuid_id";
    private String UUIDStr = "";

    // Header
    private Toolbar toolbar;
    private Toolbar toolbarNavigate; //for navigate tab
    private NavigationView navigationView;
    private View navHeader;
    private LinearLayout navheaderbanner;
    private ActionBarDrawerToggle toggle;

    // Bottom sheet
    protected BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerView;
    View layer;

    // Navigation Toolbar
    private AutoCompleteTextView startingPointTextView;
    private AutoCompleteTextView destinationTextView;

    // Loading screen
    private ConstraintLayout loadingScreen;

    //Database
    FirebaseFirestore db;

    // Bus stop
    // Key Roadname Value LTABusStopData Object
    private Map<String, LTABusStopData> allBusStops = new HashMap<>();
    // Key: Bus stop ID Value: Bus stop name
    public static Map<String, String> allBusByID = new HashMap<>();
    // Key: Bus stop ID Value BusStopCards Object
    private Map<String, BusStopCards> busStopMap = new HashMap<>();
    // Sorted LTABusStopData
    private List<LTABusStopData> sortedLTABusStopData = new ArrayList<>();

    // Bus cards
    private ArrayList<Card> favCardList = new ArrayList<>(); // Favorite cards
    private ArrayList<Card> singleCardList = new ArrayList<>(); // single cards (POI)
    public ArrayList<Card> nearbyCardList = new ArrayList<>(); // NearbyList
    public ArrayList<String> favBusStopID = new ArrayList<>();

    //Route cards
    private ArrayList<? super Card> transitCardList = new ArrayList<>(); // Public transport cards
    private ArrayList<Card> walkingCardList = new ArrayList<>(); // Walking cards

    // Bus Routes
    private JSONLTABusRoute busRoute;

    // UserData
    UserData userData;

    // Map Markers
    private ClusterManager<MapMarkers> mClusterManager;
    private Map<String, MapMarkers> markerMap = new HashMap<>();

    // Recycler
    private CardAdapter adapter = null;

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

    //Progress
    private ProgressBar progressBar;

    //Navigate
    boolean optionMode = true;

    FloatingActionButton fab;


    //PlaceAutoCompleteAdapter
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private AutocompleteFilter autoCompleteFilter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-1.3520828333333335, -103.81983583333334), new LatLng(1.3520828333333335, 103.8198358333334));

    //Shared Preference for Language Alert Dialog
    private static final String SELECTED_ITEM = "SelectedItem";
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor sharedPrefEditor;

    // Context Variable for classes
    public static Context context = null;

    // Address for starting point
    String convertedAddress;

    //direction query
    String query;
    String mrtLine;
    //Twitter username of Mrt updates
    final static String ScreenName = "SMRT_Singapore";
    List<String> twitterList = new ArrayList<String>();
    // Weather
    private SGWeather sgWeather;
    TextView location;
    TextView weather;
    TextView temperature;
    TextView psi25;
    TextView psi10;
    TextView uv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_NoActionBar);
//        setContentView(R.layout.loadingscreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        toolbarNavigate = findViewById(R.id.navigate_toolbar);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 0);
        toolbarNavigate.setLayoutParams(params);
//        setSupportActionBar(toolbarNavigate);
        setSupportActionBar(toolbar);
        View rootView = findViewById(R.id.includeroot);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        navheaderbanner = navHeader.findViewById(R.id.headerbanner);
        weather = navHeader.findViewById(R.id.tvWeather);
        temperature = navHeader.findViewById(R.id.tvTemperature);
        location = navHeader.findViewById(R.id.tvLocation);
        psi25 = navHeader.findViewById(R.id.tvPSI25);
        psi10 = navHeader.findViewById(R.id.tvPSI10);
        uv = navHeader.findViewById(R.id.tvUV);
        loadingScreen = findViewById(R.id.splashscreen);
        // Toolbar :: Transparent
//        toolbar.setBackgroundColor(Color.TRANSPARENT);

        startingPointTextView = findViewById(R.id.textViewStartingPoint);
        destinationTextView = findViewById(R.id.textViewDestination);

        SharedPreferences prefs = getSharedPreferences(UUID_ID, MODE_PRIVATE);
        String restoredText = prefs.getString("UUID", null);
        if (restoredText != null) {
            UUIDStr = restoredText;
        }else{
            SharedPreferences.Editor editor = getSharedPreferences(UUID_ID, MODE_PRIVATE).edit();
            String id = UUID.randomUUID().toString();
            editor.putString("UUID", id);
            editor.apply();
            UUIDStr = id;
        }

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

        // Status bar :: Transparent
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setNavigationBarColor(Color.WHITE);
            window.setStatusBarColor(Color.WHITE);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int flags = window.getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            window.getDecorView().setSystemUiVisibility(flags);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            window.setSustainedPerformanceMode(true);
        }

        bottomNav = findViewById(R.id.bottom_navigation);
        progressBar = findViewById(R.id.progressBar);
        layer = findViewById(R.id.bg);

        layer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return false;
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
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
//        db.disableNetwork();

        userData = new UserData();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                        if(sgWeather != null && mCurrentLocation != null) {
                            sgWeather.updateLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

                            handler.postDelayed(() -> location.setText( sgWeather.getmLocation()),500);
                            handler.postDelayed(() -> weather.setText(sgWeather.getmWeatherForecast()),500);
                            handler.postDelayed(() -> temperature.setText(sgWeather.getmTemperature()+getString(R.string.degree)),500);
                            handler.postDelayed(() -> psi25.setText("PM2.5: "+sgWeather.getmPM25()),500);
                            handler.postDelayed(() -> psi10.setText("PM10: "+sgWeather.getmPM10()),500);
                            handler.postDelayed(() -> uv.setText(getString(R.string.uvIndex)+": "+sgWeather.getmUV()),500);
                        }else{
                            location.setText("Updating..");
                            weather.setText("-");
                            temperature.setText("-°C");
                            psi25.setText("PM2.5: -");
                            psi10.setText("PM10: -");
                            uv.setText("UV Index: -");

                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes

                    }
                }
        );

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak")
                AsyncTask asyncTask = new AsyncTask() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if(mCurrentLocation == null)
                            return;
                    }

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        if(mCurrentLocation == null)
                            return null;

                        return new CameraPosition.Builder()
                                .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(o != null)
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition((CameraPosition) o));
                    }
                };

                asyncTask.execute();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

//        View mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        scheduleJob();

        mGeoDataClient = Places.getGeoDataClient(this, null);
        autoCompleteFilter = new AutocompleteFilter.Builder().setCountry("SG").build();
        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(MainActivity.this, mGeoDataClient, LAT_LNG_BOUNDS, autoCompleteFilter);

        downloadTweets();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (adapter != null)
            adapter.resumeHandlers();
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mLocationPermissionGranted) {
            getDeviceLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (adapter != null)
            adapter.pauseHandlers();

        if (mLocationPermissionGranted) {
            // pausing location updates
            stopLocationUpdates();
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
        assert jobScheduler != null;
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
                        if(toolbarNavigate.isShown())
                            hideActionBar(toolbarNavigate);
                        if(toolbar.isShown())
                            hideActionBar(toolbar);
                        fab.hide();
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                        Objects.requireNonNull(getSupportActionBar()).show();
                        if(bottomNav.getSelectedItemId() == R.id.action_nav && !getSupportActionBar().isShowing())
                            showActionBar(toolbarNavigate);
                        if(bottomNav.getSelectedItemId() != R.id.action_nav && !getSupportActionBar().isShowing())
                            showActionBar(toolbar);
                        layer.setVisibility(View.GONE);
                        fab.show();
                    } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                        if(toolbarNavigate.isShown())
                            hideActionBar(toolbarNavigate);
                        if(toolbar.isShown())
                            hideActionBar(toolbar);
//                        Objects.requireNonNull(getSupportActionBar()).hide();
                        fab.hide();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    layer.setVisibility(View.VISIBLE);
                    layer.setAlpha(slideOffset);
                    fab.hide();
//                    fab.setSize(1-(int)slideOffset);
                }
            });
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CardAdapter(getApplicationContext(), new ArrayList(), mMap, bottomSheetBehavior, recyclerView);
        adapter.doAutoRefresh();

        adapter.setOnFavoriteClickListener(favBusStopID -> setFavBusStopID(favBusStopID));

        recyclerView.setAdapter(adapter);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        loadFavoritesFromDB();
        bottomNav.setOnNavigationItemSelectedListener((MenuItem item) -> {
            int id = item.getItemId();
            if (id == R.id.action_fav) {

//                getSupportActionBar().hide();
//                setSupportActionBar(toolbar);
//                getSupportActionBar().show();
                if(toolbar.getVisibility() != View.VISIBLE) {
                    hideActionBar(toolbar);
                    handler.postDelayed(() -> showActionBar(toolbar), 350);
                }

                hideKeyboard();
//                if (adapter != null)
//                    setFavBusStopID(adapter.getFavBusStopID());

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                if (favBusStopID.size() > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    recyclerView.scrollToPosition(0);
                    handler.postDelayed(() -> prepareFavoriteCards(getFavBusStopID()), 600);
                }
            } else if (id == R.id.action_nav) {
                if(toolbarNavigate.getVisibility() != View.VISIBLE) {
                    hideActionBar(toolbar);
                    handler.postDelayed(() -> showActionBar(toolbarNavigate), 350);
                }

//                if (adapter != null)
//                    setFavBusStopID(adapter.getFavBusStopID());

                Spinner fareTypesSpinner = (Spinner) findViewById(R.id.fare_type_spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.fare_types_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                fareTypesSpinner.setAdapter(adapter);

                Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_by_spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(this,
                        R.array.sort_by_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                sortBySpinner.setAdapter(adapterSort);

                sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            ArrayList<? extends Card> navigateCardList = new ArrayList<NavigateTransitCard>();
                            navigateCardList = (ArrayList<? extends Card>) transitCardList;
                            List<NavigateTransitCard> castToNavigate = (List<NavigateTransitCard>) navigateCardList;

                            switch (sortBySpinner.getSelectedItem().toString()) {
                                case "Least time":
                                    Collections.sort(castToNavigate, NavigateTransitCard.timeComparator);
                                    updateAdapterList((ArrayList<? extends Card>) castToNavigate);
                                    break;
                                case "Least distance":
                                    Collections.sort(castToNavigate, NavigateTransitCard.distanceComparator);
                                    //transitCardList = (ArrayList<? super Card>) castToNavigate;
                                    updateAdapterList((ArrayList<? extends Card>) castToNavigate);
                                    break;
                                case "Least walking":
                                    Collections.sort(castToNavigate, NavigateTransitCard.walkingDistanceComparator);
                                    updateAdapterList((ArrayList<? extends Card>) castToNavigate);
                                    break;
                            }
                        }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                startingPointTextView.setAdapter(mPlaceAutoCompleteAdapter);
                destinationTextView.setAdapter(mPlaceAutoCompleteAdapter);
                //ImageButton switchButton = findViewById(R.id.switchButton);
                ImageButton startVoiceSearchButton = findViewById(R.id.imgBtnStartVoiceSearch);
                ImageButton destVoiceSearchButton = findViewById(R.id.imgBtnDestVoiceSearch);
                ImageButton switchButton = findViewById(R.id.switchButton);
                ImageButton searchButton = findViewById(R.id.searchButton);
                TabLayout navigationTabs = findViewById(R.id.tabLayout);

                handler.postDelayed(() -> {
                    startingPointTextView.setSelectAllOnFocus(true);
                    startingPointTextView.requestFocus();
                    showKeyboard(startingPointTextView);
                },600);

                startingPointTextView.setText(convertedAddress);

                handler.postDelayed(() -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN), 100);

                destinationTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (!startingPointTextView.getText().toString().isEmpty() && !destinationTextView.getText().toString().isEmpty()) {
                                Log.i(TAG,"lookUpRoutes!");
                                String mode ="";
                                if (navigationTabs.getSelectedTabPosition() == 0){
                                    mode = "transit";
                                    optionMode = true;
                                }
                                else if(navigationTabs.getSelectedTabPosition() == 1){
                                    mode = "walking";
                                    optionMode = false;
                                }
                                String query = "https://maps.googleapis.com/maps/api/directions/json?origin="
                                        + startingPointTextView.getText().toString() + "&destination="
                                        + destinationTextView.getText().toString()
                                        + "&mode=" + mode //+ "&departure_time=1529577013" //for testing
                                        + "&alternatives=true&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM";
                                //lookUpRoutes("https://maps.googleapis.com/maps/api/directions/json?origin=ClarkeQuay&destination=DhobyGhautMRT&mode=transit&alternatives=true&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM");
                                Log.i(TAG,query);
                                hideKeyboard();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                lookUpRoutes(query, fareTypesSpinner.getSelectedItem().toString(), sortBySpinner.getSelectedItem().toString());

                            }else{
                                Toast.makeText(MainActivity.this,"Starting point and Destination cannot be empty!",Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                        return false;
                    }
                });

                startVoiceSearchButton.setOnClickListener(v -> {
                    promptSpeechInput(100);
                });

                destVoiceSearchButton.setOnClickListener(v -> {
                    promptSpeechInput(200);
                });

                switchButton.setOnClickListener(v -> {
                    String startPoint = startingPointTextView.getText().toString();
                    startingPointTextView.setText(destinationTextView.getText());
                    destinationTextView.setText(startPoint);
                });

                searchButton.setOnClickListener(v -> {
                Log.i(TAG,"onClickListener!");
                    if (!startingPointTextView.getText().toString().isEmpty() && !destinationTextView.getText().toString().isEmpty()) {
                        Log.i(TAG,"lookUpRoutes!");
                        String mode = "";
                        if (navigationTabs.getSelectedTabPosition() == 0){
                            mode = "transit";
                            optionMode = true;
                        }
                        else if(navigationTabs.getSelectedTabPosition() == 1){
                            mode = "walking";
                            optionMode = false;
                        }
                         query = "https://maps.googleapis.com/maps/api/directions/json?origin="
                                + startingPointTextView.getText().toString() + "&destination="
                                + destinationTextView.getText().toString()
                                + "&mode=" + mode //+ "&departure_time=1529577013" //for testing
                                + "&alternatives=true&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM";
                        //lookUpRoutes("https://maps.googleapis.com/maps/api/directions/json?origin=ClarkeQuay&destination=DhobyGhautMRT&mode=transit&alternatives=true&key=AIzaSyBhE8bUHClkv4jt5FBpz2VfqE8MJeN5IaM");
                        Log.i(TAG,query);
                        hideKeyboard();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        lookUpRoutes(query, fareTypesSpinner.getSelectedItem().toString(), sortBySpinner.getSelectedItem().toString());

                    }else{
                        Toast.makeText(MainActivity.this,"Starting point and Destination cannot be empty!",Toast.LENGTH_LONG).show();
                    }
                });
            } else if (id == R.id.action_nearby) {
//                getSupportActionBar().hide();
//                setSupportActionBar(toolbar);
//                getSupportActionBar().show();
                if(toolbar.getVisibility() != View.VISIBLE) {
                    hideActionBar(toolbarNavigate);
                    handler.postDelayed(() -> showActionBar(toolbar), 350);
                }

                if(mCurrentLocation==null){
                    getDeviceLocation();
                    getLocationPermission();
                    return false;
                }

                hideKeyboard();

                @SuppressLint("StaticFieldLeak")
                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        return new CameraPosition.Builder()
                                .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition((CameraPosition) o));
                    }
                };

                asyncTask.execute();
                if (!isPooling()) {
                    setPooling(true);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                    lookUpNearbyBusStops();
                    recyclerView.scrollToPosition(0);
                    handler.postDelayed(this::lookUpNearbyBusStops, 600);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    @SuppressLint("MissingPermission")
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
//                if(sgWeather!=null)
//                    sgWeather.updateLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

                if (!firstLocationUpdate) {
                    if (mCurrentLocation != null) {
                        firstLocationUpdate = true;
                        loadFavoritesFromDB();
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.setMaxZoomPreference(MAX_ZOOM);
                        mMap.setMinZoomPreference(MIN_ZOOM);
                        sgWeather.updateLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                        loadingScreen.setVisibility(View.GONE);
                        bottomNav.setSelectedItemId(R.id.action_fav);

                        Geocoder gc = new Geocoder(getApplicationContext());
                        try {
                            Address address = gc.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1).get(0);
                            convertedAddress = address.getAddressLine(0);
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Geocoder gc = new Geocoder(getApplicationContext());
                    try {
                        Address address = gc.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1).get(0);
                        convertedAddress = address.getAddressLine(0);
                    }
                    catch(IOException e) {
                        e.printStackTrace();
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
            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();
        if (mLocationPermissionGranted) {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");
                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                })
                .addOnFailureListener(this, e -> {
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

    /**
     * voice search
     */
    public void promptSpeechInput(int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");

        try {
            startActivityForResult(intent, requestCode);
        }
        catch(ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Sorry! Your device doesn't support speech language!", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i) {

        switch(request_code) {

            case 100:
                if(result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    startingPointTextView.setText(result.get(0));
                }
                break;

            case 200:

                if(result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    destinationTextView.setText(result.get(0));
                }
                break;
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
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

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

        switch(id) {

            case R.id.nav_language_preferences:

                final String[] listItems = {"English", "中文", "Bahasa Melayu", "தமிழ்"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Choose your preferred language");
                mBuilder.setSingleChoiceItems(listItems, getSelectedItem(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        saveSelectedItem(option);
                        switch (option) {
                            case 0:
                                //English
                                setLocale("en");
                                recreate();
                                break;
                            case 1:
                                //chinese
                                setLocale("zh");
                                recreate();
                                break;
                            case 2:
                                //malay
                                setLocale("ms");
                                recreate();
                                break;
                            case 3:
                                //tamil
                                setLocale("ta");
                                recreate();
                                break;
                            default:
                                break;
                        }

                        // dismiss alert dialog when language selected
                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                // show alert dialog
                mDialog.show();
                break;

            case R.id.nav_about:

                Log.d(TAG, "onNavigationItemSelected: Settings");
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // method to localize the language
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save data to Shared Preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    //shared preferences method for language alert dialog options
    private int getSelectedItem() {
        if (sharedPreference == null) {
            sharedPreference = PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this);
        }
        return sharedPreference.getInt(SELECTED_ITEM, -1);
    }

    //shared preferences method for language alert dialog options
    private void saveSelectedItem(int item) {
        if (sharedPreference == null) {
            sharedPreference = PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this);
        }
        sharedPrefEditor = sharedPreference.edit();
        sharedPrefEditor.putInt(SELECTED_ITEM, item);
        sharedPrefEditor.commit();
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

        sgWeather = new SGWeather();

        // Loads Bus Routes from json file, data is accessable from busRoute.getBusRouteMap()
        Gson gson = new Gson();
        busRoute =  gson.fromJson( Utils.loadBUSRouteJSONFromAsset(getApplicationContext()), JSONLTABusRoute.class );
        busRoute.createMap();
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

    /**
     * Sets up favoritecards from list
     * <p>
     * This method always finishes almost immediately
     *
     * @param favBusStopID list of busstop IDs
     */
    public void setFavBusStopID(ArrayList<String> favBusStopID) {
        this.favCardList.clear();
        this.favBusStopID = favBusStopID;
        userData.setFavBusStopID(favBusStopID);
        db.collection("user").document(UUIDStr).set(userData);
    }

    /**
     * Loads up favoritecards from Firebase
     * <p>
     * This method always finishes almost immediately
     * Fills up favBusStopID and syncs it to adapter
     */
    private void loadFavoritesFromDB(){
        Log.d(TAG, "loadFavoritesFromDB: UUIDStr: "+UUIDStr);
        DocumentReference docRef = db.collection("user").document(UUIDStr);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    userData = document.toObject(UserData.class);
                    if (userData != null) {
                        favBusStopID = userData.getFavBusStopID();
                    }
                    adapter.setFavBusStopID(favBusStopID);

//                    bottomNav.setSelectedItemId(R.id.action_fav);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    /**
     * Loads up LTA data from the Datamall
     * <p>
     * This method always finishes almost immediately
     * It will fill up sortedLTABusStopData and allBusStops
     */
    private void PrepareLTAData(){
        Log.d(TAG, "PrepareLTAData: Start");

        if(!haveNetworkConnection(this)) {
            showNoNetworkDialog(this);
        }

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
            FillBusData();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void LinkIDtoName(){
        @SuppressLint("StaticFieldLeak")
        @SuppressWarnings("unchecked")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                for (Map.Entry<String, LTABusStopData> newData : allBusStops.entrySet()) {
                    String key = newData.getKey();
                    LTABusStopData value = newData.getValue();
                    allBusByID.put(value.getBusStopCode(),value.getDescription());
//                    sortedLTABusStopData.add(value);
//                    Log.d(TAG, "doInBackground: LinkIDtoName");
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
                LinkIDtoName();
                dialog.dismiss();
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
                    newStop.setBusStopDesc(value.getRoadName());
                    busStopMap.put(newStop.getBusStopID(), newStop);

                    MapMarkers infoWindowItem = new MapMarkers(Double.parseDouble(value.getBusStopLat()),
                            Double.parseDouble(value.getBusStopLong()), value.getDescription(), id);
//                    if (!mClusterManager.getClusterMarkerCollection().getMarkers().contains(infoWindowItem)) {
                    mClusterManager.addItem(infoWindowItem);
                    markerMap.put(value.getDescription(), infoWindowItem);
                    mClusterManager.setOnClusterItemClickListener(mapMarkers -> {
                        if (allBusStops.containsKey(mapMarkers.getSnippet())) {
//                            Log.d(TAG, "FillBusData: Get Bus stop Data for "+mapMarkers.getTitle()+" "+mapMarkers.getSnippet());
                            BusStopCards card = getBusStopData(mapMarkers.getSnippet());
                            if(card != null) {
                                card.setType(Card.BUS_STOP_CARD);
                                singleCardList.clear();
                                singleCardList.add(card);
                                updateAdapterList(singleCardList);
                            }
//                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            Log.e(TAG, "FillBusData: ERROR Missing data from LTA? : " + mapMarkers.getTitle());
                        }
                        return false;
                    });
//                    }
                }

                return null;
            }
        };
        asyncTask.execute();

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
            Toast.makeText(getApplicationContext(),
                    "Failed to sync data from network!",
                    Toast.LENGTH_SHORT).show();
            return null;
        }

//        prepareBottomSheet();
        if(!haveNetworkConnection(this)) {
            Toast.makeText(getApplicationContext(),
                    "Failed to sync data from network!",
                    Toast.LENGTH_SHORT).show();
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
//        Log.d(TAG, "Look up bus timings for : " + result.getBusStopID());
        JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, result.getBusStopID());
        Map<String, Map> entry;
        try {
            entry = ltaReply.execute().get();
            for (Map.Entry<String, Map> entryData : entry.entrySet()) {
                String key = entryData.getKey(); // Bus stop ID
                Map value = entryData.getValue(); // Map with Bus to Timings
                BusStopCards card = busStopMap.get(key);
                card.setType(Card.BUS_STOP_CARD);
                card.setMajorUpdate(true);
                card.setBusStopDesc(result.getBusStopDesc());
                Map<String, List<String>> finalData = new HashMap<>(value);
                for (List<String> newData : finalData.values()) {
                    String toConvertID = newData.get(0);
//                    Log.d(TAG, "getBusStopData: toConvertID " + toConvertID);
                    if(allBusStops.get(toConvertID).getRoadName() != null) {
                        newData.set(3, allBusStops.get(toConvertID).getDescription());
//                        Log.d(TAG, "getBusStopData1: "+allBusStops.get(toConvertID).getRoadName());
                    }
//                    Log.d(TAG, "getBusStopData1: "+allBusStops.get(toConvertID).getDescription());
                }
                result.setBusServices(finalData);
                result.setBusStopDesc(result.getBusStopDesc());
                result.setLastUpdated(Calendar.getInstance().getTime().toString());

//                Log.d(TAG, "getBusStopData: Bus stop ID:" + key
//                        + " Bus Stop Name: " + card.getBusStopName()
//                        + " Bus Stop Desc: " + card.getBusStopDesc()
//                        + " - " + card.getBusServices()
//                        + " - Last Updated: " + Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
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

        if(!haveNetworkConnection(getApplicationContext())){
            Toast.makeText(getApplicationContext(),
                    "No Network detected! Check your network!",
                    Toast.LENGTH_SHORT).show();
//            showNoNetworkDialog(mContext);
            return;
        }

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
                return sortLocations(sortedLTABusStopData, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                List<LTABusStopData> toProcess = (List<LTABusStopData>) o;
                if(toProcess.size() <= 0){
                    Log.d(TAG, "lookUpNearbyBusStops: SORTING returned no data");
                    Toast.makeText(getApplicationContext(),
                            "No response from servers! Check your network!",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                nearbyCardList.clear();
                for(int i=0; i< 11; i++) {
                    BusStopCards card = getBusStopData(toProcess.get(i).getBusStopCode());
                    if(card != null) {
                        card.setType(Card.BUS_STOP_CARD);
                        card.setMajorUpdate(true);
                        nearbyCardList.add(card);
                        Log.d(TAG, "lookUpNearbyBusStops: " + card.toString());
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapterList(nearbyCardList);
                    }
                },400);
//                updateAdapterList(nearbyCardList);
            }
        };
        asyncTask.execute();
    }

    /**
     * Sorts and return a List of LTABusStopData by location
     * <p>
     * This method always returns immediately
     *
     * @param  locations List of LTABusStopData
     * @param  myLatitude Current latitude
     * @param  myLongitude Current longitude
     * @return List - LTABusStopData list
     */
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
//        Log.d(TAG, "sortLocations: BEGIN SORTING!");
        Collections.sort(locations, comp);
        long elapsedTime = System.currentTimeMillis() - start;
        Log.d(TAG, "sortLocations: COMPLETED SORTING! "+elapsedTime+"ms");
        return locations;
    }

    /**
     * Sorts and return a List of Card by location
     * <p>
     * This method always returns immediately
     *
     * @param  locations List of Card
     * @param  myLatitude Current latitude
     * @param  myLongitude Current longitude
     * @return List - Card list
     */
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
//        Log.d(TAG, "sortLocations: BEGIN SORTING!");
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
        handler.postDelayed(() -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED), 100);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * Prepares Favorite cards for display
     * <p>
     * This method always completes
     *
     * @param  list List of Favorite Card
     */
    private void prepareFavoriteCards(ArrayList<String> list){
        if(list.size() < 1){
            Log.e(TAG, "prepareFavoriteCards: list is empty!");
            return;
        }

        favCardList.clear();

        for(int i=0; i< list.size(); i++) {
            BusStopCards card = getBusStopData(list.get(i));
            if (card != null) {
                card.setType(Card.BUS_STOP_CARD);
                card.setMajorUpdate(true);
                favCardList.add(card);
                Log.d(TAG, "prepareFavoriteCards: adding " + card.getBusStopID() + " to favCardList");
            }
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

    private void lookUpRoutes(String query, String fareTypes, String spinnerSelectedItem){
        List<String> directionsQuery = new ArrayList<>();
        directionsQuery.add(query);
        Log.i(TAG,directionsQuery.toString());
        JSONGoogleDirectionsParser directionsParser = new JSONGoogleDirectionsParser(MainActivity.this,directionsQuery);
        List<GoogleRoutesData> result; //= new ArrayList<>(); //result from parser
        try {
            result = directionsParser.execute().get();
            Log.d(TAG,query);
            if(result.size() <= 0){
                Log.d(TAG, "lookUpRoute: Google returned no data");
                return;
            }
            transitCardList.clear();
            walkingCardList.clear();
            progressBar.setVisibility(View.VISIBLE);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!optionMode){
                        //walking
                        for(int i=0; i< result.size(); i++) {
                            if (getWeatherData(result.get(i))) {
                                String msg = "Remember to bring an umbrella with you!";
                                NavigateWalkingCard card = NavigateWalkingCard.getRouteDataWalking(result.get(i), msg);
                                card.setType(Card.NAVIGATE_WALKING_CARD);
                                walkingCardList.add(card);
                                Log.d(TAG, "lookUpRoute: " + card.toString());
                            }
                            else{
                                String msg = "Weather looks good!";
                                NavigateWalkingCard card = NavigateWalkingCard.getRouteDataWalking(result.get(i), msg);
                                card.setType(Card.NAVIGATE_WALKING_CARD);
                                walkingCardList.add(card);
                                Log.d(TAG, "lookUpRoute: " + card.toString());
                            }
                        }
                        updateAdapterList(walkingCardList);

                    }else{/*
                        //NORMAL ROUTES*/
                        for(int i=0; i< result.size(); i++) {
                            if(getDistanceMatrix(result.get(i))) {
                                NavigateTransitCard card1 = NavigateTransitCard.getRouteData(result.get(i), fareTypes, "* Suggested Route *");
                                card1.setType(card1.NAVIGATE_TRANSIT_CARD);
                                transitCardList.add(card1);
                                Log.d(TAG, "lookUpRoute: " + card1.toString());
                            }
                            else{
                                NavigateTransitCard card1 = NavigateTransitCard.getRouteData(result.get(i), fareTypes, "");
                                card1.setType(card1.NAVIGATE_TRANSIT_CARD);
                                transitCardList.add(card1);
                                Log.d(TAG, "lookUpRoute: " + card1.toString());
                            }
                        }
                        ArrayList<? extends Card> navigateCardList = new ArrayList<NavigateTransitCard>();
                        navigateCardList = (ArrayList<? extends Card>) transitCardList;
                        List<NavigateTransitCard> castToNavigate = (List<NavigateTransitCard>) navigateCardList;

                        switch (spinnerSelectedItem) {
                            case "Least time":
                                Collections.sort(castToNavigate, NavigateTransitCard.timeComparator);
                                updateAdapterList((ArrayList<? extends Card>) castToNavigate);
                                break;
                            case "Least distance":
                                Collections.sort(castToNavigate, NavigateTransitCard.distanceComparator);
                                //transitCardList = (ArrayList<? super Card>) castToNavigate;
                                updateAdapterList((ArrayList<? extends Card>) castToNavigate);
                                break;
                            case "Least walking":
                                Collections.sort(castToNavigate, NavigateTransitCard.walkingDistanceComparator);
                                updateAdapterList((ArrayList<? extends Card>) castToNavigate);
                                break;
                        }

                        //updateAdapterList((ArrayList<? extends Card>) transitCardList);
                    }
                }
            }, 1500);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public boolean getWeatherData(GoogleRoutesData googleRoutesData){
        List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
        boolean umbrella = false;
        if (routeSteps != null) {
            for (int i = 0; i < routeSteps.size(); i++) {
                double startLat = routeSteps.get(i).getStartLocationLat();
                double startLng = routeSteps.get(i).getStartLocationLng();
                if (sgWeather!=null) {
                    Log.d("WALKing -------------- ", "START " + startLat + ", " + startLng);
                    sgWeather.updateForSpecificLocation(new LatLng(startLat, startLng));
                    String temp = sgWeather.getmTempForLatLong();
                    String weather = sgWeather.getmWeatherForLatLong();
                    Log.d("WALKing -------------- ", "TEMPERATURE " + temp);
                    Log.d("WALKing -------------- ", "WEATHER " + weather);
                    if (weather != null) {
                        if (weather.contains("Sunny") || weather.contains("Rain") || weather.contains("Thunderstorms")) {
                            umbrella = true;
                        } else {
                            umbrella = false;
                        }
                    } else {
                        umbrella = false;
                    }
                }
            }
        }
        else{
            Log.d(TAG, "routeSteps EMPTY" );
        }
        return umbrella;
    }
    private boolean lookUpTrafficDuration(String type, String train, String queryMatrix, String queryDir){
        boolean pass = false;
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
            if(result.size() <= 0){
                Log.d(TAG, "lookUpTrafficDuration: Google returned no data");
                return pass;
            }
            for(int i=0; i< result.size(); i++) {
                if (type=="bus") {
                    Log.d(TAG, "lookUpTrafficDuration BUS: " + i );
                    if (getMatrix(result1.get(0))) { //no congestion, to display on suggested
                        pass = true;
                    } else { // dont display
                        pass = false;
                    }
                }
                else if (type == "mrt"){
                    List<String> twitterServiceList = new ArrayList<String>();

                    switch(train) {
                        case("East West Line"):
                            mrtLine = "EWL";
                            break;
                        case("North South Line"):
                            mrtLine = "NSL";
                            break;
                        case("North East Line"):
                            mrtLine = "NEL";
                            break;
                        case("Downtown Line"):
                            mrtLine = "DTL";
                            break;
                        case ("Circle Line"):
                            mrtLine = "CCL";
                            break;
                        default:
                            break;
                    }

                    Log.d("LookUpTrafficDuration", train);
                    Log.d("LookUpTrafficDuration", mrtLine);
                    String keyTwitter = "[" + mrtLine + "]";
                    Log.d("MRTLINE", keyTwitter);
                    for(String s : twitterList){
                        if(s.contains(keyTwitter)){
                            twitterServiceList.add(s);
                            Log.d("CONTAINED", s);
                        }
                    }
                    if (twitterServiceList.size() > 0) {
                        Log.d("TWITTERSERVICELIST", twitterServiceList.get(0));
                        if (twitterServiceList.get(0).contains("commenced") || twitterServiceList.get(0).contains("CLEARED") || twitterServiceList.get(0).contains("restored") || twitterServiceList.get(0).contains("resumed")) {
                            Log.d("NO FAULT", "NO FAULT");
                            pass = true;
                        } else if (twitterServiceList.get(0).contains("Due to") || twitterServiceList.get(0).contains("pls add") || twitterServiceList.get(0).contains("train travel time") || twitterServiceList.get(0).contains("no train service")) {
                            Log.d("GOT FAULT", "GOT FAULT");
                            pass = false;
                        } else {
                            pass = false;
                        }
                    }
                    else{
                        pass = true;
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return pass;
    }
    private boolean getMatrix(DistanceData distanceData){
        int duration = Integer.parseInt(distanceData.getDuration().replaceAll("[^0-9]", ""));
        int duration_in_traffic = Integer.parseInt(distanceData.getDuration_in_traffic().replaceAll("[^0-9]", ""));
        Log.d("GetMatrix()", "duration "+ duration + " , duration traffic " + duration_in_traffic );
        //if (duration - duration_in_traffic > 0){ //no congestion.
        if (duration - duration_in_traffic >= 4){
            Log.d("GetMatrix()", "BOOLEAN NO CONGESTION");
            return true;
        }
        else {
            return false;
        }
    }
    private boolean getDistanceMatrix(GoogleRoutesData googleRoutesData) {
        List<GoogleRoutesSteps> routeSteps = googleRoutesData.getSteps();
        boolean pass = false;
        if (routeSteps != null) {
            Log.d(TAG, "routeSteps duration: "+ routeSteps.get(0).getDuration());
            for (int i = 0; i < routeSteps.size(); i++) {
                Log.d("getDistanceMatrix",String.valueOf(i));
                if (routeSteps.get(i).getTravelMode().equals("TRANSIT") && routeSteps.get(i).getTrainLine()!= null ) {
                    Log.d(TAG, "IS A TRAIN" + i);
                    String trainline = routeSteps.get(i).getTrainLine();
                    Log.d(TAG, trainline);
                    pass =  lookUpTrafficDuration("mrt", trainline, "", query);
                }
                else if (routeSteps.get(i).getTravelMode().equals("TRANSIT") && routeSteps.get(i).getBusNum()!= null ) {
                    Log.d(TAG, "IS A BUS" + i);
                    Log.d("BUS TRANSIT", routeSteps.get(i).toString());
                    Double startLat = routeSteps.get(i).getStartLocationLat();
                    Double startLng = routeSteps.get(i).getStartLocationLng();
                    Double endLat = routeSteps.get(i).getEndLocationLat();
                    Double endLng = routeSteps.get(i).getEndLocationLng();
                    Log.d(TAG, startLat.toString() + " " + startLng.toString() + " " + endLat.toString() + " " + endLng.toString());
                    String queryMatrix = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + startLat + "," + startLng + "&destinations=" + endLat + "," + endLng + "&departure_time=now&key=AIzaSyATjwuhqNJTXfoG1TvlnJUmb3rlgu32v5s";
                    Log.d("DISTANCEMATRIX", "query");
                    pass =  lookUpTrafficDuration("bus", "", queryMatrix, query);
                }
            }
        }
        else{
            Log.d(TAG, "routeSteps EMPTY" );
            pass = false;
        }
        return pass;
    }
          
    public void hideKeyboard(){
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    int mToolbarHeightBackUp = 0;
    ValueAnimator mVaActionBar2 = null;
    void hideActionBar(Toolbar bar) {
        // holds the original Toolbar height.
        // this can also be obtained via (an)other method(s)
        int mToolbarHeight = 0;
        int mAnimDuration = 150/* milliseconds */;
//        ValueAnimator mVaActionBar = null;
        // initialize `mToolbarHeight`
//        mToolbarHeight = getSupportActionBar().getHeight();
//        Log.d(TAG, "hideActionBar: "+mToolbarHeight);

        if(bar.equals(toolbar)) {
//            Log.d(TAG, "hideActionBar: SHOWN");
            TypedValue tv = new TypedValue();
//            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                mToolbarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
        }
        else {
//            Log.d(TAG, "hideActionBar: SHOWN2");
            mToolbarHeight = toolbarNavigate.getMinimumHeight();
        }

        if (mVaActionBar2 != null && mVaActionBar2.isRunning()) {
            // we are already animating a transition - block here
//            Log.d(TAG, "hideActionBar: we are already animating a transition");
            return;
        }

        // animate `Toolbar's` height to zero.
//        Log.d(TAG, "hideActionBar1: "+mToolbarHeight);
        mVaActionBar2 = ValueAnimator.ofInt(mToolbarHeight , 0);
        mVaActionBar2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // update LayoutParams

                ((CoordinatorLayout.LayoutParams)bar.getLayoutParams()).height
                        = (Integer)animation.getAnimatedValue();
//                Log.d(TAG, "hideActionBar2: "+bar.getLayoutParams().height);
                bar.requestLayout();
            }
        });

        mVaActionBar2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (getSupportActionBar() != null) { // sanity check
                    getSupportActionBar().hide();
                }
            }
        });

        mVaActionBar2.setDuration(mAnimDuration);
        mVaActionBar2.start();
    }

    ValueAnimator mVaActionBar1 = null;
    void showActionBar(Toolbar bar) {
        int mToolbarHeight = 0;
        int mAnimDuration = 150/* milliseconds */;
//        ValueAnimator mVaActionBar = null;
        if (mVaActionBar1 != null && mVaActionBar1.isRunning() && bar != null) {
            // we are already animating a transition - block here
            return;
        }

        if(bar.equals(toolbar)) {
            TypedValue tv = new TypedValue();
//            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                mToolbarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
        }
        else {
            mToolbarHeight = toolbarNavigate.getMinimumHeight();
            mToolbarHeightBackUp = mToolbarHeight;
        }

        // restore `Toolbar's` height
        mVaActionBar1 = ValueAnimator.ofInt(0 , mToolbarHeight);
        mVaActionBar1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // update LayoutParams
                ((CoordinatorLayout.LayoutParams)bar.getLayoutParams()).height
                        = (Integer)animation.getAnimatedValue();
                bar.requestLayout();
            }
        });

        mVaActionBar1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setSupportActionBar(bar);
                if (getSupportActionBar() != null) { // sanity check
                    getSupportActionBar().show();
                }
            }
        });

        mVaActionBar1.setDuration(mAnimDuration);
        mVaActionBar1.start();
    }


    // download twitter timeline after first checking to see if there is a network connection
    public void downloadTweets() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new JSONTwitterParser().execute(ScreenName);
        } else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    public class JSONTwitterParser extends AsyncTask<String, Void , String>{
        final static String CONSUMER_KEY = "nW88XLuFSI9DEfHOX2tpleHbR";
        final static String CONSUMER_SECRET = "hCg3QClZ1iLR13D3IeMvebESKmakIelp4vwFUICuj6HAfNNCer";
        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0]);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e("result",result);

            try {
                JSONArray jsonArray_data = new JSONArray(result);
                for (int i=0; i<jsonArray_data.length();i++){

                    JSONObject jsonObject = jsonArray_data.getJSONObject(i);
                    twitterList.add(jsonObject.getString("text"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            }  catch (IOException ex2) {
            }
            return sb.toString();
        }

        private String getTwitterStream(String screenName) {
            String results = null;

            //Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
                String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

                // Concatenate the encoded consumer key, a colon character, and the
                // encoded consumer secret
                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                //Obtain a bearer token
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                String rawAuthorization = getResponseBody(httpPost);
                Authenticated auth = jsonToAuthenticated(rawAuthorization);

                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth.token_type.equals("bearer")) {

                    //Authenticate API requests with bearer token
                    HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                    httpGet.setHeader("Content-Type", "application/json");
                    // update the results with the body of the response
                    results = getResponseBody(httpGet);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }
    }

}
