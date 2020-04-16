package com.ProjectITI.tripsproject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ProjectITI.tripsproject.AddTrip.AddTrip;
import com.ProjectITI.tripsproject.Alert.Trip_alert;
import com.ProjectITI.tripsproject.drawOnMap.MapFrag;
import com.ProjectITI.tripsproject.ui.Trips_History.HistoryFragment;
import com.ProjectITI.tripsproject.ui.Upcoming_Trips.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeScreen extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public static Context context;
    public static Application application;
    private NavigationView navigationView;
    String user;
    AlertDialog alertDialog;
    private AppBarConfiguration mAppBarConfiguration;
    ActionBarDrawerToggle toggle;

    FirebaseAuth mAuth;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        application = getApplication();
        context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddTripActivity();
            }
        });
         drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setNavigationInformation();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        HomeFragment home=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace( R.id.nav_host_fragment,home).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment selectedFregment=null;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_logout:
                        logout();
                        Toast.makeText(getApplicationContext(),"Log Out",Toast.LENGTH_LONG).show();
                        break;
                    case  R.id.nav_upcomping_trips:
                        selectedFregment=new HomeFragment();
                       // getSupportFragmentManager().beginTransaction().replace( R.id.nav_host_fragment,selectedFregment).commit();
                        transaction.replace( R.id.nav_host_fragment, selectedFregment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.nav_History:
                        selectedFregment=new HistoryFragment() ;
                        transaction.replace( R.id.nav_host_fragment, selectedFregment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.drawOnMap:
                        selectedFregment=new MapFrag() ;
                        transaction.replace( R.id.nav_host_fragment, selectedFregment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        /*
                        TripDao tripDao = new TripDao();
                        ArrayList<String> StartPoints = tripDao.getStartPoints();
                        Log.i("tag" , "StartPoints : "+StartPoints.size());
                        ArrayList<String> EndPoints = tripDao.getEndPoints();
                        Log.i("tag" , "EndPoints : "+EndPoints.size());
                        Intent intent = new Intent(context, MapsActivity.class);
                        startActivity(intent);
                        break;*/
                }
                drawer.closeDrawers();
                return true;

            }
        });


    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), Trip_alert.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("action", "logout");
        getApplicationContext().startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void goToAddTripActivity() {
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent i = new Intent(this, AddTrip.class);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
    }

    private void setNavigationInformation() {
        View hView = navigationView.getHeaderView(0);

        SharedPreferences sh = getSharedPreferences("USER", 0);
        String user_name = sh.getString("name", "");

        TextView username = (TextView) hView.findViewById(R.id.username_id);
        username.setText(user_name);

        TextView email = (TextView) hView.findViewById(R.id.email_id);
        email.setText(mAuth.getCurrentUser().getEmail());
    }





}
