package com.ProjectITI.tripsproject.drawOnMap;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFrag extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<String> StartPoints = new ArrayList<>();
    ArrayList<String> EndPoints = new ArrayList<>();
    ArrayList<String> NamesList = new ArrayList<>();
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_maps, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TripDao tripDao = new TripDao();
        tripDao.getDrawRoutes(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void drawRoates(ArrayList<String> startPoints, ArrayList<String> endPoints, ArrayList<String> namesList) {
        NamesList = namesList;
        StartPoints = startPoints;
        EndPoints = endPoints;
        try {
            for (int i = 0; i < StartPoints.size(); i++) {
                String name = namesList.get(i);
                String location1 = StartPoints.get(i);
                Geocoder gc1 = new Geocoder(getContext());
                List<Address> addresses1 = gc1.getFromLocationName(location1, 5); // get the found Address Objects

                for (Address a : addresses1) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        Loc1 = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
                String location2 = EndPoints.get(i);
                Geocoder gc2 = new Geocoder(getContext());
                List<Address> addresses2 = gc2.getFromLocationName(location2, 5); // get the found Address Objects

                for (Address a : addresses2) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        Loc2 = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
                mMap.addMarker(new MarkerOptions().position(Loc1).title("Start Point").snippet(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                if (j > 8)
                    j = 0;
                mMap.addPolyline(
                        new PolylineOptions().add(Loc1).add(Loc2).width(10f).color(color[j])
                );
                mMap.addMarker(new MarkerOptions().position(Loc2).title("End Point").snippet(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                j++;
            }
            if(Loc1!=null)
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc1, 10f));
            }


        } catch (IOException e) {
            // handle the exception
        }
    }
}
