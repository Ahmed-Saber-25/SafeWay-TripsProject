package com.ProjectITI.tripsproject.Login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.ProjectITI.tripsproject.NetworkConnection.CheckNetworkConnection;
import com.ProjectITI.tripsproject.NetworkConnection.ConnectionDetector;
import com.ProjectITI.tripsproject.R;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.ProjectITI.tripsproject.HomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

public class LoginPresenter implements LoginContract.PresenterInterface {

    LoginContract.ViewInterface viewInterface;
    Activity activity;
    public FirebaseAuth mAuth;
    public DatabaseReference databaseReference;
    public String userId;

    Dialog loadingDialog;

    public LoginPresenter(LoginContract.ViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        loadingDialog = new Dialog(activity);
    }

    @Override
    public void Login(final String email, final String pass) {
        //we are connected to a network
        CheckNetworkConnection checkNetworkConnection = new CheckNetworkConnection();
        ConnectionDetector connectionDetector = new ConnectionDetector(activity);

        if (checkNetworkConnection.hasInternetConnection(activity)) {
            //we are connected to a network

            //Access internet or not
            if (connectionDetector.hasInternetConnection()) {
                MyCustomDialog();
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("users").child(mAuth.getUid()).child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        userId = mAuth.getUid();
                                        Log.v("tag", mAuth.getUid());
                                        viewInterface.LoginSucceed(email, pass, dataSnapshot.getValue(String.class));
                                    }
                                    loadingDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    loadingDialog.dismiss();
                                }
                            });
                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {

                Toast.makeText(activity, "you should connect to the internet", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(activity, "you should connect to the internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GoogleAccount", "firebaseAuthWithGoogle:" + acct.getId());
        MyCustomDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loooooooooooooooooogin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            databaseReference.child("users").child(user.getUid()).child("Email").setValue(user.getEmail());
                            databaseReference.child("users").child(user.getUid()).child("Password").setValue("Gmail");
                            databaseReference.child("users").child(user.getUid()).child("UserName").setValue(user.getDisplayName());
                            databaseReference.child("users").child(user.getUid()).child("TripsCount").setValue(0);

                            viewInterface.LoginSucceed(user.getEmail(), "", user.getDisplayName());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Looooooooooogin", "signInWithCredential:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookAccessToken: ", token.toString());
        MyCustomDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loooooooooooooooooogin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            databaseReference.child("users").child(user.getUid()).child("Email").setValue(user.getEmail());
                            databaseReference.child("users").child(user.getUid()).child("Password").setValue("Facebook");
                            databaseReference.child("users").child(user.getUid()).child("UserName").setValue(user.getDisplayName());
                            databaseReference.child("users").child(user.getUid()).child("TripsCount").setValue(0);

                            viewInterface.LoginSucceed(user.getEmail(), "", user.getDisplayName());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("faaaaaaaaaiiiiiiled", "signInWithCredential:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    public void MyCustomDialog() {
        loadingDialog = new Dialog(activity);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading_dialog);
        Window window = loadingDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);

        TextView progressMsg = loadingDialog.findViewById(R.id.progress_message);
        progressMsg.setText("Login ...");

        RotateLoading rotateLoading;
        rotateLoading = loadingDialog.findViewById(R.id.rotateloading);
        rotateLoading.start();
        loadingDialog.show();
    }
}
