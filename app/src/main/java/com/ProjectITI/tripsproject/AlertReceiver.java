package com.ProjectITI.tripsproject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.core.app.NotificationCompat;


import com.victor.loading.rotate.RotateLoading;

public class AlertReceiver extends BroadcastReceiver {

    Ringtone r;
    MediaPlayer r2;
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClassName("com.ProjectITI.tripsproject","com.ProjectITI.tripsproject.ShowAlertDialog");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        //Ring(context);
        //DisplayDialogOverApps(context);
    }

/*
    public void Ring(Context context) {
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.O) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r2 = MediaPlayer.create(context, notification);
            r2.setLooping(true);
            r2.start();

        } else {
            Uri notification2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (notification2 == null) {
                // alert is null, using backup
                notification2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // I can't see this ever being null (as always have a default notification)
                // but just incase
                if (notification2 == null) {
                    // alert backup is null, using 2nd backup
                    notification2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            r = RingtoneManager.getRingtone(context, notification2);
            r.play();
        }
    }

    public void DisplayDialogOverApps(final Context context) {


        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params1 = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null, false);

        params1.height = dpToPx(300, context);
        params1.width = metrics.widthPixels - 40;
        params1.x = 0;
        params1.format = PixelFormat.TRANSLUCENT;
        params1.y = 0;

        wm.addView(view, params1);

        Button cancelButton, snoozeButton, startButton;

        cancelButton = view.findViewById(R.id.CancelButton);
        snoozeButton = view.findViewById(R.id.SnoozeButton);
        startButton = view.findViewById(R.id.StartButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wm.removeViewImmediate(view);
                /*int MyVersion = Build.VERSION.SDK_INT;
                if (MyVersion > Build.VERSION_CODES.O) {
                    if (r2!=null)
                    {
                        r2.stop();
                    }
                }
                else
                {
                    if (r!=null)
                    {
                        r.stop();
                    }
                }


            }
        });

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHelper notificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                notificationHelper.getManager().notify(1, nb.build());
                wm.removeViewImmediate(view);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wm.removeViewImmediate(view);
            }
        });

    }

    public int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }*/
}
