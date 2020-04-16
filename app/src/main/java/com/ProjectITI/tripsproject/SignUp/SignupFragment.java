package com.ProjectITI.tripsproject.SignUp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ProjectITI.tripsproject.DataValidation;
import com.ProjectITI.tripsproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignupFragment extends Fragment implements SignUpContract.ViewInterface{

    private OnFragmentInteractionListener mListener;

    Button loginButton;
    Button signupButton;

    EditText UserNameEditText;
    EditText EmailEditText;
    EditText PassEditText;
    EditText ConfirmPassEditText;

    TextInputLayout UserNameInputLayout;
    TextInputLayout EmailInputLayout;
    TextInputLayout PassInputLayout;
    TextInputLayout ConfirmPassInputLayout;

    SignUpContract.PresenterInterface presenterInterface;

    public SignupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        setupPresinter();
        setupView(view);

        return view;
    }

    public void setupView(View view)
    {
        UserNameEditText = view.findViewById(R.id.UserName);;
        EmailEditText = view.findViewById(R.id.Email);
        PassEditText = view.findViewById(R.id.Password);
        ConfirmPassEditText = view.findViewById(R.id.PasswordConfirm);

        UserNameInputLayout = view.findViewById(R.id.input_layout_UserName);
        EmailInputLayout = view.findViewById(R.id.input_layout_Email);
        PassInputLayout = view.findViewById(R.id.input_layout_Password);
        ConfirmPassInputLayout = view.findViewById(R.id.input_layout_Password_Confirm);

        signupButton = view.findViewById(R.id.SignUp);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        loginButton = view.findViewById(R.id.LoginActivity);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.gotoLogin();
            }
        });
    }

    public void setupPresinter()
    {
        presenterInterface = new SignUpPresenter(this, (AppCompatActivity) getActivity());
    }

    public void signUp() {
        boolean emailValidation = DataValidation.isNotEmpty(EmailEditText, EmailInputLayout, "Enter your email");
        boolean userNameValidation = DataValidation.isNotEmpty(UserNameEditText, UserNameInputLayout, "Enter your user name");
        boolean passValidation = DataValidation.isNotEmpty(PassEditText, PassInputLayout, "Enter your password");
        boolean rePassValidation = DataValidation.isNotEmpty(ConfirmPassEditText, ConfirmPassInputLayout, "Enter your Confirm-password");
        if (emailValidation) {
            emailValidation = DataValidation.emailFormat(EmailEditText, EmailInputLayout, "Format must be name@emailaddress.com");
        }
        if (userNameValidation) {
            userNameValidation = DataValidation.dataLength(UserNameEditText, UserNameInputLayout, "Minimum number of characters is 6", 6);
        }
        if (passValidation) {
            passValidation = DataValidation.dataLength(PassEditText, PassInputLayout, "Minimum number of characters is 6", 6);
        }
        if (rePassValidation) {
            rePassValidation = DataValidation.isMatch(PassEditText, ConfirmPassEditText, ConfirmPassInputLayout, "Confirm-password not match password");
        }

        if (emailValidation && userNameValidation && passValidation && rePassValidation) {
            final String email = EmailEditText.getText().toString();
            final String userName = UserNameEditText.getText().toString();
            final String pass = PassEditText.getText().toString();
            presenterInterface.createUser(email,userName,pass);
            Log.d("Validation","Valid");
        } else {
            Log.d("Validation","notValid");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void gotoLogin();
    }
}
