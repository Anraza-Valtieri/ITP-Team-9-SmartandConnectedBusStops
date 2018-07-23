package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
import com.sit.itp_team_9_smartandconnectedbusstops.Animators.MapAnimator;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTAResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.OnBusCardClick;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.OnFavoriteClick;
import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.Card;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateTransitCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateWalkingCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils.haveNetworkConnection;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements JSONLTAResponse {
    //    private PackageManager mPackageManager;
    private static final String TAG = CardAdapter.class.getSimpleName();
    private static final int BUS_STOP_CARD = 0;
    private static final int NAVIGATE_TRANSIT_CARD = 1;
    private static final int NAVIGATE_WALKING_CARD = 2;
    private Context mContext;
    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheet;
    private RecyclerView recyclerView;
    private View view;
    private ArrayList<Card> mCard;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();
    private ArrayList<String> favBusStopID = new ArrayList<>();
    private ArrayList<String> favRoute = new ArrayList<>();

    private List<LatLng> listLatLng = new ArrayList<>();

    private OnFavoriteClick mOnFavoriteClickListener;
    private OnBusCardClick mOnBusCardClickListener;

    public void setOnFavoriteClickListener(OnFavoriteClick l) {
        mOnFavoriteClickListener = l;
    }

    public void setOnBusCardClickListener(OnBusCardClick l) {
        mOnBusCardClickListener = l;
    }

    public List<LatLng> getListLatLng() {
        return listLatLng;
    }

    public void setListLatLng(List<LatLng> listLatLng) {
        this.listLatLng = listLatLng;
    }

    public void clearRoute(){
        if(mMap != null) {
            listLatLng.clear();
            MapAnimator.getInstance().animateRoute(mMap, listLatLng);
        }
    }

    public ArrayList<String> getFavBusStopID() {
        return favBusStopID;
    }

    public void setFavBusStopID(ArrayList<String> newFavBusStopID) {
        if(favBusStopID != null)
            favBusStopID.clear();
        if(newFavBusStopID != null)
            favBusStopID = newFavBusStopID;
        else
            favBusStopID = new ArrayList<>();
    }

    public ArrayList<String> getFavRoute() {
        return favRoute;
    }

    public void setFavRoute(ArrayList<String> newFavRoute) {
        if(favRoute!= null)
            favRoute.clear();
        if(newFavRoute != null)
            favRoute = newFavRoute;
        else
            favRoute = new ArrayList<>();
    }

    public ArrayList<Card> getmCard() {
        return mCard;
    }

    public void setmCard(ArrayList<Card> mCard) {
        this.mCard = mCard;
    }

    private List<ApplicationInfo> mApplications;

    public CardAdapter(Context context, ArrayList<Card> card, GoogleMap mMap,
                       BottomSheetBehavior bottomSheet, RecyclerView rv) {
//        this.mApplications = mApplications;
        mContext = context;
        mCard = card;
        this.mMap = mMap;
        this.bottomSheet = bottomSheet;
        this.recyclerView = rv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view;
        ViewHolder viewHolder = null;
        switch(viewType){
            default:
            case BUS_STOP_CARD:
                view = LayoutInflater.from(mContext).inflate(R.layout.busstopcard,parent,false);
                viewHolder = new ViewHolder(view, BUS_STOP_CARD, mContext);
                break;
            case NAVIGATE_TRANSIT_CARD:
                view = LayoutInflater.from(mContext).inflate(R.layout.navigate_transit_card,parent,false);
                viewHolder = new ViewHolder(view, NAVIGATE_TRANSIT_CARD, mContext);
                break;
            case NAVIGATE_WALKING_CARD:
                view = LayoutInflater.from(mContext).inflate(R.layout.navigate_walking_card,parent,false);
                viewHolder = new ViewHolder(view, NAVIGATE_WALKING_CARD, mContext);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            // Perform a full update
            onBindViewHolder(viewHolder, position);
        } else {
            // Perform a partial update
//            for (Object payload : payloads) {
//                String text = payload.toString().replace("[", "").replace("]", "");
//                Log.d(TAG, "onBindViewHolder: payload "+text);
//                viewHolder.busLastUpdated.setText(Utils.dateCheck2(Utils.formatCardTime(text)));
//            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        switch(getItemViewType(position)){
            case BUS_STOP_CARD:
                BusStopCards card = (BusStopCards) mCard.get(position);
                card.setType(card.BUS_STOP_CARD);
                holder.setItem(card);

                final View cardview = holder.itemView.findViewById(R.id.buscard);
                ImageButton favorite = cardview.findViewById(R.id.favoritebtn);

                favorite.setOnClickListener(v -> {
                    if (card.isFavorite()) {
                        card.setFavorite(false);
                        favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        favBusStopID.remove(card.getBusStopID());
                    } else {
                        card.setFavorite(true);
                        favorite.setImageResource(R.drawable.ic_favorite_red);
                        favBusStopID.add(card.getBusStopID());
                    }

                    if (mOnFavoriteClickListener != null) {
                        mOnFavoriteClickListener.onFavoriteBusClick(favBusStopID);
                    }
                });
                cardview.setOnClickListener(v -> {
                    int DEFAULT_ZOOM = 18;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Double.parseDouble(card.getBusStopLat()) - 0.0002,
                                    Double.parseDouble(card.getBusStopLong())))
                            .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),500, null);
                    bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    recyclerView.scrollToPosition(position);

                    if(mOnBusCardClickListener != null){
                        mOnBusCardClickListener.onBusCardClick(card.getBusStopID());
                    }
                });
                break;
            case NAVIGATE_TRANSIT_CARD:
                NavigateTransitCard transitCard = (NavigateTransitCard) mCard.get(position);
                transitCard.setType(NavigateTransitCard.NAVIGATE_TRANSIT_CARD);
                if(mCard.get(position).isNeedsUpdate())
                    holder.setItem(transitCard);

                final View cardTransit = holder.itemView.findViewById(R.id.transitcard);
                ImageButton favTransit = cardTransit.findViewById(R.id.favoritebtnTransit);

                favTransit.setOnClickListener(v -> {
                    if (transitCard.getRouteID()!=null) {
                        if (transitCard.isFavorite()) {
                            transitCard.setFavorite(false);
                            favTransit.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Log.d(TAG, "onBindViewHolder: favTransit:" + transitCard.getRouteID());
                            favRoute.remove(transitCard.getRouteID());
                        } else {
                            transitCard.setFavorite(true);
                            favTransit.setImageResource(R.drawable.ic_favorite_red);
                            Log.d(TAG, "onBindViewHolder: favTransit:" + transitCard.getRouteID());
                            favRoute.add(transitCard.getRouteID());
                        }
                    }

                    if (mOnFavoriteClickListener != null) {
                        mOnFavoriteClickListener.onFavoriteRouteClick(favRoute);
                    }
                });

                cardTransit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<LatLng> points = (transitCard.getPolyLines()); // list of latlng

                        PolylineOptions options = new PolylineOptions()
                                .jointType(JointType.ROUND)
                                .startCap(new SquareCap())
                                .endCap(new SquareCap())
                                .geodesic(true);
                        if(points != null) {
                            listLatLng.clear();
                            clearPolylines();
                            for (int z = 0; z < points.size(); z++) {
                                LatLng point = points.get(z);
                                options.add(point);
                                listLatLng.add(point);

                            }
                            bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(listLatLng.get(listLatLng.size()-1));
                            builder.include(listLatLng.get(0));
                            LatLngBounds bounds = builder.build();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                            startAnim();
                        }else{
                            Toast.makeText(mContext, "Strangely there is no Route lines from Google.", Toast.LENGTH_LONG);
                        }
                    }
                });
                break;

            case NAVIGATE_WALKING_CARD:
                NavigateWalkingCard walkingCard = (NavigateWalkingCard) mCard.get(position);
                walkingCard.setType(NavigateWalkingCard.NAVIGATE_WALKING_CARD);
                holder.setItem(walkingCard);
        }

    }


    private void startAnim(){
        if(mMap != null && !listLatLng.isEmpty()) {
            MapAnimator.getInstance().animateRoute(mMap, listLatLng);
        } else {
            Toast.makeText(mContext, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    public void resetAnimation(){
        startAnim();
    }
    @Override
    public int getItemCount() {
        return mCard == null ? 0 : mCard.size();
//        return mApplications == null ? 0 : mApplications.size();
    }

    @Override
    public int getItemViewType(int position) {
        Card card = mCard.get(position);
        if (card instanceof BusStopCards){
            return BUS_STOP_CARD;
        }
        else if(card instanceof NavigateTransitCard){
            return NAVIGATE_TRANSIT_CARD;
        }
        else{
            return NAVIGATE_WALKING_CARD;
        }
    }

    public void addAllCard(ArrayList<? extends Card> card){
        mCard.addAll(card);
        Refresh();
//        Log.d(TAG, "addAllCard: called adds "+mCard.size());
    }

    public void addCard(Card card){
        mCard.add(card);
        notifyItemInserted(mCard.size());
        Refresh();
//        Log.d(TAG, "addCard: called adds "+mCard.size());
    }

    public void Clear(){
//        Log.d(TAG, "Clear: called "+mCard.size());
        mCard.clear();
        Refresh();
    }

    public void Refresh(){
//        Log.d(TAG, "Refresh: called");
        notifyDataSetChanged();
//        doAutoRefresh();
//        updateUI();
    }

    public void clearPolylines(){
//        resetAnimation();
        clearRoute();

    }


    private void updateCardData(List<BusStopCards> cards){
        if(!haveNetworkConnection(mContext)){
            Toast.makeText(mContext,
                    "No Network detected!",
                    Toast.LENGTH_SHORT).show();
//            showNoNetworkDialog(mContext);
            return;
        }
        Log.d(TAG, "updateCardData: Start");
        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                for (BusStopCards card : cards) {
                    List<String> urlsList = new ArrayList<>();
                    urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
//                    Log.d(TAG, "Look up bus timings for : " + card.getBusStopID());
                    JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, card.getBusStopID());
                    ltaReply.delegate2 = CardAdapter.this;
                    ltaReply.execute();
                }
                return null;
            }
        };
        asyncTask.execute();
    }

    public void pauseHandlers(){
        handler2.removeCallbacksAndMessages(null);
    }

    public void resumeHandlers(){
//        handler2.removeCallbacksAndMessages(null);
//        handler2.post(runnable2);
        handler2.postDelayed(runnable2, 2000);
    }

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            if (mCard != null && mCard.size() > 0) {
                List<BusStopCards> busStopCards = new ArrayList<>();
                for (int i = 0; i < mCard.size(); i++) {
//                        Log.d(TAG, "run: Adding Buscard!");
                    if (mCard.get(i).getType() == Card.BUS_STOP_CARD) {
                        ((BusStopCards) mCard.get(i)).setMajorUpdate(true);
                        mCard.get(i).setNeedsUpdate(false);
                        busStopCards.add((BusStopCards) mCard.get(i));
                    }
                    else {
                        if(mCard.get(i).isNeedsUpdate())
                            mCard.get(i).setNeedsUpdate(true);
                        else
                            mCard.get(i).setNeedsUpdate(false);
                    }
                }
                updateCardData(busStopCards);
                notifyItemRangeChanged(0, mCard.size());
                //updateCardData(mCard);
                doAutoRefresh();
            }
        }
    };

    public void doAutoRefresh() {
        handler2.removeCallbacksAndMessages(null);
        handler2.postDelayed(runnable2, 15000);
    }

    @Override
    public void processFinishFromLTA(Map<String, Map> result) {
//        Log.d(TAG, "processFinishFromLTA: Received");
        if(result.size() < 1 || mCard.size() < 1){
            Log.e(TAG, "processFinishFromLTA: LTA returned no data");
        }else {
            @SuppressLint("StaticFieldLeak")
            @SuppressWarnings("unchecked")
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    for (int i = 0; i < mCard.size(); i++) {
                        Card cards = mCard.get(i);
//                        Log.d(TAG, "processFinishFromLTA: "+cards.toString()+ " type: "+cards.getType());
                        if(cards.getType() == BUS_STOP_CARD) {
                            BusStopCards updateCard = (BusStopCards) cards;
//                        Log.d(TAG, "processFinishFromLTA: "+updateCard.getBusStopID()+ " size: "+result.size());
                            if (result.get(updateCard.getBusStopID()) != null) {
                                Map<String, List<String>> loadedData = result.get(updateCard.getBusStopID());
                                Map<String, List<String>> finalData = new HashMap<>(loadedData);
//                Map<String, List<String>> finalData = result.get(updateCard.getBusStopID());
                                for (Map.Entry<String, List<String>> newData : finalData.entrySet()) {
                                    String key2 = newData.getKey();
                                    List<String> schedule = newData.getValue();

                                    Map<String, List<String>> toUpdateService = updateCard.getBusServices();
                                    List<String> toUpdateFields = toUpdateService.get(key2);
                                    if (!schedule.get(0).equals("") && toUpdateFields != null)
                                        toUpdateFields.set(0, schedule.get(0));
                                    if (!schedule.get(1).equals("") && toUpdateFields != null)
                                        toUpdateFields.set(1, schedule.get(1));
                                    if (!schedule.get(2).equals("") && toUpdateFields != null)
                                        toUpdateFields.set(2, schedule.get(2));
                                    updateCard.setLastUpdated(Calendar.getInstance().getTime().toString());
                                }
                            }
                        }
                    }
                    return null;
                }
            }.execute();
//            Log.d(TAG, "processFinishFromLTA: END looking at "+mCard.size());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //For bus card
//        ImageView appIcon;
//        TextView appName;
        Context mContext;
        TextView busStopID;
        TextView busStopName;
        TextView busStopDesc;
        TextView busStopLat;
        TextView busStopLong;
        TextView busNo;
        TextView busDirection;
        TextView busDuration1;
        TextView busDuration2;
        TextView busLastUpdated;
        TextView operator;
        ImageButton favorite;
        ImageView updating;

        //For navigate transit card
        TextView totalTime;
        TextView totalDistance, condition;
        TextView cost;
        View breakdownBar;
        TextView startingStation;
        ImageView imageViewStartingStation;
        TextView transitStation;
        TextView timeTaken;
        TextView numStops;
        ExpandableListView listViewNumStops;
        ImageButton favTransit;
        ImageView umbrella;

        //For navigate walking card
        TextView walkingTime;
        TextView walkingDistance, remark;
        TextView startingRoad;
        ExpandableListView listViewDetailedSteps;

        int cardType = 0;
        //BusStopCards card;

        // Create map to store
        Map<String, List<String>> busServices = new HashMap<>();
        // create list one and store values
        List<String> busTiming = new ArrayList<>();

        /*ViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.busstopcard, parent, false));
        }*/


        public ViewHolder(View itemView, int type, Context context) {
            super(itemView);
            mContext = context;
            busStopID = itemView.findViewById(R.id.sub_text);
            busStopName = itemView.findViewById(R.id.primary_text);
            busNo = itemView.findViewById(R.id.busnumber);
            busDirection = itemView.findViewById(R.id.direction);
            busDuration1 = itemView.findViewById(R.id.duration1);
            busDuration2 = itemView.findViewById(R.id.duration2);
            busLastUpdated = itemView.findViewById(R.id.updatedTiming);
            favorite = itemView.findViewById(R.id.favoritebtn);
            operator = itemView.findViewById(R.id.operator);
            updating = itemView.findViewById(R.id.updatingIcon);

            //For navigate transit card
            totalTime = itemView.findViewById(R.id.textViewTotalTime);
            totalDistance = itemView.findViewById(R.id.textViewTotalDistance);
            cost = itemView.findViewById(R.id.textViewCost);
            breakdownBar = itemView.findViewById(R.id.breakdownBar);
            listViewNumStops = itemView.findViewById(R.id.listViewNumStops);
            condition = itemView.findViewById(R.id.textViewCondition);
            favTransit = itemView.findViewById(R.id.favoritebtnTransit);
            umbrella = itemView.findViewById(R.id.imageViewUmbrella);
            //startingStation = itemView.findViewById(R.id.textViewStartingStation);
            //imageViewStartingStation = itemView.findViewById(R.id.imageViewStartingStation);
            //transitStation = itemView.findViewById(R.id.textViewTransitStation);
            //timeTaken = itemView.findViewById(R.id.textViewTimeTaken);
            //numStops = itemView.findViewById(R.id.textViewNumStops);

            //For navigate walking card
            walkingTime = itemView.findViewById(R.id.textViewWalkingTime);
            walkingDistance = itemView.findViewById(R.id.textViewWalkingDistance);
            listViewDetailedSteps = itemView.findViewById(R.id.listViewDetailedSteps);
            remark = itemView.findViewById(R.id.textViewRemark);

            cardType = type;
        }


        private void setItem(Card card){
            switch (card.getType()) {
                case BUS_STOP_CARD:
                    BusStopCards cards = (BusStopCards)card;
                    cards.setType(Card.BUS_STOP_CARD);
                    this.busStopName.setText(cards.getBusStopName());
                    this.busStopID.setText(cards.getBusStopID());
                    this.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(cards.getLastUpdated())));
                    if(cards.isFavorite())
                        this.favorite.setImageResource(R.drawable.ic_favorite_red);
                    else
                        this.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    if(!Utils.haveNetworkConnection(mContext)){
                        ColorStateList csl = AppCompatResources.getColorStateList(mContext,R.color.error_red);
                        ImageViewCompat.setImageTintList(updating, csl);
                    }else{
                        ColorStateList csl = AppCompatResources.getColorStateList(mContext,R.color.good_green);
                        ImageViewCompat.setImageTintList(updating, csl);
                    }

                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                    fadeIn.setDuration(500);
                    fadeOut.setDuration(500);
                    fadeOut.setStartOffset(500+fadeIn.getStartOffset()+500);
                    fadeIn.setRepeatCount(3);
                    fadeOut.setRepeatCount(2);
                    updating.startAnimation(fadeIn);
                    updating.startAnimation(fadeOut);

//                    Log.d(TAG, "setItem: "+cards.isMajorUpdate());
                    // This part creates layout for bus services
                    final View cardview = itemView.findViewById(R.id.buscard);
                    ImageButton favorite = cardview.findViewById(R.id.favoritebtn);

                    Map<String, List<String>> timings = cards.getBusServices();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (timings.size() > 0) {
                        LinearLayout options_layout = itemView.findViewById(R.id.busdetailLayout);
                        options_layout.setOrientation(LinearLayout.VERTICAL);
                        options_layout.removeAllViewsInLayout();
                        for (String busNo : cards.getSortedKeys()) {
//            for (Map.Entry<String, List<String>> entry : timings.entrySet()) {
                            List<String> value = timings.get(busNo);
                            assert inflater != null;
                            View to_add = inflater.inflate(R.layout.busstopcarddetails, (ViewGroup) itemView.getRootView(), false);
                            TextView busID = to_add.findViewById(R.id.busnumber);
                            TextView operator = to_add.findViewById(R.id.operator);
                            TextView direction = to_add.findViewById(R.id.direction);
                            TextView duration = to_add.findViewById(R.id.duration1);
                            TextView duration2 = to_add.findViewById(R.id.duration2);
                            TextView duration3 = to_add.findViewById(R.id.duration3);
                            ConstraintLayout card1 = to_add.findViewById(R.id.buscard1);
                            ConstraintLayout card2 = to_add.findViewById(R.id.buscard2);
                            ConstraintLayout card3 = to_add.findViewById(R.id.buscard3);

                            ImageView wheel1 = to_add.findViewById(R.id.wheel1);
                            ImageView wheel2 = to_add.findViewById(R.id.wheel2);
                            ImageView wheel3 = to_add.findViewById(R.id.wheel3);

                            busID.setText(busNo);
                            switch (value.get(13)) {
                                case "SBST":
                                    operator.setText("SBS");
//                                busID.setTextColor(Color.parseColor("#790e8b"));
                                    operator.setTextColor(Color.parseColor("#790e8b"));
                                    break;
                                case "SMRT":
                                    operator.setText("SMRT");
//                                busID.setTextColor(Color.parseColor("#b61827"));
                                    operator.setTextColor(Color.parseColor("#b61827"));
                                    break;
                                case "TTS":
                                    operator.setText("TTS");
//                                busID.setTextColor(Color.parseColor("#338a3e"));
                                    operator.setTextColor(Color.parseColor("#338a3e"));
                                    break;
                                case "GAS":
                                    operator.setText("GAS");
//                                busID.setTextColor(Color.parseColor("#c9bc1f"));
                                    operator.setTextColor(Color.parseColor("#c9bc1f"));
                                    break;
                            }

                            if (!value.get(1).equals("")) {
                                duration.setText(Utils.dateCheck(Utils.formatTime(value.get(1))));
                                if (value.get(4).equals(""))
                                    wheel1.setVisibility(INVISIBLE);

                                switch (value.get(2)) {
                                    case "SDA":
                                        duration.setTextColor(Color.parseColor("#c9bc1f"));
                                        break;
                                    case "LSD":
                                        duration.setTextColor(Color.parseColor("#c63f17"));
                                        break;
                                    default:
                                        duration.setTextColor(Color.parseColor("#087f23"));
                                        break;
                                }
                            } else
                                card1.setVisibility(INVISIBLE);


                            if (!value.get(5).equals("")) {
                                duration2.setText(Utils.dateCheck(Utils.formatTime(value.get(5))));
                                if (value.get(7).equals(""))
                                    wheel2.setVisibility(INVISIBLE);

                                switch (value.get(6)) {
                                    case "SDA":
                                        duration2.setTextColor(Color.parseColor("#c9bc1f"));
                                        break;
                                    case "LSD":
                                        duration2.setTextColor(Color.parseColor("#c63f17"));
                                        break;
                                    default:
                                        duration2.setTextColor(Color.parseColor("#087f23"));
                                        break;
                                }
                            } else
                                card2.setVisibility(INVISIBLE);
                                direction.setText(value.get(0));
                                options_layout.addView(to_add);
                        }
                    } else {
                        LinearLayout options_layout = itemView.findViewById(R.id.busdetailLayout);
                        options_layout.removeAllViewsInLayout();
                        assert inflater != null;
                        View to_add = inflater.inflate(R.layout.busstopcarddetailsnobus, (ViewGroup) itemView.getRootView(), false);
                        options_layout.addView(to_add);
                    }
                    if (cards.isFavorite())
                        favorite.setImageResource(R.drawable.ic_favorite_red);

                    cards.setMajorUpdate(false);
                    //        doDataRefresh(holder, position);
                    break;
                case NAVIGATE_TRANSIT_CARD:
                    NavigateTransitCard cardsTransit = (NavigateTransitCard)card;
                    cardsTransit.setType(Card.NAVIGATE_TRANSIT_CARD);
                    if (cardsTransit.getError() == null || cardsTransit.getError().isEmpty()){
                        /*if(cardsTransit.getTotalTime().contains("hour")){
                            String totalTimeText = String.format(Locale.getDefault(), "%d "+
                                    MainActivity.context.getResources().getString(R.string.hour) + "%d"
                                    +MainActivity.context.getResources().getString(R.string.minutes),cardsTransit.getTotalTime().,);
                        }*/

                        this.totalTime.setText(cardsTransit.getTotalTime());
                        this.totalDistance.setText(cardsTransit.getTotalDistance());
                        this.cost.setText(cardsTransit.getCost());
                        this.condition.setText(cardsTransit.getCondition());

                        //traffic condition
                        if(cardsTransit.getCondition()=="Slight delay"){
                           this.condition.setTextColor(Color.RED);
                        }
                        else{
                            this.condition.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
                        }

                        //for umbrella icon
                        if(cardsTransit.isUmbrella()) {
                            Log.d("UMRELLA ", "- ------------" + cardsTransit.isUmbrella());
                            this.umbrella.setImageResource(R.drawable.ic_umbrellasvg);
                        }
                        else {
                            this.umbrella.setVisibility(View.GONE);
                        }

                        //for favorite btn
                        if(cardsTransit.isFavorite())
                            this.favTransit.setImageResource(R.drawable.ic_favorite_red);
                        else
                            this.favTransit.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                        this.favTransit.setVisibility(VISIBLE);

                        //Creates layout for transit stations
                        final View transitCardView = itemView.findViewById(R.id.transitcard);
                        LinearLayout transit_layout = transitCardView.findViewById(R.id.linearLayoutTransitStops);
                        transit_layout.setOrientation(LinearLayout.VERTICAL);

                        transit_layout.removeAllViewsInLayout();

                        transit_layout.setVisibility(VISIBLE);
                        for (Map.Entry<String, List<Object>> entry : cardsTransit.getTransitStations().entrySet()) {
                            String key = entry.getKey();
                            //List<Integer> stationImageStationColor = entry.getValue();
                            Integer stationImage = (Integer) entry.getValue().get(0);
                            Integer stationColor = (Integer) entry.getValue().get(1);
                            String lineName = (String) entry.getValue().get(2);
                            String numInBetweenStops = String.valueOf(entry.getValue().get(3));
                            String arrivalStop = (String) entry.getValue().get(4);
                            String timeTakenForEachWaypoint = (String)entry.getValue().get(5);
                            List<String> inBetweenStopNames = (List<String>) entry.getValue().get(6);

                            LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater2 != null;
                            if (stationImage.equals(R.drawable.ic_baseline_directions_walk_24px)){
                                //walking layout
                                View to_add_navigate = inflater2.inflate(R.layout.navigate_transit_card_transit_walking,
                                        (ViewGroup) itemView.getRootView(), false);
                                //TextView textViewWalking = to_add_navigate.findViewById(R.id.textViewWalking);
                                ExpandableListView listViewDetailedWalking = to_add_navigate.findViewById(R.id.listViewDetailedWalking);

                                String walkingInstructions = mContext.getResources().getString(R.string.walk) +" "+ key +
                                        " ( " + timeTakenForEachWaypoint + ")";
                                //For detailed steps (expandable list adapter and listeners)
                                List<String> walkingHeader = new ArrayList<>();
                                walkingHeader.add(walkingInstructions);
                                HashMap<String, List<String>> detailedWalkingSteps = new HashMap<>();
                                detailedWalkingSteps.put(walkingInstructions,inBetweenStopNames);
                                ExpandableListAdapter walkingAdapter = new ExpandableListAdapter(mContext,
                                        walkingHeader,detailedWalkingSteps);
                                listViewDetailedWalking.setAdapter(walkingAdapter);

                                listViewDetailedWalking.setOnGroupExpandListener(groupPosition -> {
                                    Log.i(TAG,"on group expand");
                                    Utils.setListViewHeightBasedOnChildren(listViewDetailedWalking);
                                    listViewDetailedWalking.smoothScrollToPosition(groupPosition);

                                });

                                listViewDetailedWalking.setOnGroupCollapseListener(groupPosition -> {
                                    Log.i(TAG,"on group collapse");
                                    Utils.setListViewToOriginal(listViewDetailedWalking);
                                });


                                transit_layout.addView(to_add_navigate);

                            }else {
                                //transit layout
                                View to_add_navigate = inflater2.inflate(R.layout.navigate_transit_card_transit_stops,
                                        (ViewGroup) itemView.getRootView(), false);
                                TextView textViewDepartureStop = to_add_navigate.findViewById(R.id.textViewDepartureStop);
                                TextView textViewArrivalStop = to_add_navigate.findViewById(R.id.textViewArrivalStop);
                                ImageView imageViewTransitStation = to_add_navigate.findViewById(R.id.imageViewTransitStation);
                                ExpandableListView listViewNumStops = to_add_navigate.findViewById(R.id.listViewNumStops);
                                TextView transitLine = to_add_navigate.findViewById(R.id.textViewLine);
                                View transitLineBackground = to_add_navigate.findViewById(R.id.viewLine);

                                textViewDepartureStop.setText(key);
                                textViewArrivalStop.setText(arrivalStop);
                                transitLine.setText(lineName);
                                transitLineBackground.setBackgroundColor(stationColor);
                                imageViewTransitStation.setColorFilter(stationColor, PorterDuff.Mode.SRC_IN);
                                imageViewTransitStation.setImageResource(stationImage);

                                //For in between stops (expandable list adapter and listeners)
                                List<String> inBetweenStopsHeader = new ArrayList<>();
                                String inBetweenStopsHeaderString = timeTakenForEachWaypoint
                                        + " (" + numInBetweenStops + " "
                                        + mContext.getString(R.string.stops) +  ")";
                                Log.i(TAG, inBetweenStopsHeaderString);
                                inBetweenStopsHeader.add(inBetweenStopsHeaderString);
                                HashMap<String, List<String>> inBetweenStops = new HashMap<>();
                                inBetweenStops.put(inBetweenStopsHeaderString,inBetweenStopNames);

                                ExpandableListAdapter transitListAdapter = new ExpandableListAdapter(mContext,
                                        inBetweenStopsHeader,inBetweenStops);
                                listViewNumStops.setAdapter(transitListAdapter);

                                listViewNumStops.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                    @Override
                                    public void onGroupExpand(int groupPosition) {
                                        Log.i(TAG,"on group expand");
                                        Utils.setListViewHeightBasedOnChildren(listViewNumStops);
                                        listViewNumStops.smoothScrollToPosition(groupPosition);

                                    }
                                });

                                listViewNumStops.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                                    @Override
                                    public void onGroupCollapse(int groupPosition) {
                                        Log.i(TAG,"on group collapse");
                                        Utils.setListViewToOriginal(listViewNumStops);
                                    }
                                });
                                Log.i(TAG, "transitStationString=" + key);
                                transit_layout.addView(to_add_navigate);
                            }
                        }

                        //Creates layout for breakdown bar (summary bar below the total distance)
                        LinearLayout breakdown_bar_layout = transitCardView.findViewById(R.id.linearLayoutBreakdownBar);
                        breakdown_bar_layout.setOrientation(LinearLayout.HORIZONTAL);

                        breakdown_bar_layout.removeAllViewsInLayout();
                        breakdown_bar_layout.setVisibility(VISIBLE);
                        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                LinkedList<View> listToAdd = new LinkedList<>();
                                for (int i=0; i < cardsTransit.getTimeTaken().size();i++) {
                                    String breakdownBarPartActualTime = (String) cardsTransit.getTimeTaken().get(i).get(0);
                                    float breakdownBarPartWeight = (Float) cardsTransit.getTimeTaken().get(i).get(1);
                                    int breakdownBarPartColor = (Integer) cardsTransit.getTimeTaken().get(i).get(2);

                                    LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    assert inflater2 != null;
                                    View to_add_breakdown = inflater2.inflate(R.layout.navigate_transit_card_breakdown_bar, (ViewGroup) itemView.getRootView(), false);
                                    View breakdownBarPart = to_add_breakdown.findViewById(R.id.breakdownBar);
                                    TextView breakdownBarPartTime = to_add_breakdown.findViewById(R.id.textViewTime);

                                    Log.i(TAG, "breakdownBarPartWeight: " + breakdownBarPartWeight);
                                    breakdownBarPart.setBackgroundColor(breakdownBarPartColor);
                                    breakdownBarPartTime.setText(breakdownBarPartActualTime);
                                    to_add_breakdown.setLayoutParams(new LinearLayout.LayoutParams(110, LinearLayout.LayoutParams.MATCH_PARENT, breakdownBarPartWeight));
//                                    breakdown_bar_layout.addView(to_add_breakdown);
//                                    return to_add_breakdown;
                                    listToAdd.add(to_add_breakdown);
                                }
                                return listToAdd;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);
                                LinkedList<View> linkedList = (LinkedList<View>) o;
                                for(View view: linkedList) {
                                    breakdown_bar_layout.addView(view);
                                }
                            }
                        };
                        asyncTask.execute();
                        /*for (int i=0; i < cardsTransit.getTimeTaken().size();i++) {
                            String breakdownBarPartActualTime = (String) cardsTransit.getTimeTaken().get(i).get(0);
                            float breakdownBarPartWeight = (Float) cardsTransit.getTimeTaken().get(i).get(1);
                            int breakdownBarPartColor = (Integer) cardsTransit.getTimeTaken().get(i).get(2);

                            LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater2 != null;
                            View to_add_breakdown = inflater2.inflate(R.layout.navigate_transit_card_breakdown_bar, (ViewGroup) itemView.getRootView(), false);
                            View breakdownBarPart = to_add_breakdown.findViewById(R.id.breakdownBar);
                            TextView breakdownBarPartTime = to_add_breakdown.findViewById(R.id.textViewTime);

                            Log.i(TAG, "breakdownBarPartWeight: " + breakdownBarPartWeight);
                            breakdownBarPart.setBackgroundColor(breakdownBarPartColor);
                            breakdownBarPartTime.setText(breakdownBarPartActualTime);
                            to_add_breakdown.setLayoutParams(new LinearLayout.LayoutParams(110, LinearLayout.LayoutParams.MATCH_PARENT, breakdownBarPartWeight));
                            breakdown_bar_layout.addView(to_add_breakdown);
                        }*/
                    }else{
                        //No routes available
                        this.totalTime.setText(R.string.transit_error);
                        this.totalDistance.setText("");
                        this.cost.setText("");
                        this.condition.setText("");

                        this.favTransit.setVisibility(View.GONE);

                        //Creates layout for transit stations
                        final View transitCardView = itemView.findViewById(R.id.transitcard);
                        LinearLayout transit_layout = transitCardView.findViewById(R.id.linearLayoutTransitStops);
                        transit_layout.setOrientation(LinearLayout.VERTICAL);
                        transit_layout.removeAllViewsInLayout();
                        transit_layout.setVisibility(View.GONE);

                        LinearLayout breakdown_bar_layout = transitCardView.findViewById(R.id.linearLayoutBreakdownBar);
                        breakdown_bar_layout.setOrientation(LinearLayout.HORIZONTAL);

                        breakdown_bar_layout.removeAllViewsInLayout();
                        breakdown_bar_layout.setVisibility(View.GONE);
                    }
                    if (cardsTransit.isFavorite())
                        favTransit.setImageResource(R.drawable.ic_favorite_red);
                    break;
                case NAVIGATE_WALKING_CARD:
                    NavigateWalkingCard cardsWalking = (NavigateWalkingCard)card;
                    cardsWalking.setType(Card.NAVIGATE_WALKING_CARD);
                    Log.i(TAG,"navigate walking card");
                    this.walkingTime.setText(cardsWalking.getTotalTime());
                    String walkingDistance = "( " + cardsWalking.getTotalDistance() +")";
                    this.walkingDistance.setText(walkingDistance);
                    this.remark.setText(cardsWalking.getRemark());

                    //For detailed steps (expandable list adapter and listeners)
                    ExpandableListAdapter walkingListAdapter = new ExpandableListAdapter(mContext,
                            cardsWalking.getDescription(),cardsWalking.getDetailedSteps());
                    this.listViewDetailedSteps.setAdapter(walkingListAdapter);

                    this.listViewDetailedSteps.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            Log.i(TAG,"on group expand");
                            Utils.setListViewHeightBasedOnChildren(listViewDetailedSteps);
                            listViewDetailedSteps.smoothScrollToPosition(groupPosition);

                            /*if (listViewDetailedSteps.isGroupExpanded(groupPosition)) {
                                listViewDetailedSteps.collapseGroup(groupPosition);
                            } else {
                                listViewDetailedSteps.expandGroup(groupPosition);
                            }*/

                        }
                    });

                    this.listViewDetailedSteps.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                        @Override
                        public void onGroupCollapse(int groupPosition) {
                            Log.i(TAG,"on group collapse");
                            Utils.setListViewToOriginal(listViewDetailedSteps);
                        }
                    });

                    //TODO walking route on map
                    break;
            }
        }
    }
}
