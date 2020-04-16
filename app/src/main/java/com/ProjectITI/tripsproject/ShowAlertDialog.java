package com.ProjectITI.tripsproject;

import android.animation.Animator;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.ui.Upcoming_Trips.FloatingWidgetService;
import com.appolica.flubber.Flubber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowAlertDialog extends AppCompatActivity {

    Ringtone r;
    MediaPlayer r2;
    TripDao tripDao;

    String key, repeat, name, startPoint, endPoint, startDate, time, type;
    ArrayList<String> notes = new ArrayList<>();

    boolean add2request = false;
    public static Intent floatingIntent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripDao = new TripDao();
        if (getIntent() != null) {
            key = getIntent().getStringExtra("key");
            repeat = getIntent().getStringExtra("repeat");
            name = getIntent().getStringExtra("name");
            startPoint = getIntent().getStringExtra("from");
            endPoint = getIntent().getStringExtra("to");
            startDate = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            type = getIntent().getStringExtra("type");

            tripDao.getTripNotes(key, this);
        }
        Ring(this);
        MyCustomDialog(this);
    }

    Dialog tripDialog;

    public void MyCustomDialog(final Context context) {

        tripDialog = new Dialog(context);
        tripDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tripDialog.setContentView(R.layout.alert_dialog);
        Window window = tripDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        tripDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tripDialog.setCancelable(false);

        Button cancelButton, snoozeButton, startButton;
        ImageView alarmImage = tripDialog.findViewById(R.id.AlarmImage);
        TextView tripName = tripDialog.findViewById(R.id.TripNameText);

        tripName.setText(name);

        cancelButton = tripDialog.findViewById(R.id.CancelButton);
        snoozeButton = tripDialog.findViewById(R.id.SnoozeButton);
        startButton = tripDialog.findViewById(R.id.StartButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRing();
                tripDao.cancelTrip(getIntent().getStringExtra("key"));
                finish();
            }
        });

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRing();
                NotificationHelper notificationHelper = new NotificationHelper(context, getIntent().getStringExtra("name"), "wait your trip");
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

                Intent notifyIntent = getIntent();
                notifyIntent.setClassName("com.ProjectITI.tripsproject", "com.ProjectITI.tripsproject.ShowAlertDialog");
                notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        getApplicationContext(), notifyIntent.getIntExtra("request", 0), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                nb.setContentIntent(notifyPendingIntent);
                notificationHelper.getManager().notify(getIntent().getIntExtra("request", 0), nb.build());
                tripDao.setWaitingTrip(getIntent().getStringExtra("key"));
                finish();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRing();
                tripDao.DoneTrip(getIntent().getStringExtra("key"));
                gotToMap(getIntent().getStringExtra("from"), getIntent().getStringExtra("to"));

                if (repeat.equals("Repeat Weekly") || repeat.equals("Repeat Daily") || repeat.equals("Repeat Monthly")) {
                    Trip new_trip = new Trip(name, startPoint, endPoint, time, startDate, "upcoming", type, repeat, false);
                    addNewTrip_Reapet(repeat, new_trip, notes);
                    add2request = true;
                }
                //One Way Trip, Round Trip
                if (type.equals("Round Trip")) {
                    Trip new_trip = new Trip(name, startPoint, endPoint, time, startDate, "upcoming", type, repeat, true);
                    addNewTrip_RoundTrip(new_trip, notes, add2request);


                }
                if (floatingIntent != null) {
                    context.stopService(floatingIntent);
                }
                floatingIntent = new Intent(context, FloatingWidgetService.class);
                //newIntent.putExtra("position", values.get(position).getId());
                floatingIntent.putStringArrayListExtra("notes", notes);
                context.startService(floatingIntent);
                finish();
            }
        });

        Animator animator = Flubber.with()
                .animation(Flubber.AnimationPreset.MORPH)
                .repeatCount(0)
                .duration(1000)
                .createFor(alarmImage);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();


        tripDialog.show();
    }

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
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    public void stopRing() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.O) {
            if (r2 != null) {
                r2.stop();
            }
        } else {
            if (r != null) {
                r.stop();
            }
        }
    }

    public void gotToMap(String source, String destiaion) {

        String uri = "http://maps.google.com/maps?f=d&hl=en" + "&daddr=" + destiaion;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void addNewTrip_RoundTrip(Trip trip, ArrayList<String> notes, boolean add) {
        String start = trip.getTo();
        String end = trip.getFrom();
        trip.setRepeat("No Repeat");
        trip.setStatus("upcoming");
        trip.setType("One Way Trip");
        trip.setFrom(start);
        trip.setTo(end);
        trip.setWait(true);
        trip.setName(trip.getName() + " - BACK");

        TripDao tripDao = new TripDao();
        tripDao.AddTrip(trip, notes, Calendar.getInstance(), add);



        /*
        Intent intent = new Intent(context,showCalenderandTimepicker.class);
        intent.putExtra("tripName", trip.getName());
        intent.putExtra("endPoint", trip.getTo());
        intent.putExtra("date", trip.getDate());
        intent.putExtra("time", trip.getTime());
        intent.putExtra("type", trip.getType());
        intent.putExtra("repeat", trip.getRepeat());
        intent.putExtra("startPoint", trip.getFrom());

        intent.putStringArrayListExtra("notes", notes);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
*/
    }


    private void addNewTrip_Reapet(String repeat, Trip trip, ArrayList<String> notes) {
        String newDate = "";
        Date trip_date;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            trip_date = df.parse(trip.getDate());
            Log.i("tag", "new Date  : " + trip_date.toString());
            cal.setTime(trip_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (repeat.equals("Repeat Weekly")) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            newDate = day + "/" + Month + "/" + year;
            trip.setDate(newDate);

        } else if (repeat.equals("Repeat Daily")) {

            cal.add(Calendar.DAY_OF_MONTH, 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            newDate = day + "/" + Month + "/" + year;
            trip.setDate(newDate);

        } else if (repeat.equals("Repeat Monthly")) {
            cal.add(Calendar.MONTH, 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            newDate = day + "/" + Month + "/" + year;
            trip.setDate(newDate);
        }
        ArrayList<String> notes2 = new ArrayList<>();
        TripDao tripDao = new TripDao();
        tripDao.AddTrip(trip, notes, cal, false);

    }

    public void fillNotes(ArrayList<String> map) {
        notes = map;
    }
}
