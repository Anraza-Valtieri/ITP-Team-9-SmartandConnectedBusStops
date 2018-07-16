package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.AppGuideParentDataItem;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

import java.util.ArrayList;

public class AppGuideAdapter extends RecyclerView.Adapter<AppGuideAdapter.AppGuideViewHolder>{

    private ArrayList<AppGuideParentDataItem> dummyParentDataItems;

    public AppGuideAdapter(ArrayList<AppGuideParentDataItem> dummyParentDataItems) {
        this.dummyParentDataItems = dummyParentDataItems;
    }

    @NonNull
    @Override
    public AppGuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_app_guide, parent, false);
        return new AppGuideViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppGuideViewHolder holder, int position) {
        AppGuideParentDataItem dummyParentDataItem = dummyParentDataItems.get(position);
        holder.textView_parentName.setText(dummyParentDataItem.getParentName());

        int noOfChildTextViews = holder.linearLayout_childItems.getChildCount();
        int noOfChild = dummyParentDataItem.getChildDataItems().size();
        Log.d("Hello", String.valueOf(noOfChildTextViews));
        Log.d("Byebye", String.valueOf(noOfChild));
        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {


                TextView currentTextView = (TextView) holder.linearLayout_childItems.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }
        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
            TextView currentTextView = (TextView) holder.linearLayout_childItems.getChildAt(textViewIndex);
            currentTextView.setText(dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AppGuideViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private TextView textView_parentName;
        private LinearLayout linearLayout_childItems;

        public AppGuideViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            textView_parentName = itemView.findViewById(R.id.tv_parentName);
            linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);
            linearLayout_childItems.setVisibility(View.GONE);


            int intMaxNoOfChild = 0;
            for (int index = 0; index < dummyParentDataItems.size(); index++) {
                int intMaxSizeTemp = dummyParentDataItems.get(index).getChildDataItems().size();
                if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                TextView textView = new TextView(context);
                textView.setId(indexView);
                textView.setPadding(16, 16, 16, 16);
                //textView.setGravity(Gravity.LEFT);
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //textView.setOnClickListener(this);

                linearLayout_childItems.addView(textView, layoutParams);

                Log.d("Sub2", linearLayout_childItems.getChildAt(indexView).toString());
            }
            textView_parentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                        linearLayout_childItems.setVisibility(View.GONE);
                    } else {
                        linearLayout_childItems.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
