package com.sit.itp_team_9_smartandconnectedbusstops;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class TTFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = FirebaseMessagingService.class.getSimpleName();
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        storeToken(token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

    private void sendFcmTokenToServer(String token){
        //implement your code to send the token to the server
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                sendPushNotification(json);
                sendNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendNotification(JSONObject json) {
        Intent intent = new Intent(this, ActionReceiver.class);
        Log.e(TAG, "Notification JSON " + json.toString());
        try{
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            PendingIntent pendingIntent;
            if(title.contains("update") || title.contains("Update")) {
                intent.putExtra("action", "update");
                pendingIntent = PendingIntent.getBroadcast(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            }
            else if (title.contains("feedback") || title.contains("Feedback")) {
                intent.putExtra("action", "feedback");
                pendingIntent = PendingIntent.getBroadcast(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            } else{
                intent.putExtra("action", "default");
                pendingIntent = PendingIntent.getBroadcast(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

            }

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap rawBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            NotificationCompat.Builder groupBuilder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setLargeIcon(rawBitMap)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setGroupSummary(true)
                    .setGroup("TRANSITTHERE")
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setContentIntent(pendingIntent);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setLargeIcon(rawBitMap)
                    .setContentTitle(title)
                    .setContentText(message)
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

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private final static AtomicInteger c = new AtomicInteger(1);
    private static int getID(){
        return c.incrementAndGet();
    }
}
