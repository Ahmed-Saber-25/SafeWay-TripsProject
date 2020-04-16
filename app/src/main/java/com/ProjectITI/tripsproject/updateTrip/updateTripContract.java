package com.ProjectITI.tripsproject.updateTrip;

import com.ProjectITI.tripsproject.Model.Trip;

import java.util.Calendar;

public interface updateTripContract {
    interface PresenterInterface {

        void updateTrip(String id , Trip trip, Calendar calendar);

    }

        interface ViewInterface {

            void returnToMain();

            void displayMessage(String message);

            void displayError(String message);

        }
    }


