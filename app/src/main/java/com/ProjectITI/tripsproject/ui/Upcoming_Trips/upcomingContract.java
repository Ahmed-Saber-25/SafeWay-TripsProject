package com.ProjectITI.tripsproject.ui.Upcoming_Trips;

import com.ProjectITI.tripsproject.Model.Trip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Calendar;

public interface upcomingContract {
    interface PresenterInterface {

        void getupcomingList();

        void setData(List<Trip> trips);

        void onDelete(String tripId);

        void stop();
    }

    interface ViewInterface {

        void Notes(String tripid, ArrayList<String> notes);

        void displayMessage(String msg);

        void setupcomingData(List<Trip> upcomingtrips);

        void NoupcomingTrips();

    }
}
