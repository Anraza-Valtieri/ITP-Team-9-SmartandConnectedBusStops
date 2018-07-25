package com.sit.itp_team_9_smartandconnectedbusstops;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = AboutActivity.class.getSimpleName();
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
//                sendNotification();

                Intent intent = new Intent(this, ActionReceiver.class);
                intent.putExtra("action", "update");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Bitmap rawBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                NotificationCompat.Builder groupBuilder = new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setLargeIcon(rawBitMap)
                        .setContentTitle("Developer test, update!")
                        .setContentText("You can now restart the app")
                        .setGroupSummary(true)
                        .setGroup("TRANSITTHERE")
                        .setWhen(System.currentTimeMillis())
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentIntent(pendingIntent);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setLargeIcon(rawBitMap)
                        .setContentTitle("Developer test, update!")
                        .setContentText("You can now restart the app")
                        .setAutoCancel(true)
                        .setGroup("TRANSITTHERE")
                        .setSound(defaultSoundUri)
                        .setWhen(System.currentTimeMillis())
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentIntent(pendingIntent);

                if(intent.hasExtra("action") && intent.getStringExtra("action").equals("update")){
                    notificationBuilder.addAction(R.drawable.ic_update_black_24dp, "Update",pendingIntent);
                }
                if(intent.hasExtra("action") && intent.getStringExtra("action").equals("feedback")){
                    notificationBuilder.addAction(R.drawable.ic_feedback_black_24dp, "Feedback",pendingIntent);
                }

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel("0", "General Notifications", NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("General Notice");
                    channel.enableLights(true);
                    channel.setLightColor(Color.BLUE);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0 /* ID of notification */, groupBuilder.build());
                notificationManager.notify(getID() /* ID of notification */, notificationBuilder.build());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(BuildConfig.DEBUG) {
//            sendNotification();
        }
    }

    private final static AtomicInteger c = new AtomicInteger(1);
    private static int getID(){
        return c.incrementAndGet();
    }
}
