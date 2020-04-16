package com.ProjectITI.tripsproject.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ProjectITI.tripsproject.HomeScreen;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements LoginContract.ViewInterface {

    private static final int RC_SIGN_IN = 99;
    Button signUpButton;

    EditText EmailEditText;
    EditText PassEditText;

    TextInputLayout EmailInputLayout;
    TextInputLayout PassInputLayout;

    Button loginButton;

    LoginContract.PresenterInterface presenterInterface;

    LoginButton loginButtonFacebook;
    SignInButton loginButtonGmail;

    GoogleSignInClient mGoogleSignInClient;

    private OnFragmentInteractionListener mListener;
    CallbackManager mCallbackManager;

    FirebaseAuth mAuth;
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setupPresenter();
        setupView(view);
        return view;
    }

    public void setupView(View view)
    {
        FacebookSdk.sdkInitialize(getContext());
        mAuth = FirebaseAuth.getInstance();

        EmailEditText = view.findViewById(R.id.Email);
        PassEditText = view.findViewById(R.id.Password);
        EmailInputLayout = view.findViewById(R.id.input_layout_Email);
        PassInputLayout = view.findViewById(R.id.input_layout_Password);
        loginButton = view.findViewById(R.id.Login);
        signUpButton = view.findViewById(R.id.SignUpActivity);
        loginButtonGmail = view.findViewById(R.id.login_button_gmail);
        loginButtonFacebook = view.findViewById(R.id.login_button_facebook);

        //Configure Facebook Sign In
        mCallbackManager = CallbackManager.Factory.create();
        loginButtonFacebook.setFragment(this);
        loginButtonFacebook.setReadPermissions("email", "public_profile");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),gso);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EmailEditText.getText().toString().trim().isEmpty())
                {
                    EmailInputLayout.setError("fill Email");
                }
                else if (PassEditText.getText().toString().isEmpty())
                {
                    PassInputLayout.setError("fill Password");
                }
                else
                {
                    presenterInterface.Login(EmailEditText.getText().toString(),PassEditText.getText().toString());
                }
            }
        });

        loginButtonGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



        loginButtonFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("looooooooooooog", "facebook:onSuccess:" + loginResult.getAccessToken());
                LoginManager.getInstance().logOut();
                presenterInterface.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("looooooooooooog", "canceled" );
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("looooooooooooog", "error: " + error.getMessage() );
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.gotoSignup();
            }
        });
    }

    public void setupPresenter()
    {
        presenterInterface = new LoginPresenter(this,getActivity());
    }

    @Override
    public void LoginSucceed(String email,String pass,String userName) {
        TripDao tripDao = new TripDao();
        tripDao.setActivity(getActivity());
        tripDao.startAllAlarm();
        //Toast.makeText(getContext(),userName,Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("USER", MODE_PRIVATE).edit();
        editor.putString("name", userName);
        editor.commit();
        startActivity(new Intent(getContext(), HomeScreen.class));
        getActivity().finish();
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
        void gotoSignup();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenterInterface.firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleSignIn", "Google sign in failed", e);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
