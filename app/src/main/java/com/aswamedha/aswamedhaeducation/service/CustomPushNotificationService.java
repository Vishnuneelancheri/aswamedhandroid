package com.aswamedha.aswamedhaeducation.service;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CustomPushNotificationService extends FirebaseMessagingService {
    public CustomPushNotificationService(){super();}
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }
    private void sendNotification(String message) {

    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

}
