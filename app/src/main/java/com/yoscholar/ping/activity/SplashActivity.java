package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoscholar.ping.R;
import com.yoscholar.ping.utils.AppPreference;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        launch();
    }

    private void launch() {

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to showToast case your app logo / company
             */

            @Override
            public void run() {

                if (AppPreference.getBoolean(SplashActivity.this, AppPreference.IS_LOGGED_IN))
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
