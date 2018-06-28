package com.sit.itp_team_9_smartandconnectedbusstops.Utils;

import android.support.v7.util.DiffUtil;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.BusStopCards;

import java.util.List;

public class BusStopCardDiff extends DiffUtil.Callback {

    private final List<BusStopCards> mOldCardList;
    private final List<BusStopCards> mNewCardList;

    public BusStopCardDiff(List<BusStopCards> mOldList, List<BusStopCards> mNewist) {
        this.mOldCardList = mOldList;
        this.mNewCardList = mNewist;
    }

    @Override
    public int getOldListSize() {
        return mOldCardList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCardList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCardList.get(oldItemPosition).getBusStopID() == mNewCardList.get(
                newItemPosition).getBusStopID();
//        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//        return mOldCardList.get(oldItemPosition).getBusServices().e == mNewCardList.get(
//                newItemPosition).getBusStopID();
        return false;
    }
}
