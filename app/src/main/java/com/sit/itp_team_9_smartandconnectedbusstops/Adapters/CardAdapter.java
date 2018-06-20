package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private final static int BUS_STOP_CARD = 0;
    private final static int NAVIGATE_TRANSIT_CARD = 1;
    private final static int NAVIGATE_WALKING_CARD = 2;
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

    public CardAdapter(Context context, ArrayList<Card> card, GoogleMap mMap, BottomSheetBehavior bottomSheet) {
//        this.mApplications = mApplications;
        mContext = context;
        this.mCard = card;
        this.mMap = mMap;
        this.bottomSheet = bottomSheet;
//        mPackageManager = mContext.getPackageManager();
    }

    private List<ApplicationInfo> mApplications;

    public int getItemViewType(Card card){
        if (card instanceof BusStopCards){
            return BUS_STOP_CARD;
        } else if(card instanceof NavigateTransitCard){
            return NAVIGATE_TRANSIT_CARD;
        }
        else{
            return NAVIGATE_WALKING_CARD;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder;
        View v;
        Context context = parent.getContext();
        switch(viewType){
            default:
            case BUS_STOP_CARD:
                v = LayoutInflater.from(context)
                        .inflate(R.layout.busstopcard, parent, false);
                holder = new ViewHolder(v, BUS_STOP_CARD);
                break;
            case NAVIGATE_TRANSIT_CARD:
                v = LayoutInflater.from(context)
                        .inflate(R.layout.navigate_transit_card, parent, false);
                holder = new ViewHolder(v, NAVIGATE_TRANSIT_CARD);
                break;
            case NAVIGATE_WALKING_CARD:
                v = LayoutInflater.from(context)
                        .inflate(R.layout.navigate_walking_card, parent, false);
                holder = new ViewHolder(v, NAVIGATE_WALKING_CARD);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO switch case here
        /*switch (holder.lay){
            case
        }*/

        Card cards = mCard.get(position);
        switch (cards.getType()) {
            case BUS_STOP_CARD:
                BusStopCards card = (BusStopCards) mCard.get(position);
                holder.setItem(card);
//                holder.setBusItem(card);
                // This part creates layout for bus services
                final View cardview = holder.itemView.findViewById(R.id.buscard);
                ImageButton favorite = cardview.findViewById(R.id.favoritebtn);
                Map<String, List<String>> timings = card.getBusServices();
                if(timings.size() > 0) {
                    LinearLayout options_layout = holder.itemView.findViewById(R.id.busdetailLayout);
                    options_layout.setOrientation(LinearLayout.VERTICAL);
                    options_layout.removeAllViewsInLayout();
                    for(String busNo : card.getSortedKeys()){
//            for (Map.Entry<String, List<String>> entry : timings.entrySet()) {
                        List<String> value = timings.get(busNo);
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        assert inflater != null;
                        View to_add = inflater.inflate(R.layout.busstopcarddetails, (ViewGroup) holder.itemView.getRootView(), false);

                        TextView busID = to_add.findViewById(R.id.busnumber);
                        TextView direction = to_add.findViewById(R.id.direction);
                        TextView duration = to_add.findViewById(R.id.duration1);
                        TextView duration2 = to_add.findViewById(R.id.duration2);

                        busID.setText(busNo);

                        if (!value.get(0).equals(""))
                            duration.setText(Utils.dateCheck(Utils.formatTime(value.get(0))));

                        if (!value.get(1).equals(""))
                            duration2.setText(Utils.dateCheck(Utils.formatTime(value.get(1))));

                        direction.setText(value.get(3));
                        options_layout.addView(to_add);
                    }
                    updateUI(position);
                }else{
                    LinearLayout options_layout = holder.itemView.findViewById(R.id.busdetailLayout);
                    options_layout.removeAllViewsInLayout();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    View to_add = inflater.inflate(R.layout.busstopcarddetailsnobus, (ViewGroup) holder.itemView.getRootView(), false);
                    options_layout.addView(to_add);
                    updateUI(position);
                }
                if(card.isFavorite())
                    favorite.setImageResource(R.drawable.ic_favorite_red);

//        doDataRefresh(holder, position);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(card.isFavorite()){
                            card.setFavorite(false);
                            favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            favBusStopID.remove(card.getBusStopID());
                        }else{
                            card.setFavorite(true);
                            favorite.setImageResource(R.drawable.ic_favorite_red);
                            favBusStopID.add(card.getBusStopID());
                        }
                    }
                });
                cardview.setOnClickListener(v -> {
                    int DEFAULT_ZOOM = 18;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Double.parseDouble(card.getBusStopLat())-0.0002,
                                    Double.parseDouble(card.getBusStopLong())))
                            .zoom(DEFAULT_ZOOM)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });
                break;
            case NAVIGATE_TRANSIT_CARD:
                break;
            case NAVIGATE_WALKING_CARD:
                break;
        }
    }



    @Override
    public int getItemCount() {
        return mCard == null ? 0 : mCard.size();
//        return mApplications == null ? 0 : mApplications.size();
    }

    public void addAllCard(ArrayList<Card> card){
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
            List<BusStopCards> busStopCards = new ArrayList<>();
            Log.d(TAG, "run: "+mCard.size());
            for (int i = 0; i < mCard.size(); i++){
                Card cards = mCard.get(i);
                if(cards.getType() == BUS_STOP_CARD)
                    busStopCards.add((BusStopCards)mCard.get(i));
            }
            updateCardData(busStopCards);
            doAutoRefresh();
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
        //TODO switch case for different card types

//        ImageView appIcon;
//        TextView appName;
        int holderType = 0;
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
        ImageButton favorite;

        BusStopCards card;

        // Create map to store
        Map<String, List<String>> busServices = new HashMap<>();
        // create list one and store values
        List<String> busTiming = new ArrayList<>();

//        ViewHolder(ViewGroup parent, int type) {
//            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.busstopcard, parent, false), type);
//        }


        public ViewHolder(View itemView, int type) {
            super(itemView);
            busStopID = itemView.findViewById(R.id.sub_text);
            busStopName = itemView.findViewById(R.id.primary_text);
            busNo = itemView.findViewById(R.id.busnumber);
            busDirection = itemView.findViewById(R.id.direction);
            busDuration1 = itemView.findViewById(R.id.duration1);
            busDuration2 = itemView.findViewById(R.id.duration2);
            busLastUpdated = itemView.findViewById(R.id.updatedTiming);
            favorite = itemView.findViewById(R.id.favoritebtn);
            holderType = type;
        }

        private void setBusItem(BusStopCards card){

        }

        private void setItem(Card card){
            switch (holderType) {
                case BUS_STOP_CARD:
                    BusStopCards cards = (BusStopCards)card;
                    this.busStopName.setText(cards.getBusStopName());
                    this.busStopID.setText(cards.getBusStopID());
                    this.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(cards.getLastUpdated())));
                    if(cards.isFavorite())
                        this.favorite.setImageResource(R.drawable.ic_favorite_red);
                    else
                        this.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    break;
                case NAVIGATE_TRANSIT_CARD:
                case NAVIGATE_WALKING_CARD:
                    break;
            }
        }

        private void setItem(NavigateTransitCard card){
            /*
            this.busStopName.setText(card.getBusStopName());
            this.busStopID.setText(card.getBusStopID());
            this.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
            if(card.isFavorite())
                this.favorite.setImageResource(R.drawable.ic_favorite_red);
            else
                this.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);*/
        }

        private void setItem(NavigateWalkingCard card){
            /*
            this.busStopName.setText(card.getBusStopName());
            this.busStopID.setText(card.getBusStopID());
            this.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
            if(card.isFavorite())
                this.favorite.setImageResource(R.drawable.ic_favorite_red);
            else
                this.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);*/
        }

    }
}
