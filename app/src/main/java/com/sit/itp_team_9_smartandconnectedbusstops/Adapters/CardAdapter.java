package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.sit.itp_team_9_smartandconnectedbusstops.Interfaces.JSONLTAResponse;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.Parser.JSONLTABusTimingParser;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements JSONLTAResponse{
//    private PackageManager mPackageManager;
    private static final String TAG = CardAdapter.class.getSimpleName();
    private Context mContext;
    private GoogleMap mMap;
    private ArrayList<BusStopCards> mCard;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();

    public ArrayList<BusStopCards> getmCard() {
        return mCard;
    }

    public void setmCard(ArrayList<BusStopCards> mCard) {
        this.mCard = mCard;
    }

    public CardAdapter(Context context, ArrayList<BusStopCards> card, GoogleMap mMap) {
//        this.mApplications = mApplications;
        mContext = context;
        mCard = card;
        this.mMap = mMap;
//        mPackageManager = mContext.getPackageManager();
    }

    private List<ApplicationInfo> mApplications;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BusStopCards card = mCard.get(position);
        holder.setItem(card);

        // This part creates layout for bus services
        final View cardview = holder.itemView.findViewById(R.id.buscard);
        Map<String, List<String>> timings = card.getBusServices();
        if(timings.size() > 0) {
            LinearLayout options_layout = holder.itemView.findViewById(R.id.busdetailLayout);
            options_layout.setOrientation(LinearLayout.VERTICAL);
            options_layout.removeAllViewsInLayout();
            for (Map.Entry<String, List<String>> entry : timings.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                View to_add = inflater.inflate(R.layout.busstopcarddetails, (ViewGroup) holder.itemView.getRootView(), false);

                TextView busID = to_add.findViewById(R.id.busnumber);
                TextView direction = to_add.findViewById(R.id.direction);
                TextView duration = to_add.findViewById(R.id.duration1);

                busID.setText(key);
                direction.setText("MISSING DATA");

                String durationText = "";
                if (value.get(0).equals("") && value.get(1).equals(""))
                    durationText = "No Service Available";

                if (!value.get(0).equals("") && value.get(1).equals(""))
                    durationText = Utils.dateCheck(Utils.formatTime(value.get(0)));

                if (!value.get(0).equals("") && !value.get(1).equals(""))
                    durationText = Utils.dateCheck(Utils.formatTime(value.get(0))) + " & " +
                            Utils.dateCheck(Utils.formatTime(value.get(1)));

            /*if(!value.get(0).equals("") && !value.get(1).equals("") && !value.get(2).equals(""))
                durationText = Utils.dateCheck(Utils.formatTime(value.get(0)))+" "+
                                Utils.dateCheck(Utils.formatTime(value.get(1)))+" "+
                                Utils.dateCheck(Utils.formatTime(value.get(2)));*/

                duration.setText(durationText);
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
//        doDataRefresh(holder, position);
        // TODO Touch interaction of cards.
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int DEFAULT_ZOOM = 18;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.parseDouble(card.getBusStopLat())-0.0002,
                                Double.parseDouble(card.getBusStopLong())), DEFAULT_ZOOM));
            }
        });
    }



    @Override
    public int getItemCount() {
        return mCard == null ? 0 : mCard.size();
//        return mApplications == null ? 0 : mApplications.size();
    }

    public void addAllCard(ArrayList<BusStopCards> card){
        mCard.addAll(card);
        Refresh();
        Log.d(TAG, "addAllCard: called adds "+mCard.size());
    }

    public void addCard(BusStopCards card){
        mCard.add(card);
        notifyItemInserted(mCard.size());
        Refresh();
        Log.d(TAG, "addCard: called adds "+mCard.size());
    }

    public void Clear(){
        Log.d(TAG, "Clear: called "+mCard.size());
        mCard.clear();
        Refresh();
    }

    public void Refresh(){
        Log.d(TAG, "Refresh: called");
        handler.removeCallbacksAndMessages(null);
        notifyDataSetChanged();
//        doAutoRefresh();
    }

    
    public void updateCardData(List<BusStopCards> cards){
        for (BusStopCards card: cards) {
            List<String> urlsList = new ArrayList<>();
            urlsList.add("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=");
            Log.d(TAG, "Look up bus timings for : " + card.getBusStopID());
            JSONLTABusTimingParser ltaReply = new JSONLTABusTimingParser(urlsList, card.getBusStopID());
            ltaReply.delegate2 = CardAdapter.this;
            ltaReply.execute();
        }
    }

    private void updateUI(int position){
        handler.postDelayed(() ->{
            notifyItemChanged(position);
        }, 1000);
    }


    public void doAutoRefresh() {
        handler2.postDelayed(() -> {
            // Write code for your refresh logic
//                notifyItemChanged(position);
            updateCardData(mCard);
            doAutoRefresh();
        }, 10000);
    }

    @Override
    public void processFinishFromLTA(Map<String, Map> result) {
        Log.d(TAG, "processFinishFromLTA: Received");
        if(result.size() < 1){
            Log.e(TAG, "processFinishFromLTA: LTA returned no data");
            return;
        }else {
            @SuppressLint("StaticFieldLeak")
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    for (int i = 0; i < mCard.size(); i++) {
                        BusStopCards updateCard = mCard.get(i);
//                        Log.d(TAG, "processFinishFromLTA: "+updateCard.getBusStopID()+ " size: "+result.size());
                        Map<String, List<String>> finalData = new HashMap<>();
                        if (result.containsKey(updateCard.getBusStopID())) {
                            Map<String, List<String>> loadedData = result.get(updateCard.getBusStopID());
                            finalData.putAll(loadedData);
//                Map<String, List<String>> finalData = result.get(updateCard.getBusStopID());
                            for (Map.Entry<String, List<String>> newData : finalData.entrySet()) {
                                String key2 = newData.getKey();
                                List<String> schedule = newData.getValue();

                                Map<String, List<String>> toUpdateService = updateCard.getBusServices();
                                List<String> toUpdateFields = toUpdateService.get(key2);
                                if (!schedule.get(0).equals(""))
                                    toUpdateFields.set(0, schedule.get(0));
                                if (!schedule.get(1).equals(""))
                                    toUpdateFields.set(1, schedule.get(1));
                                if (!schedule.get(2).equals(""))
                                    toUpdateFields.set(2, schedule.get(2));
                                updateCard.setLastUpdated(Calendar.getInstance().getTime().toString());
                            }
                        }
                    }
                    return null;
                }
            }.execute();


            Log.d(TAG, "processFinishFromLTA: END looking at "+mCard.size());
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        ImageView appIcon;
//        TextView appName;

        TextView busStopID;
        TextView busStopName;
        TextView busStopDesc;
        TextView busStopLat;
        TextView busStopLong;
        TextView busNo;
        TextView busDirection;
        TextView busDuration;
        TextView busLastUpdated;

        BusStopCards card;

        // Create map to store
        Map<String, List<String>> busServices = new HashMap<>();
        // create list one and store values
        List<String> busTiming = new ArrayList<>();

        public ViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.busstopcard, parent, false));
        }

        private ViewHolder(View itemView) {
            super(itemView);
            busStopID = itemView.findViewById(R.id.sub_text);
            busStopName = itemView.findViewById(R.id.primary_text);
            busNo = itemView.findViewById(R.id.busnumber);
            busDirection = itemView.findViewById(R.id.direction);
            busDuration = itemView.findViewById(R.id.duration1);
            busLastUpdated = itemView.findViewById(R.id.updatedTiming);
        }

        private void setItem(BusStopCards card){
            this.busStopName.setText(card.getBusStopName());
            this.busStopID.setText(card.getBusStopID());
            this.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
        }


    }
}
