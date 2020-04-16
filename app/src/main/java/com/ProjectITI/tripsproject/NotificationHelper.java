package com.ProjectITI.tripsproject;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ProjectITI.tripsproject.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    String title;
    String body;
    public NotificationHelper(Context base,String title, String body) {
        super(base);

        this.title = title;
        this.body = body;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setOngoing(true)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.clock_icon);
    }
}
