package com.sit.itp_team_9_smartandconnectedbusstops.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.sit.itp_team_9_smartandconnectedbusstops.MainActivity;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private TextView mOffsetText;
    private TextView mStateText;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            setStateText(newState);
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            setOffsetText(slideOffset);
        }
    };
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);


//
//        recyclerView.setLayoutManager(mLinearLayoutManager);
//        recyclerView.setAdapter(mAdapter);
    }

    private void setOffsetText(final float slideOffset) {
        ViewCompat.postOnAnimation(mOffsetText, new Runnable() {
            @Override
            public void run() {
                mOffsetText.setText(getString(R.string.offset, slideOffset));
            }
        });
    }

    private void setStateText(int newState) {
        mStateText.setText(MainActivity.getStateAsString(newState));
    }
}
