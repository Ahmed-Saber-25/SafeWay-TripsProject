package com.ProjectITI.tripsproject.ui.Upcoming_Trips;

import android.util.Log;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class upcomingPresenter implements upcomingContract.PresenterInterface {


    upcomingContract.ViewInterface view;
    TripDao tripDao ;

    public upcomingPresenter(upcomingContract.ViewInterface view, TripDao tripDao) {
        this.view = view;
        this.tripDao = tripDao;
    }

    @Override
    public void getupcomingList() {
        Log.i("tag","getupcomingList");
        tripDao.getAllData(this);
    }

    @Override
    public void setData(List<Trip> trips) {
        view.setupcomingData(trips);

    }

    @Override
    public void onDelete(String tripId) {
        tripDao.deleteTrip(tripId);
        //getupcomingList();
    }

    @Override
    public void stop() {
        view = null;
    }


}
