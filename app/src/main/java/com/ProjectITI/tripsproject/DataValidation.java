package com.ProjectITI.tripsproject;

import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;

public class DataValidation {

    public static boolean isNotEmpty(EditText inputTextField, TextInputLayout inputLabel, String validationText) {
        boolean isEmpty = true;
        String validationString = null;

        if (inputTextField.getText().toString().trim().length()==0) {
            isEmpty = false;
            validationString = validationText;
        }
        
        inputLabel.setError(validationString);
        return isEmpty;
    }
    public static boolean isMatch(EditText passTextField, EditText rePassTextField, TextInputLayout inputLabel, String validationText) {
        boolean isMatch = true;
        String validationString = null;

        if (!rePassTextField.getText().toString().equals(passTextField.getText().toString())) {
            isMatch = false;
            validationString = validationText;
        }
        
        inputLabel.setError(validationString);
        return isMatch;
    }
    
    public static boolean dataLength(EditText inputTextField,TextInputLayout inputLabel, String validationText, int requiredLength) {
        boolean isDataLength = true;
        String validationString = null;

        if (inputTextField.getText().length() < requiredLength) {
            isDataLength = false;
            validationString = validationText;

        }
        inputLabel.setError(validationString);
        return isDataLength;

    }

    public static boolean UserNameFormat(EditText inputTextField, TextInputLayout inputLabel, String validationText) {
        boolean isNumeric = true;
        String validationString = null;

        if (!inputTextField.getText().toString().matches("[a-zA-Z0-9]+")) {
            isNumeric = false;
            validationString = validationText;

        }
        inputLabel.setError(validationString);
        return isNumeric;

    }
    
    public static boolean emailFormat(EditText inputTextField, TextInputLayout inputLabel, String validationText) {
        boolean isEmail = true;
        String validationString = null;

        if (!inputTextField.getText().toString().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com")) {
            isEmail = false;
            validationString = validationText;

        }
        inputLabel.setError(validationString);
        return isEmail;

    }


}
