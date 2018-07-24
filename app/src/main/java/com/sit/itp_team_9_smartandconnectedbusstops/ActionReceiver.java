package com.sit.itp_team_9_smartandconnectedbusstops;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ActionReceiver extends BroadcastReceiver {

    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        // an Intent broadcast.
        String action = intent.getStringExtra("action");
        if(action.equals("action1"))
            updateApp();
        else if(action.equals("action2"))
            goToFeedback();
        else
            goDefault();
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    private void updateApp(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1wgvp6lIvjLnC8sOza4nYgrL6n_mea2Sj"));
        mContext.startActivity(browserIntent);
    }
    private void goToFeedback(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/EgthF6mMFOLt6vci1"));
        mContext.startActivity(browserIntent);
    }
    private void goDefault(){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        mContext.startActivity(intent);
    }
}
