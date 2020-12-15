package com.devx.fooddelivery.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.devx.fooddelivery.MainActivity;
import com.devx.fooddelivery.R;

public class NotificationService extends ContextWrapper {

    private static final String TAG = "Notification Service";

    public NotificationService(Context base) {
        super(base);
        createChannels();
    }

    private String channelName = "notification channel";
    private String channelId = "app " + channelName;

    private void createChannels() {
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    public void createNotification(){
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .setContentTitle("Your Order is Successfull !🍔")
                .setContentText("Food will be delivered soon")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat.from(this).notify(1, notification);
    }

    public void SuccessNotification(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data", "channel");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .setContentTitle("Your Order is Successfull !🍔")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText("Food will be delivered soon")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat.from(this).notify(1, notification);
    }




}
