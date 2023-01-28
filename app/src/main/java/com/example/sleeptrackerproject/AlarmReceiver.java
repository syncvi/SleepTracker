package com.example.sleeptrackerproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, AlarmClockActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Alarm")
                .setContentText("Time to wake up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123, builder.build());

        Intent alarmDialogIntent = new Intent(context, AlarmClockActivity.class);
        alarmDialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmDialogIntent.putExtra("show_dialog", true);
        context.startActivity(alarmDialogIntent);
    }

}

