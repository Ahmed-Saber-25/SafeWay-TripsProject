package com.ProjectITI.tripsproject.updateTrip;

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
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class UpdateTripData extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, updateTripContract.ViewInterface {

    private updateTripContract.PresenterInterface updateTripPresenter;
    private TextView tripName;
    private TextView timeSelected;
    private ImageView timePicker;
    DatePickerDialog picker;
    private TextView dateSelected;
    private ImageView datePicker;
    private Spinner type, repeat;
    PlacesClient placesClient;
    AutocompleteSupportFragment autocompleteStartPointFragment;
    AutocompleteSupportFragment autocompleteEndPointFragment;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    String apiKey1 = "AIzaSyCotxw";
    String apiKey2 = "-usxY5l35Q2";
    String apiKey3 = "i3ICLKZ3";
    String apiKey4 = "iFnbTyVkE";
    String tripID;
    int minDay, minYear, minMonth;
    int selected_year, selected_day, selected_Month;

    String start = "";
    String end = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip_data);
        updateTripPresenter = new updateTripPresenter();
        setUpComponents();

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                minDay = cldr.get(Calendar.DAY_OF_MONTH);
                minMonth = cldr.get(Calendar.MONTH);
                minYear = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(UpdateTripData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateSelected.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        selected_year = year;
                        selected_day = dayOfMonth;
                        selected_Month = monthOfYear;
                    }
                }, minYear, minMonth, minDay);
                picker.show();
            }
        });

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(UpdateTripData.this, apiKey1+apiKey2+apiKey3+apiKey4);
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
                        // Toast.makeText(UpdateTripData.this, ""+latLng.latitude, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(UpdateTripData.this, "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
                        // Toast.makeText(UpdateTripData.this, ""+latLng.latitude, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(UpdateTripData.this, "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        setValuesFromIntent();
    }

    private void setUpComponents() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        tripName = findViewById(R.id.tripNameUpdated_Field);
        timeSelected = findViewById(R.id.time_Updated);
        timePicker = findViewById(R.id.time_Picker);
        dateSelected = findViewById(R.id.date_Updated);
        datePicker = findViewById(R.id.date_Picker);
        autocompleteStartPointFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.startPoint_fragment);
        autocompleteEndPointFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.endPoint_fragment);

        type = findViewById(R.id.direction_Spinner_updated);
        repeat = findViewById(R.id.repeat_Spinner_Updated);


    }

    public void setValuesFromIntent() {
        Intent intent = getIntent();
        tripID = intent.getStringExtra("id");

        tripName.setText(intent.getStringExtra("tripName"));
        timeSelected.setText(intent.getStringExtra("time"));
        dateSelected.setText(intent.getStringExtra("date"));
        autocompleteStartPointFragment.setText(intent.getStringExtra("startPoint"));
        start = intent.getStringExtra("startPoint");
        autocompleteEndPointFragment.setText(intent.getStringExtra("endPoint"));
        end = intent.getStringExtra("endPoint");
        String tripRepeat = (intent.getStringExtra("repeat"));
        // No Repeat, Repeat Daily, Repeat Weekly, Repeat Monthly
        if (tripRepeat.equals("No Repeat")) {
            repeat.setSelection(0);
        } else if (tripRepeat.equals("Repeat Monthly")) {
            repeat.setSelection(1);
        } else if (tripRepeat.equals("No Repeat")) {
            repeat.setSelection(2);
        } else {
            repeat.setSelection(3);
        }
        String triptype = (intent.getStringExtra("type"));
        // One Way Trip, Round Trip
        if (triptype.equals("One Way Trip")) {
            type.setSelection(0);
        } else if (triptype.equals("Round Trip")) {
            type.setSelection(1);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        timeSelected.setText(timeText);
    }


    public void updateTripClick(View view) {
        updateTrip();


    }

    public void updateTrip() {
        Calendar fullCalender = convertDate(dateSelected.getText().toString(), timeSelected.getText().toString());
        if (tripName.getText().toString().trim().isEmpty()) {
            tripName.setError("fill trip name");
        } else if (start.equals("") || end.equals("")) {
            Toast.makeText(getApplicationContext(), "select start point and end point of the trip", Toast.LENGTH_LONG).show();
        } else if (Calendar.getInstance().getTimeInMillis() >= fullCalender.getTimeInMillis()) {
            Toast.makeText(getApplicationContext(), "the date of the trip is over", Toast.LENGTH_LONG).show();
        } else {
            String trip_name = tripName.getText().toString();
            String time = timeSelected.getText().toString();
            String date = dateSelected.getText().toString();
            String trip_repeat = repeat.getSelectedItem().toString();
            String status = "upcoming";
            String trip_type = type.getSelectedItem().toString();
            //Trip(String name, String from, String to, String time, String date, String status, String type, String repeat) {
            //public Trip(String id, String name, String from, String to, String time, String date, String status, String type, String repeat, ArrayList<String> notes) {
            Trip trip = new Trip(trip_name, start, end, time, date, status, trip_type, trip_repeat, false);
            updateTripPresenter.updateTrip(tripID, trip, convertDate(date, time));
            returnToMain();
        }

    }

    private Calendar convertDate(String date, String time) {
        Date trip_date;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy h:mm a");
        try {
            trip_date = df.parse(date + " " + time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(trip_date);
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
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
