package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTAResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.Card;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateTransitCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.NavigateWalkingCard;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ArrayList<Card> mCard;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();
    public ArrayList<String> favBusStopID = new ArrayList<>();

    public ArrayList<String> getFavBusStopID() {
        return favBusStopID;
    }

    public void setFavBusStopID(ArrayList<String> favBusStopID) {
        this.favBusStopID.clear();
        this.favBusStopID = favBusStopID;
    }

    public ArrayList<Card> getmCard() {
        return mCard;
    }

    public void setmCard(ArrayList<Card> mCard) {
        this.mCard = mCard;
    }

    private List<ApplicationInfo> mApplications;

    public CardAdapter(Context context, ArrayList<Card> card, GoogleMap mMap, BottomSheetBehavior bottomSheet) {
//        this.mApplications = mApplications;
        mContext = context;
        this.mCard = card;
        this.mMap = mMap;
        this.bottomSheet = bottomSheet;
//        mPackageManager = mContext.getPackageManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO switch case
        View view;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        switch(getItemViewType(position)){
            case BUS_STOP_CARD:
                BusStopCards card = (BusStopCards) mCard.get(position);
                card.setType(card.BUS_STOP_CARD);
                if(card.isMajorUpdate())
                    holder.setItem(card);

                TextView busStopID = holder.itemView.findViewById(R.id.sub_text);
                TextView busStopName = holder.itemView.findViewById(R.id.primary_text);
                TextView busLastUpdated = holder.itemView.findViewById(R.id.updatedTiming);

                busStopName.setText(card.getBusStopName());
                busStopID.setText(card.getBusStopID());
                busLastUpdated.setText(Utils.dateCheck2(Utils.formatCardTime(card.getLastUpdated())));
                final View cardview = holder.itemView.findViewById(R.id.buscard);
                ImageButton favorite = cardview.findViewById(R.id.favoritebtn);

                /*if(card.isMajorUpdate()) {
                    // This part creates layout for bus services


                    Map<String, List<String>> timings = card.getBusServices();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (timings.size() > 0) {
                        LinearLayout options_layout = holder.itemView.findViewById(R.id.busdetailLayout);
                        options_layout.setOrientation(LinearLayout.VERTICAL);
                        options_layout.removeAllViewsInLayout();
                        for (String busNo : card.getSortedKeys()) {
                            List<String> value = timings.get(busNo);
                            assert inflater != null;
                            View to_add = inflater.inflate(R.layout.busstopcarddetails, (ViewGroup) holder.itemView.getRootView(), false);
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
                                    wheel1.setVisibility(View.GONE);

                                switch (value.get(2)) {
                                    case "SDA":
                                        card1.setBackgroundColor(Color.parseColor("#c9bc1f"));
                                        break;
                                    case "LSD":
                                        card1.setBackgroundColor(Color.parseColor("#c63f17"));
                                        break;
                                    default:
                                        break;
                                }
                            } else
                                card1.setVisibility(View.INVISIBLE);


                            if (!value.get(5).equals("")) {
                                duration2.setText(Utils.dateCheck(Utils.formatTime(value.get(5))));
                                if (value.get(7).equals(""))
                                    wheel2.setVisibility(View.GONE);

                                switch (value.get(6)) {
                                    case "SDA":
                                        card1.setBackgroundColor(Color.parseColor("#c9bc1f"));
                                        break;
                                    case "LSD":
                                        card1.setBackgroundColor(Color.parseColor("#c63f17"));
                                        break;
                                    default:
                                        break;
                                }
                            } else
                                card2.setVisibility(View.INVISIBLE);

//                            if (!value.get(9).equals("")) {
//                                duration3.setText(Utils.dateCheck(Utils.formatTime(value.get(9))));
//                                if (value.get(11).equals(""))
//                                    wheel3.setVisibility(View.GONE);
//
//                                switch (value.get(10)) {
//                                    case "SDA":
//                                        card1.setBackgroundColor(Color.parseColor("#c9bc1f"));
//                                        break;
//                                    case "LSD":
//                                        card1.setBackgroundColor(Color.parseColor("#c63f17"));
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            } else
//                                card3.setVisibility(View.INVISIBLE);
                            direction.setText(value.get(0));
                            options_layout.addView(to_add);
                        }
                    } else {
                        LinearLayout options_layout = holder.itemView.findViewById(R.id.busdetailLayout);
                        options_layout.removeAllViewsInLayout();
                        assert inflater != null;
                        View to_add = inflater.inflate(R.layout.busstopcarddetailsnobus, (ViewGroup) holder.itemView.getRootView(), false);
                        options_layout.addView(to_add);
                    }*/
                    if (card.isFavorite())
                        favorite.setImageResource(R.drawable.ic_favorite_red);

                    //        doDataRefresh(holder, position);
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
                    });
                    cardview.setOnClickListener(v -> {
                        int DEFAULT_ZOOM = 18;
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(Double.parseDouble(card.getBusStopLat()) - 0.0002,
                                        Double.parseDouble(card.getBusStopLong())))
                                .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    });

//                    card.setMajorUpdate(false);
//                }
                updateUI(position);
                break;
            case NAVIGATE_TRANSIT_CARD:
                NavigateTransitCard transitCard = (NavigateTransitCard) mCard.get(position);
                transitCard.setType(transitCard.NAVIGATE_TRANSIT_CARD);
                holder.setItem(transitCard);
                final View transitCardView = holder.itemView.findViewById(R.id.transitcard);
                TextView totalTime = transitCardView.findViewById(R.id.textViewTotalTime);
                TextView totalDistance = transitCardView.findViewById(R.id.textViewTotalDistance);
                TextView cost = transitCardView.findViewById(R.id.textViewCost);
                View breakdownBar = transitCardView.findViewById(R.id.breakdownBar);
                TextView startingStation = transitCardView.findViewById(R.id.textViewStartingStation);
                TextView transferStation = transitCardView.findViewById(R.id.textViewTransferStation);
                TextView endingStation = transitCardView.findViewById(R.id.textViewEndingStation);
                TextView timeTaken = transitCardView.findViewById(R.id.textViewTimeTaken);
                TextView numStops = transitCardView.findViewById(R.id.textViewNumStops);
                ImageView imageViewStartingStation = transitCardView.findViewById(R.id.imageViewStartingStation);
                ImageView imageViewEndingStation = transitCardView.findViewById(R.id.imageViewEndingStation);


                totalTime.setText(transitCard.getTotalTime());
                totalDistance.setText(transitCard.getTotalDistance());
                cost.setText(transitCard.getCost());
                //breakdownBar.setProgress(10); //TODO breakdown bar needs to be set
                startingStation.setText(transitCard.getStartingStation());
                transferStation.setText(transitCard.getTransferStation());
                endingStation.setText(transitCard.getEndingStation());
                timeTaken.setText(transitCard.getTimeTaken());
                numStops.setText(transitCard.getNumStops());
                //imageViewStartingStation.setColorFilter((Color.rgb( 255, 255, 255)));
                imageViewStartingStation.setImageResource(transitCard.getImageViewStartingStation());
                imageViewStartingStation.setColorFilter(transitCard.getImageViewStartingStationColor(), PorterDuff.Mode.SRC_IN);
                imageViewEndingStation.setImageResource(transitCard.getImageViewEndingStation());
                imageViewEndingStation.setColorFilter(transitCard.getImageViewEndingStationColor(), PorterDuff.Mode.SRC_IN);

                transitCardView.setOnClickListener(v -> {
                    //TODO open new detailed card
                    Log.d(TAG,"onClick NavigateTransitCard");
                    //bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });
                break;

            case NAVIGATE_WALKING_CARD:
                //TODO complete code here
                NavigateWalkingCard walkingCard = (NavigateWalkingCard) mCard.get(position);
                walkingCard.setType(walkingCard.NAVIGATE_WALKING_CARD);
                holder.setItem(walkingCard);
                final View walkingCardView = holder.itemView.findViewById(R.id.walkingCard);
                TextView walkingTime = walkingCardView.findViewById(R.id.textViewWalkingTime);
                TextView walkingDistance = walkingCardView.findViewById(R.id.textViewWalkingDistance);
                TextView startingRoad = walkingCardView.findViewById(R.id.textViewStartingRoad);

                walkingTime.setText(walkingCard.getTotalTime());
                walkingDistance.setText(walkingCard.getTotalDistance());
                startingRoad.setText(walkingCard.getDescription());

                walkingCardView.setOnClickListener(v -> {
                    //TODO open new detailed card
                    Log.d(TAG,"onClick NavigateTransitCard");
                    bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });
                break;
        }

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
        this.mCard.addAll(card);
        Refresh();
        Log.d(TAG, "addAllCard: called adds "+mCard.size());
    }

    public void addCard(Card card){
        mCard.add(card);
        notifyItemInserted(mCard.size());
        Refresh();
        Log.d(TAG, "addCard: called adds "+mCard.size());
    }

    public void Clear(){
        Log.d(TAG, "Clear: called "+mCard.size());
        this.mCard.clear();
        Refresh();
    }

    public void Refresh(){
        Log.d(TAG, "Refresh: called");
        handler.removeCallbacksAndMessages(null);
        notifyDataSetChanged();
//        doAutoRefresh();
    }


    private void updateCardData(List<BusStopCards> cards){
        if(!haveNetworkConnection(mContext)){
            Toast.makeText(mContext,
                    "No Network detected, failed to refresh data!",
                    Toast.LENGTH_SHORT).show();
//            showNoNetworkDialog(mContext);
            return;
        }
        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                for (BusStopCards card : cards) {
                    List<String> urlsList = new ArrayList<>();
                    urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
                    Log.d(TAG, "Look up bus timings for : " + card.getBusStopID());
                    JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, card.getBusStopID());
                    ltaReply.delegate2 = CardAdapter.this;
                    ltaReply.execute();
                }
                return null;
            }
        };
        asyncTask.execute();
    }

    private void updateUI(int position){
//        Log.d(TAG, "updateUI: "+position);
        handler.postDelayed(() -> notifyItemChanged(position), 1000);
    }

    public void pauseHandlers(){
        handler.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
    }

    public void resumeHandlers(){
        for(int i = 0; i < mCard.size(); i++){
            updateUI(i);
        }
        doAutoRefresh();
    }

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            if (mCard != null) {
                List<BusStopCards> busStopCards = new ArrayList<>();
                for(int i=0; i<mCard.size(); i++ ) {
                    Card card = mCard.get(i);
                    if (card.getType() == card.BUS_STOP_CARD) {
//                        Log.d(TAG, "run: Adding Buscard!");
                        ((BusStopCards)mCard.get(i)).setMajorUpdate(true);
                        busStopCards.add((BusStopCards) mCard.get(i));
                    }
                }
                updateCardData(busStopCards);
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
        if(result.size() < 1){
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        //TODO switch case here
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

        //For navigate transit card
        TextView totalTime;
        TextView totalDistance;
        TextView cost;
        ProgressBar breakdownBar;
        TextView startingStation;
        TextView transferStation;
        TextView endingStation;
        TextView timeTaken;
        TextView numStops;

        //For navigate walking card
        TextView walkingTime;
        TextView walkingDistance;
        TextView startingRoad;

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

            //For navigate transit card
            totalTime = itemView.findViewById(R.id.textViewTotalTime);
            totalDistance = itemView.findViewById(R.id.textViewTotalDistance);
            cost = itemView.findViewById(R.id.textViewCost);
            breakdownBar = itemView.findViewById(R.id.progressBar);
            startingStation = itemView.findViewById(R.id.textViewStartingStation);
            transferStation = itemView.findViewById(R.id.textViewTransferStation);
            endingStation = itemView.findViewById(R.id.textViewEndingStation);
            timeTaken = itemView.findViewById(R.id.textViewTimeTaken);
            numStops = itemView.findViewById(R.id.textViewNumStops);

            //For navigate walking card
            walkingTime = itemView.findViewById(R.id.textViewWalkingTime);
            walkingDistance = itemView.findViewById(R.id.textViewWalkingDistance);
            startingRoad = itemView.findViewById(R.id.textViewStartingRoad);

            cardType = type;
        }


        private void setItem(Card card){
            switch (this.cardType) {
                case BUS_STOP_CARD:
                    BusStopCards cards = (BusStopCards)card;
                    cards.setType(card.BUS_STOP_CARD);
                    this.busStopName.setText(cards.getBusStopName());
                    this.busStopID.setText(cards.getBusStopID());
                    this.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(cards.getLastUpdated())));
                    if(cards.isFavorite())
                        this.favorite.setImageResource(R.drawable.ic_favorite_red);
                    else
                        this.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);

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
                                    wheel1.setVisibility(View.GONE);

                                switch (value.get(2)) {
                                    case "SDA":
                                        card1.setBackgroundColor(Color.parseColor("#c9bc1f"));
                                        break;
                                    case "LSD":
                                        card1.setBackgroundColor(Color.parseColor("#c63f17"));
                                        break;
                                    default:
                                        break;
                                }
                            } else
                                card1.setVisibility(View.INVISIBLE);


                            if (!value.get(5).equals("")) {
                                duration2.setText(Utils.dateCheck(Utils.formatTime(value.get(5))));
                                if (value.get(7).equals(""))
                                    wheel2.setVisibility(View.GONE);

                                switch (value.get(6)) {
                                    case "SDA":
                                        card1.setBackgroundColor(Color.parseColor("#c9bc1f"));
                                        break;
                                    case "LSD":
                                        card1.setBackgroundColor(Color.parseColor("#c63f17"));
                                        break;
                                    default:
                                        break;
                                }
                            } else
                                card2.setVisibility(View.INVISIBLE);

                        /*if (!value.get(9).equals("")) {
                            duration3.setText(Utils.dateCheck(Utils.formatTime(value.get(9))));
                            if (value.get(11).equals(""))
                                wheel3.setVisibility(View.GONE);

                            switch (value.get(10)) {
                                case "SDA":
                                    card1.setBackgroundColor(Color.parseColor("#c9bc1f"));
                                    break;
                                case "LSD":
                                    card1.setBackgroundColor(Color.parseColor("#c63f17"));
                                    break;
                                default:
                                    break;
                            }
                        } else
                            card3.setVisibility(View.INVISIBLE);*/
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
                    NavigateTransitCard cards2 = (NavigateTransitCard)card;
                    cards2.setType(card.NAVIGATE_TRANSIT_CARD);
                    this.totalTime.setText(cards2.getTotalTime());
                    this.totalDistance.setText(cards2.getTotalDistance());
                    this.cost.setText(cards2.getCost());
                    this.startingStation.setText(cards2.getStartingStation());
                    this.transferStation.setText(cards2.getTransferStation());
                    this.endingStation.setText(cards2.getEndingStation());
                    this.numStops.setText(cards2.getNumStops());
                    this.timeTaken.setText(cards2.getTimeTaken());
                    break;
                case NAVIGATE_WALKING_CARD:
                    NavigateWalkingCard cards3 = (NavigateWalkingCard)card;
                    cards3.setType(card.NAVIGATE_WALKING_CARD);
                    this.walkingTime.setText(cards3.getTotalTime());
                    this.walkingDistance.setText(cards3.getTotalDistance());
                    this.startingRoad.setText(cards3.getDescription());
                    break;
            }
        }

        /*private void setItem(NavigateTransitCard card){

        }

        private void setItem(NavigateWalkingCard card) {
            this.walkingTime.setText(card.getTotalTime());
            this.walkingDistance.setText(card.getTotalDistance());
            this.startingRoad.setText(card.getDescription());
        }*/

    }
}
