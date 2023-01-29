package com.example.sleeptrackerproject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private Context _context;

    public NotificationHelper(Context context) {
        _context = context;
    }

    public static void sendNotification(Context context, String title, String message) {
        // create an explicit intent for an Activity
        // when user taps the noti it starts main activity
        Intent intent = new Intent(context, MainActivity.class);
        // new activity should be started and any existing tasks should be cleared
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // when user taps the noti it starts main activity
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(123, builder.build());
    }
}

