package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
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
import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;
import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
//    private PackageManager mPackageManager;
    private static final String TAG = CardAdapter.class.getSimpleName();
    private Context mContext;
    private GoogleMap mMap;
    private ArrayList<BusStopCards> mCard;

    public CardAdapter(Context context, ArrayList<BusStopCards> card, GoogleMap mMap) {
//        this.mApplications = mApplications;
        mContext = context;
        this.mCard = card;
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
        holder.busStopName.setText(card.getBusStopName());
        holder.busStopID.setText(card.getBusStopID());
        holder.busLastUpdated.setText(Utils.dateCheck(Utils.formatCardTime(card.getLastUpdated())));
        View cardview = holder.itemView.findViewById(R.id.buscard);
        LinearLayout options_layout = holder.itemView.findViewById(R.id.busdetailLayout);
        options_layout.setOrientation(LinearLayout.VERTICAL);
        Map<String, List<String>> timings = card.getBusServices();
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
            direction.setText("DESTINATION");
            String durationText = "";
            if(value.get(0).equals("") && value.get(1).equals(""))
                durationText = "No Service Available";

            if(!value.get(0).equals("") && value.get(1).equals(""))
                durationText = Utils.dateCheck(Utils.formatTime(value.get(0)));

            if(!value.get(0).equals("") && !value.get(1).equals(""))
                durationText = Utils.dateCheck(Utils.formatTime(value.get(0)))+" & "+
                                Utils.dateCheck(Utils.formatTime(value.get(1)));

            /*if(!value.get(0).equals("") && !value.get(1).equals("") && !value.get(2).equals(""))
                durationText = Utils.dateCheck(Utils.formatTime(value.get(0)))+" "+
                                Utils.dateCheck(Utils.formatTime(value.get(1)))+" "+
                                Utils.dateCheck(Utils.formatTime(value.get(2)));*/

            duration.setText(durationText);
            direction.setText(value.get(3));
            options_layout.addView(to_add);
        }
        doAutoRefresh(holder, position);
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
        Log.d(TAG, "addAllCard: called adds "+mCard.size());
    }

    public void addCard(BusStopCards card){
        mCard.add(card);
        notifyItemInserted(mCard.size());
//        Refresh();
        Log.d(TAG, "addCard: called adds "+mCard.size());
    }

    public void Clear(){
        Log.d(TAG, "Clear: called "+mCard.size());
        mCard.clear();
        handler.removeCallbacksAndMessages(null);
        Refresh();
    }

    public void Refresh(){
        Log.d(TAG, "Refresh: called");
        notifyDataSetChanged();
    }

    private final Handler handler = new Handler();
    private void doAutoRefresh(final ViewHolder holder, final int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic
                notifyItemChanged(position);
                doAutoRefresh(holder,position);
            }
        }, 10000);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

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
    }
}
