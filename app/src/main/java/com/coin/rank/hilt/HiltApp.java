package com.coin.rank.hilt;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


import androidx.annotation.RequiresApi;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HiltApp extends Application {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("notices", "Notices", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("This is a Notification for Notices");
        notificationChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
