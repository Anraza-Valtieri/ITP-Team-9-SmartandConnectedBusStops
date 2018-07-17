package com.sit.itp_team_9_smartandconnectedbusstops;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.SliderAdapter;

public class GetStartedActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mPreviousBtn;

    private int mCurrentPage;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        prefs = getSharedPreferences("com.sit.item_team_9_smartandconnectedbusstops", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            mSlideViewPager = findViewById(R.id.slideViewPager);
            mDotLayout = findViewById(R.id.dotsLayout);

            mNextBtn = findViewById(R.id.nextBtn);
            mPreviousBtn = findViewById(R.id.prevBtn);

            sliderAdapter = new SliderAdapter(this);

            mSlideViewPager.setAdapter(sliderAdapter);

            addDotsIndicator(0);

            mSlideViewPager.addOnPageChangeListener(viewListener);

            //OnClickListeners

            mNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mCurrentPage < 9) {
                        mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                    }
                    else if(mCurrentPage == 9) {
                        prefs.edit().putBoolean("firstrun", false).commit();
                        Intent i = new Intent(GetStartedActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
            });

            mPreviousBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSlideViewPager.setCurrentItem(mCurrentPage - 1);
                }
            });

        }
        else {
            Intent i = new Intent(GetStartedActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void addDotsIndicator(int position) {

        mDots = new TextView[10];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparent_white));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0) {

            mDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;

            // first page
            if(position == 0) {

                mNextBtn.setEnabled(true);
                mPreviousBtn.setEnabled(false);
                mPreviousBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("NEXT");
                mPreviousBtn.setText("");
            }
            // last page
            else if(position == mDots.length-1) {

                mNextBtn.setEnabled(true);
                mPreviousBtn.setEnabled(true);
                mPreviousBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("FINISH");
                mPreviousBtn.setText("BACK");
            }
            // any other page
            else {

                mNextBtn.setEnabled(true);
                mPreviousBtn.setEnabled(true);
                mPreviousBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Next");
                mPreviousBtn.setText("BACK");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
