package com.ProjectITI.tripsproject.ui.Trips_History;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;

import java.util.List;

public class historyPresenter implements historyContract.PresenterInterface  {

    historyContract.ViewInterface view;
    TripDao tripDao;

    public historyPresenter(historyContract.ViewInterface view, TripDao tripDao) {
        this.view = view;
        this.tripDao = tripDao;
    }

    @Override
    public void getHistoryList() {
        tripDao.getHistoryData(this);
    }

    @Override
    public void setData(List<Trip> trips) {
        view.setHistoryData(trips);
    }

    @Override
    public void onDelete(String tripId) {
        tripDao.deleteTrip(tripId);
       // getHistoryList();
    }

    @Override
    public void stop() {
        view = null;
    }
}
