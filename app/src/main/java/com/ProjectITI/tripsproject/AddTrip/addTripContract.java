package com.ProjectITI.tripsproject.AddTrip;

import com.ProjectITI.tripsproject.Model.Trip;

import java.util.Calendar;

public interface addTripContract {
    interface PresenterInterface {

            void addTrip(Trip trip, Calendar calendar);
        }

        interface ViewInterface {

            void returnToMain();

            void displayMessage(String message);

            void displayError(String message);

        }
    }


