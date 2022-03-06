package com.coin.rank.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (remoteMessage.getNotification() == null) {
            return;
        }
        Notification notification = new NotificationCompat.Builder(this, "notices")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .build();
        notificationManager.notify(new Random().nextInt(), notification);

    }
}
