package com.ProjectITI.tripsproject.ui.Trips_History;

import com.ProjectITI.tripsproject.Model.Trip;

import java.util.ArrayList;
import java.util.List;

public interface historyContract {
    interface PresenterInterface {

        void getHistoryList();

        void setData(List<Trip> trips);

        void onDelete(String tripId);

        void stop();
    }

    interface ViewInterface {

        void Notes(String tripid, ArrayList<String> notes);

        void displayMessage(String msg);

        void setHistoryData(List<Trip> upcomingtrips);

        void NoHistoryTrips();

    }
}
