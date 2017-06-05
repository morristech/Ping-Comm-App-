package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yoscholar.ping.R;
import com.yoscholar.ping.retrofitPojo.refreshToken.RefrehToken;
import com.yoscholar.ping.utils.AppPreference;
import com.yoscholar.ping.utils.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000;
    private LinearLayout errorLayout;
    private ProgressBar progressBar;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        launch();
    }

    private void init() {

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        retryButton = (Button) findViewById(R.id.retry_button);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                refreshTokenAndGoToHome();
            }
        });

    }

    private void launch() {


        if (AppPreference.getBoolean(SplashActivity.this, AppPreference.IS_LOGGED_IN))
            refreshTokenAndGoToHome();
        else
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);

    }

    private void refreshTokenAndGoToHome() {


        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();

        Call<RefrehToken> refreshTokenCall = apiInterface.refreshToken(
                AppPreference.getString(SplashActivity.this, AppPreference.EMAIL),
                AppPreference.getString(SplashActivity.this, AppPreference.USER_ID)
        );

        refreshTokenCall.enqueue(new Callback<RefrehToken>() {
            @Override
            public void onResponse(Call<RefrehToken> call, Response<RefrehToken> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        AppPreference.saveString(SplashActivity.this, AppPreference.TOKEN, response.body().getToken());

                        Toast.makeText(SplashActivity.this, "Token refreshed.", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                        finish();

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {

                        AppPreference.logOut(SplashActivity.this);

                        Toast.makeText(SplashActivity.this, "Your Account no longer exists.", Toast.LENGTH_SHORT).show();

                        openLoginScreen();

                        finish();


                    }

                } else {

                    Toast.makeText(SplashActivity.this, "Some error.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RefrehToken> call, Throwable t) {

                Toast.makeText(SplashActivity.this, "Check your network connection and retry.", Toast.LENGTH_SHORT).show();

                errorLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
