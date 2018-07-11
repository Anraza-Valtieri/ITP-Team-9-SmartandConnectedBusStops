package com.sit.itp_team_9_smartandconnectedbusstops.Model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class CustomListView extends ExpandableListView {

    public CustomListView  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView  (Context context) {
        super(context);
    }

    public CustomListView  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}