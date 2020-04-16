package com.ProjectITI.tripsproject.Alert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ProjectITI.tripsproject.Login.LoginFragment;
import com.ProjectITI.tripsproject.LoginSignup_Activity;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;

public class Trip_alert extends AppCompatActivity {
    String trip_id;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width= dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.10));

        final Intent intent = getIntent();
        trip_id = intent.getStringExtra("trip_id");
        final String action = intent.getStringExtra("action");

        AlertDialog.Builder builder = new AlertDialog.Builder(Trip_alert.this);
        if(action.equals("logout"))
        {
            builder.setMessage("Do you want to Logout ? ");
        }else {
            builder.setMessage("Do you want to " + action + " this trip ?");
        }
        builder.setTitle("Alert !");
        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        // When the user click yes button
                        if(action.equals("delete")) {
                            String s = intent.getStringExtra("state");
                            TripDao tripDao = new TripDao();
                            tripDao.deleteTrip(trip_id);
                           // TripDao.getAllData(s);
                        }else if(action.equals("cancel"))
                        {
                            TripDao tripDao = new TripDao();
                            tripDao.cancelTrip(trip_id);
                          //  TripDao.getAllData("upcoming");
                        }else{

                            /*
                            SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
                            editor.putString("name", "");
                            editor.commit();
                            */
                            TripDao tripDao = new TripDao();
                            tripDao.cancelAllAlarm();
                            FirebaseAuth.getInstance().signOut();
                            getSharedPreferences("USER", MODE_PRIVATE).edit().clear().commit();
                            Intent intent = new Intent(getApplicationContext(), LoginSignup_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            Log.i("tag","Loggin out");
                        }

                        Log.i("tag","Before Finish");
                        finish();
                    }
                });
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
