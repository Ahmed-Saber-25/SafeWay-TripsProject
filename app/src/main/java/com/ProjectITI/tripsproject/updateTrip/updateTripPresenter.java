package com.ProjectITI.tripsproject.updateTrip;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;

import java.util.Calendar;

public class updateTripPresenter implements updateTripContract.PresenterInterface {

    public updateTripPresenter() {
    }

    @Override
    public void updateTrip(String id , Trip trip, Calendar calendar) {
        TripDao tripDao = new TripDao();
        tripDao.EditTrip(id,trip,calendar);
    }
}