package com.ProjectITI.tripsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appolica.flubber.Flubber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    RelativeLayout splashRelativeLayout;
    ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        logoImageView = findViewById(R.id.SplashLogo);
        splashRelativeLayout = findViewById(R.id.SplashLayout);

        Animator animator = Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_UP)
                .repeatCount(0)
                .duration(1500)
                .createFor(logoImageView);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), LoginSignup_Activity.class);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this
                                ,Pair.create((View)splashRelativeLayout,splashRelativeLayout.getTransitionName())
                                ,Pair.create((View)logoImageView,logoImageView.getTransitionName()));

                        startActivity(intent,optionsCompat.toBundle());
                        finish();
                    }
                    else
                    {
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animator.start();
    }

}
