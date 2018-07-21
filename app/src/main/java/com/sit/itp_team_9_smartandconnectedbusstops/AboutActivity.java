package com.sit.itp_team_9_smartandconnectedbusstops;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private Button mButtonBack;
    private TextView mVersion;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mButtonBack = findViewById(R.id.btnBack);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVersion = findViewById(R.id.version);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            mVersion.setText(version);
            if(BuildConfig.DEBUG) {
                prefs = getSharedPreferences("com.sit.item_team_9_smartandconnectedbusstops", MODE_PRIVATE);
                prefs.edit().putBoolean("firstrun", true).commit();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
