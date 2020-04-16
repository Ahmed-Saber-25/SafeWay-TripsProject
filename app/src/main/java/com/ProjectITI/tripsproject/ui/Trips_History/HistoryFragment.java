package com.ProjectITI.tripsproject.ui.Trips_History;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ProjectITI.tripsproject.HomeScreen;
import com.ProjectITI.tripsproject.Login.LoginPresenter;
import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment  implements historyContract.ViewInterface{
    private historyContract.PresenterInterface presenterInterface;
    public static Context context = HomeScreen.context;
    public String userId = FirebaseAuth.getInstance().getUid();

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static View noTrips;

    private TripDao tripDao;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        noTrips = root.findViewById(R.id.history_no_trips_layout);
        recyclerView = (RecyclerView) root.findViewById(R.id.history_recycleView_id);
        tripDao = new TripDao();

//        presenterInterface = new historyPresenter();
       // presenterInterface.getAllData();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        historyPresenter main = new historyPresenter(this,tripDao);
        main.getHistoryList();
    }



    @Override
    public void Notes(String tripid, ArrayList<String> notes) {

    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setHistoryData(List<Trip> upcomingtrips) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        if (upcomingtrips.size() == 0) {
            NoHistoryTrips();
        } else {
            noTrips.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter = new RV_Trips_HistoryAdapter(context, upcomingtrips);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void NoHistoryTrips() {
        noTrips.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}