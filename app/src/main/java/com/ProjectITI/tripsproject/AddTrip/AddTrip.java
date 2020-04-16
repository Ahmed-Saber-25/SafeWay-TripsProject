package com.ProjectITI.tripsproject.AddTrip;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

import com.ProjectITI.tripsproject.AlertReceiver;
import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.ProjectITI.tripsproject.TimePickerFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class AddTrip extends AppCompatActivity implements addTripContract.ViewInterface {

    private TripDao tripDao;
    private addTripContract.PresenterInterface addTripPresenter;
    private TextView timeSelected;
    private ImageView timePicker;
    DatePickerDialog picker;
    private TextView dateSelected;
    private ImageView datePicker;
    private Spinner repeat, type;
    PlacesClient placesClient;
    TextInputEditText tripNameEditText;

    int cur_hour, cur_mins;
    int selected_hourOfDay, selected_minute;

    int minDay, minYear, minMonth;
    int selected_year, selected_day, selected_Month;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    AutocompleteSupportFragment autocompleteStartPointFragment;
    AutocompleteSupportFragment autocompleteEndPointFragment;

    String apiKey1 = "AIzaSyBHpF_";
    String apiKey2 = "TLWue13wv";
    String apiKey3 = "xHvbIqugSt";
    String apiKey4 = "_3LOa2Acw";

    String start = "";
    String end = "";

    boolean DateIsSet=false;
    boolean TimeIsSet=false;

    Calendar fullCalender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tripDao = new TripDao();

        addTripPresenter = new addTripPresenter(tripDao) ;

        setUpComponents();

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mcurrentTime.set(Calendar.HOUR_OF_DAY,selectedHour);
                        mcurrentTime.set(Calendar.MINUTE,selectedMinute);
                        mcurrentTime.set(Calendar.SECOND,0);
                        updateTimeText(mcurrentTime);

                        SetTimeCalender(selectedHour,selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddTrip.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateSelected.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        SetDateCalender(dayOfMonth,monthOfYear,year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(AddTrip.this, apiKey1+apiKey2+apiKey3+apiKey4);
        }
        // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(this);

        //StartPoint Fragment
        autocompleteStartPointFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteStartPointFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        final LatLng latLng = place.getLatLng();
                        start = place.getAddress();
                       // Toast.makeText(AddTrip.this, "" + latLng.latitude, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Status status) {
                        displayError(status.getStatusMessage());
                    }
                });

        //EndPoint Fragment

        autocompleteEndPointFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteEndPointFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        final LatLng latLng = place.getLatLng();
                        end = place.getAddress();
                       // Toast.makeText(AddTrip.this, "" + latLng.latitude, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Status status) {
                        displayError(status.getStatusMessage());
                    }
                });

        //mAuth = FirebaseAuth.getInstance();
        //databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    private void setUpComponents() {
        fullCalender = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        tripNameEditText = findViewById(R.id.tripNameUpdated_Field);
        timeSelected = findViewById(R.id.time_Selected);
        timePicker = findViewById(R.id.time_Picker);

        dateSelected = findViewById(R.id.date_Selected);
        datePicker = findViewById(R.id.date_Picker);

        type = findViewById(R.id.direction_Spinner);
        repeat = findViewById(R.id.repeat_Spinner);

        autocompleteStartPointFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.startPoint_fragment);
        autocompleteEndPointFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.endPoint_fragment);

    }


    public void SetTimeCalender(int hourOfDay,int minute) {
        fullCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
        fullCalender.set(Calendar.MINUTE, minute);
        fullCalender.set(Calendar.SECOND, 0);
        TimeIsSet = true;
        timeSelected.setError(null);
    }
    public void SetDateCalender(int day,int month,int year) {
        fullCalender.set(Calendar.DAY_OF_MONTH, day);
        fullCalender.set(Calendar.MONTH, month);
        fullCalender.set(Calendar.YEAR, year);
        DateIsSet = true;
        dateSelected.setError(null);
    }



    private void updateTimeText(Calendar c) {
        String timeText = "";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());


        timeSelected.setText(timeText);
    }

    private void startAlarm(Calendar c,String key,int request) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("name",tripNameEditText.getText().toString());
        intent.putExtra("from","");
        intent.putExtra("to","");
        intent.putExtra("type",type.getSelectedItem().toString());
        intent.putExtra("repeat",repeat.getSelectedItem().toString());
        intent.putExtra("key",key);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, request, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
        else
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }


    public void addTrip() {

                String trip_name = tripNameEditText.getText().toString();
                String to = "to test";
                String time = timeSelected.getText().toString();
                String date = dateSelected.getText().toString();
                String trip_repeat = repeat.getSelectedItem().toString();
                String status = "upcoming";
                String trip_type = type.getSelectedItem().toString();
                Trip trip = new Trip(trip_name, start, end, time, date, status, trip_type, trip_repeat,false);
                ArrayList<String> notes = new ArrayList<>();


            addTripPresenter.addTrip(trip,fullCalender);
                returnToMain();


    }



    public void addTripClick(View view) {
        if (tripNameEditText.getText().toString().trim().isEmpty())
        {
            tripNameEditText.setError("fill trip name");
        }
        else if (start.equals("") || end.equals(""))
        {
            Toast.makeText(getApplicationContext(),"select start point and end point of the trip",Toast.LENGTH_LONG).show();
        }
        else if (!TimeIsSet)
        {
            timeSelected.setError("");
            timeSelected.setText("Set time");
        }
        else if (!DateIsSet)
        {
            dateSelected.setError("");
            dateSelected.setText("Set date");
        }
        else if (Calendar.getInstance().getTimeInMillis() >= fullCalender.getTimeInMillis())
        {
            Toast.makeText(getApplicationContext(),"the date of the trip is over",Toast.LENGTH_LONG).show();
        }
        else
        {
            addTrip();
        }

    }

    @Override
    public void returnToMain() {
        finish();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void displayError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

}
