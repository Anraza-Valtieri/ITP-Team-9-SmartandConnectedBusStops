package com.sit.itp_team_9_smartandconnectedbusstops;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.AppGuideAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.AppGuideChildDataItem;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.AppGuideParentDataItem;

import java.util.ArrayList;

public class AppGuideActivity extends AppCompatActivity {

    private Button mButtonBack;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        mButtonBack = findViewById(R.id.btnBack);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContext = AppGuideActivity.this;
        mRecyclerView = findViewById(R.id.recyclerViewAppGuide);
        RecyclerDataAdapter recyclerDataAdapter = new RecyclerDataAdapter(getDummyDataToPass());
        //AppGuideAdapter appGuideAdapter = new AppGuideAdapter(getDummyDataToPass());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(recyclerDataAdapter);
        mRecyclerView.setHasFixedSize(true);



    }

    private ArrayList<AppGuideParentDataItem> getDummyDataToPass() {

        ArrayList<AppGuideParentDataItem> dummyDataItems = new ArrayList<>();
        ArrayList<AppGuideChildDataItem> dummyChildDataItems;
        AppGuideParentDataItem dummyParentDataItem;
        AppGuideChildDataItem dummyChildDataItem;

        /////////
        dummyParentDataItem = new AppGuideParentDataItem();
        dummyParentDataItem.setParentName("Getting Started : TransitThere");
        dummyChildDataItems = new ArrayList<>();
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("For a start, you will be greeted with the following three icons on the bottom of the start screen namely:");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tFavourites: You will be able to view all your favourited bus stops arranged according to the distance you are currently located and routes that you have previously saved bringing you everyday convenience on your fingertips");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tNearby: You will be able to view all the nearby bus stops around your surroundings. Each bus stop card features all the bus services and their arrival timings updated every minute to bring you timely information on bus arrival");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tDirections: Navigate your way across the island with simple to use search navigation, featuring voice search, suggested routes, fares calculation etc. If you require further assistance on this functionality, click on Help Topic 2: How to navigate for directions?");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("In the navigation bar, you will be greeted with the following topics:");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\t Weather: Upon clicking into the navigation menu, you will be informed of the latest weather with information such as PSI level and UV Index etc");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tLanguage Preferences: You will be able to select your preferred language for use within the app");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tApp Guide: In here, you will come across various help topics specially curated to guide you in using the app");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tAbout: General information about the app");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tData Sources: Highlights the various data sources used to gather and display information within this app");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("•\tAcknowledgements: Featuring open-sources libraries used in this app and accreditation of respective owners’ work");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyParentDataItem.setChildDataItems(dummyChildDataItems);
        dummyDataItems.add(dummyParentDataItem);

        /////////
        dummyParentDataItem = new AppGuideParentDataItem();
        dummyParentDataItem.setParentName("Topic 1: How to favourite a bus stop/route?");
        dummyChildDataItems = new ArrayList<>();
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("1. You can simply favourite a bus stop nearby you, by clicking on the heart icon on the top right corner of the bus stop card.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("2. After searching for a route to your destination, you can simply favourite the route by clicking on the heart icon on the top right corner of the navigation card.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("3. To view your favourites, simply click on the ‘Favourites’ icon to view the items you have previously favourite.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyParentDataItem.setChildDataItems(dummyChildDataItems);
        dummyDataItems.add(dummyParentDataItem);

        /////////
        dummyParentDataItem = new AppGuideParentDataItem();
        dummyParentDataItem.setParentName("Topic 2: How to navigate for directions?");
        dummyChildDataItems = new ArrayList<>();
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("1. Simply head over to the directions tab, you will be greeted with the navigation toolbar.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("2. In the navigation toolbar, you can simply choose to use the voice search or type your start point and destination to search for a route.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("3. To switch the start point and destination, simply click on the swap button on the top right of the navigation toolbar and you are all set.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("4. To complement your search, you will be able to select the type of fares for the routes, sort your routes and choose between navigation for public transport or walking.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("5. You will then be able to view all the available route for your journey. In addition to that, some routes will be featured as ‘Suggested Route’ taking in account the weather condition and various other factors to recommend it to you.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyParentDataItem.setChildDataItems(dummyChildDataItems);
        dummyDataItems.add(dummyParentDataItem);

        /////////
        dummyParentDataItem = new AppGuideParentDataItem();
        dummyParentDataItem.setParentName("Topic 3: How to verbally search for directions?");
        dummyChildDataItems = new ArrayList<>();
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("1. To verbally search for directions, click on the microphone icon on each of the search field either start point or destination.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("2. Once your voice has been captured the app will automatically fill the search fields for you.");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyParentDataItem.setChildDataItems(dummyChildDataItems);
        dummyDataItems.add(dummyParentDataItem);

        /////////
        dummyParentDataItem = new AppGuideParentDataItem();
        dummyParentDataItem.setParentName("Topic 4: How to set your preferred language?");
        dummyChildDataItems = new ArrayList<>();
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("1. Head over to the navigation menu, click on ‘Language Preferences’");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("2. You will be created with a pop-up menu showing the various languages available. Click on your preferred language and you are all set!");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyChildDataItem = new AppGuideChildDataItem();
        dummyChildDataItem.setChildName("Note: The app will experience a tiny blackout to display the contents in your preferred language. ");
        dummyChildDataItems.add(dummyChildDataItem);
        //
        dummyParentDataItem.setChildDataItems(dummyChildDataItems);
        dummyDataItems.add(dummyParentDataItem);

        return dummyDataItems;
    }

    private class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.MyViewHolder> {
        private ArrayList<AppGuideParentDataItem> dummyParentDataItems;

        RecyclerDataAdapter(ArrayList<AppGuideParentDataItem> dummyParentDataItems) {
            this.dummyParentDataItems = dummyParentDataItems;
        }

        @Override
        public RecyclerDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_app_guide, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerDataAdapter.MyViewHolder holder, int position) {
            AppGuideParentDataItem dummyParentDataItem = dummyParentDataItems.get(position);
            holder.textView_parentName.setText(dummyParentDataItem.getParentName());

            int noOfChildTextViews = holder.linearLayout_childItems.getChildCount();
            int noOfChild = dummyParentDataItem.getChildDataItems().size();
            Log.d("Hello", String.valueOf(noOfChildTextViews));
            Log.d("Byebye", String.valueOf(noOfChild));
            if (noOfChild < noOfChildTextViews) {
                for (int index = noOfChild; index < noOfChildTextViews; index++) {



//                    TextView currentTextView = (TextView) holder.linearLayout_childItems.getChildAt(index);
//                    currentTextView.setVisibility(View.GONE);

                    /*if(dummyParentDataItem.getChildDataItems().get(index).getChildName().contains("@drawable")) {
                        ImageView imgView = (ImageView) holder.linearLayout_childItems.getChildAt(index);
                        imgView.setVisibility(View.GONE);
                    }
                    else {*/
                        TextView currentTextView = (TextView) holder.linearLayout_childItems.getChildAt(index);
                        currentTextView.setVisibility(View.GONE);
                    //}


                }
            }
            for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {

                //Log.d("HELLA", holder.linearLayout_childItems.getChildAt(textViewIndex).toString());

                //TextView currentTextView = (TextView) holder.linearLayout_childItems.getChildAt(textViewIndex);
                //currentTextView.setText(dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName());

                /*if(dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName().contains("@drawable")) {
                    Log.d("WASSUP", "ImageView + index" + textViewIndex + " " + dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName());
                    ImageView imgView = (ImageView) holder.linearLayout_childItems.getChildAt(textViewIndex);
                    //Log.d("HELLA", holder.linearLayout_childItems.getChildAt(4).toString());
                    //TextView cvbf = (TextView) holder.linearLayout_childItems.getChildAt(textViewIndex);
                    String filename = dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName();

                    int imageResource = mContext.getResources().getIdentifier(filename, null, mContext.getPackageName());

                    imgView.setImageDrawable(mContext.getResources().getDrawable(imageResource));

                }
                else {
                    Log.d("WASSUP", "TextView + index" + textViewIndex + " " + dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName());*/
                    TextView currentTextView = (TextView) holder.linearLayout_childItems.getChildAt(textViewIndex);
                    currentTextView.setText(dummyParentDataItem.getChildDataItems().get(textViewIndex).getChildName());
                //}

                /*currentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "" + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        }

        @Override
        public int getItemCount() {
            return dummyParentDataItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private Context context;
            private TextView textView_parentName;
            private LinearLayout linearLayout_childItems;

            MyViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                textView_parentName = itemView.findViewById(R.id.tv_parentName);
                linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);
                linearLayout_childItems.setVisibility(View.GONE);

                //linearLayout_childItems.removeAllViews();

                int intMaxNoOfChild = 0;
                for (int index = 0; index < dummyParentDataItems.size(); index++) {
                    int intMaxSizeTemp = dummyParentDataItems.get(index).getChildDataItems().size();
                    if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;

                    for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {

                        /*if (dummyParentDataItems.get(index).getChildDataItems().get(indexView).getChildName().contains("@drawable")) {


                            ImageView imageView = new ImageView(context);
                            //linearLayout_childItems.removeViewAt(indexView);
                            imageView.setId(indexView);
                            imageView.setPadding(0, 20, 0, 20);
                            imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
                            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            //linearLayout_childItems.removeViewAt(indexView);
                            Log.d("Sub", "CreatedImageView + Index " + indexView + " " + dummyParentDataItems.get(index).getChildDataItems().get(indexView).getChildName());
                            //linearLayout_childItems.removeViewAt(indexView);
                            linearLayout_childItems.addView(imageView, layoutParams2);

                            Log.d("Sub2", linearLayout_childItems.getChildAt(indexView).toString());
                        }
                        else {*/
                            TextView textView = new TextView(context);
                            textView.setId(indexView);
                            textView.setPadding(16, 16, 16, 16);
                            //textView.setGravity(Gravity.LEFT);
                            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            //textView.setOnClickListener(this);
                            //Log.d("Sub", "CreatedTextView + Index " + indexView + " " + dummyParentDataItems.get(index).getChildDataItems().get(indexView).getChildName());
                            linearLayout_childItems.addView(textView, layoutParams);

                            //Log.d("Sub2", linearLayout_childItems.getChildAt(indexView).toString());
                        //}
                    }
                }





                    //for(int i= 0; i < dummyParentDataItems.get(indexView).getChildDataItems().size(); i++) {

                        /*if(dummyParentDataItems.get(indexView).getChildDataItems().get(i).getChildName().contains("file:")) {
                            ImageView imageView = new ImageView(context);
                            imageView.setId(indexView);
                            imageView.setPadding(0, 20, 0, 20);
                            imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                            linearLayout_childItems.addView(imageView, layoutParams);
                        }
                        else {*/

                        //}
                    //}



                textView_parentName.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_parentName) {
                    if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                        linearLayout_childItems.setVisibility(View.GONE);
                    } else {
                        linearLayout_childItems.setVisibility(View.VISIBLE);
                    }
                }
//                else {
//                    TextView textViewClicked = (TextView) view;
//                    Toast.makeText(context, "" + textViewClicked.getText().toString(), Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }
}
