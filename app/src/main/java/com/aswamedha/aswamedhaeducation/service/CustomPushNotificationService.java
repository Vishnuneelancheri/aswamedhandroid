package com.aswamedha.aswamedhaeducation.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.questions.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CustomPushNotificationService extends FirebaseMessagingService {
    public CustomPushNotificationService(){super();}
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("token","received");
        remoteMessage.getData();
        sendNotification(remoteMessage.getData().get("message"));
    }
    private void sendNotification(String message) {

        /*String title;
        String body;*/

        try{
            /*JSONObject jsonObject = new JSONObject(messageBody);
            title = jsonObject.getString("title");
            body = jsonObject.getString("body");*/
            Log.d("test_msg", message);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            String channelId = getString(R.string.adMobId);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setColor(getResources().getColor(android.R.color.white))
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String id = getString(R.string.adMobId);
                CharSequence name = getString(R.string.app_name);
                String description = message;
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.setShowBadge(true);
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }catch (Exception e){

        }
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

}
