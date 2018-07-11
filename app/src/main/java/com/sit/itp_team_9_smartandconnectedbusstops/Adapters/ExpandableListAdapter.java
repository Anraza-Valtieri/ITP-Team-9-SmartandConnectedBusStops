package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sit.itp_team_9_smartandconnectedbusstops.R;
import com.sit.itp_team_9_smartandconnectedbusstops.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = ExpandableListAdapter.class.getSimpleName();

    private Context mContext;
    private List<String> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> mListDataChild;
    private ListView mListView;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        //this.mListView = listView;
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert  infalInflater !=null;
            view = infalInflater.inflate(R.layout.navigate_transit_card_expandable_list_title,
                    (ViewGroup) parent.getRootView(), false);
        }
        TextView lblListNumStops = (TextView) view.findViewById(R.id.textViewNumStops);
        //lblListNumStops.setTypeface(null, Typeface.BOLD);
        lblListNumStops.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int groupPosition,final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        Log.i(TAG, "getChildView");
        final String childText = (String) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            view = infalInflater.inflate(R.layout.navigate_transit_card_expandable_list_child,
                    (ViewGroup) parent.getRootView(), false);
        }

        TextView stopName = (TextView) view.findViewById(R.id.textViewStopName);

        stopName.setText(Html.fromHtml(childText));
        stopName.setTextSize(13);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
