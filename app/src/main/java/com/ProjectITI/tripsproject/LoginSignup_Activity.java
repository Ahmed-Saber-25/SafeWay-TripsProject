package com.ProjectITI.tripsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

import com.ProjectITI.tripsproject.Login.LoginFragment;
import com.ProjectITI.tripsproject.SignUp.SignupFragment;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class LoginSignup_Activity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        SignupFragment.OnFragmentInteractionListener {

    LoginFragment loginFragment;
    SignupFragment signupFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    boolean SignUp=false;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        bundle=savedInstanceState;

        if (savedInstanceState!=null)
            SignUp = bundle.getBoolean("BoolSignUp");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade();
            View decor = getWindow().getDecorView();
            fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);

            fade.excludeTarget(android.R.id.navigationBarBackground, true);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setEnterTransition(fade);
                getWindow().setExitTransition(fade);
            }
        }
        manager = getSupportFragmentManager();
        if (!SignUp)
        {

            if (manager.findFragmentByTag("fragment_login") == null)
            {
                loginFragment = new LoginFragment();
                transaction = manager.beginTransaction();
                transaction.add(R.id.FragmentLayout,loginFragment,"fragment_login");
                transaction.commit();
            }
            else
            {
                loginFragment = (LoginFragment) manager.findFragmentByTag("fragment_login");
            }
        }
        else
        {
            if (manager.findFragmentByTag("fragment_signup") == null)
            {
                signupFragment= new SignupFragment();

                transaction = manager.beginTransaction();
                transaction.add(R.id.FragmentLayout,signupFragment,"fragment_signup");
                transaction.commit();
            }
            else
            {
                signupFragment= (SignupFragment) manager.findFragmentByTag("fragment_signup");

            }
        }

    }

    @Override
    public void gotoSignup() {
        if (manager.findFragmentByTag("fragment_signup") == null)
        {
            signupFragment= new SignupFragment();

            transaction = manager.beginTransaction();
            transaction.replace(R.id.FragmentLayout,signupFragment,"fragment_signup");
            transaction.commit();
        }
        else
        {
            signupFragment= (SignupFragment) manager.findFragmentByTag("fragment_signup");
            transaction = manager.beginTransaction();
            transaction.replace(R.id.FragmentLayout,signupFragment,"fragment_signup");
            transaction.commit();
        }
        SignUp = true;
    }

    @Override
    public void gotoLogin() {
        if (manager.findFragmentByTag("fragment_login") == null)
        {
            loginFragment = new LoginFragment();

            transaction = manager.beginTransaction();
            transaction.replace(R.id.FragmentLayout,loginFragment,"fragment_login");
            transaction.commit();
        }
        else
        {
            loginFragment = (LoginFragment) manager.findFragmentByTag("fragment_login");

            transaction = manager.beginTransaction();
            transaction.replace(R.id.FragmentLayout,loginFragment,"fragment_login");
            transaction.commit();
        }
        SignUp = false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("BoolSignUp",SignUp);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}