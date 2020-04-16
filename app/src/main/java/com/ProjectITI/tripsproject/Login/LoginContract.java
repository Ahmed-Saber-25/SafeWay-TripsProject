package com.ProjectITI.tripsproject.Login;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginContract {
    interface PresenterInterface
    {
        void Login(String email, String pass);
        void firebaseAuthWithGoogle(GoogleSignInAccount acct);
        void handleFacebookAccessToken(AccessToken token);
    }

    interface ViewInterface
    {
        void LoginSucceed(String email, String pass, String userName);
    }
}
