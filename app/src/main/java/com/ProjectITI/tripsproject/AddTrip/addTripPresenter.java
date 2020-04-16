package com.ProjectITI.tripsproject.AddTrip;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;

import java.util.ArrayList;
import java.util.Calendar;

public class addTripPresenter implements addTripContract.PresenterInterface {
    TripDao tripDao ;
    addTripContract.ViewInterface view;

    public addTripPresenter(TripDao tripDao) {
        this.tripDao = tripDao;
    }

    @Override
    public void addTrip(Trip trip, Calendar calendar) {
        ArrayList<String> empty_notes = new ArrayList<>();
        tripDao.AddTrip(trip,empty_notes,calendar,false);
    }
}