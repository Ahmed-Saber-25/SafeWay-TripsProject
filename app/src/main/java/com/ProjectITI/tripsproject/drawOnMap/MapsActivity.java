package com.ProjectITI.tripsproject.drawOnMap;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<String> StartPoints = new ArrayList<>();
    ArrayList<String> EndPoints = new ArrayList<>();
    LatLng Loc1;
    LatLng Loc2;
    int j = 0;
    int BLACK = 0xFF000000;
    int GRAY = 0xFF888888;
    int WHITE = 0xFFFFFFFF;
    int RED = 0xFFFF0000;
    int GREEN = 0xFF00FF00;
    int BLUE = 0xFF0000FF;
    int YELLOW = 0xFFFFFF00;
    int CYAN = 0xFF00FFFF;
    int MAGENTA = 0xFFFF00FF;
    int[] color = {RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, BLACK, GRAY, WHITE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        StartPoints = TripDao.start;
        EndPoints = TripDao.end;
        Log.i("tag","-----------");
        Log.i("tag","EndPoints" + EndPoints.size());

        Log.i("tag","StartPoints" + StartPoints.size());
        if (StartPoints == null || EndPoints == null || StartPoints.size() == 0 || EndPoints.size() == 0) {
            Toast.makeText(getApplicationContext(), "No Avilabe trips ! PLease try again", Toast.LENGTH_LONG).show();
        } else {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            for (int i = 0; i < StartPoints.size(); i++) {
                String location1 = StartPoints.get(i);
                Geocoder gc1 = new Geocoder(this);
                List<Address> addresses1 = gc1.getFromLocationName(location1, 5); // get the found Address Objects

                for (Address a : addresses1) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        Loc1 = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
                String location2 = EndPoints.get(i);
                Geocoder gc2 = new Geocoder(this);
                List<Address> addresses2 = gc2.getFromLocationName(location2, 5); // get the found Address Objects

                for (Address a : addresses2) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        Loc2 = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
                mMap.addMarker(new MarkerOptions().position(Loc1).title("Start Point"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc1, 10f));
                if (j > 8)
                    j = 0;
                mMap.addPolyline(
                        new PolylineOptions().add(Loc1).add(Loc2).width(3f).color(color[j])
                );
                mMap.addMarker(new MarkerOptions().position(Loc2).title("End Point"));

                j++;


            }

        } catch (IOException e) {
            // handle the exception
        }

    }
}