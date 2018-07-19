package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

public class SliderAdapter extends PagerAdapter{

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.intro_screen_one,
            R.drawable.intro_screen_two,
            R.drawable.intro_screen_three,
            R.drawable.intro_screen_four,
            R.drawable.intro_screen_five,
            R.drawable.intro_screen_six,
            R.drawable.intro_screen_seven,
            R.drawable.intro_screen_eight,
            R.drawable.intro_screen_nine,
            R.drawable.intro_screen_ten
    };

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView imageView = view.findViewById(R.id.slide_image);

//        GlideApp.with(this.context).load("").centerCrop().placeholder(slide_images[position]).into(imageView);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        
        Glide.with(context).load(slide_images[position]).apply(options).into(imageView);
//        imageView.setImageResource();

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
