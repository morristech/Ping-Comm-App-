package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.yoscholar.ping.R;
import com.yoscholar.ping.retrofitPojo.login.Login;
import com.yoscholar.ping.utils.AppPreference;
import com.yoscholar.ping.utils.RetrofitApi;
import com.yoscholar.ping.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Email(message = "Email is not valid.")
    @NotEmpty(message = "Email cannot be empty.")
    private EditText emailEditText;
    @NotEmpty(message = "Password cannot be empty.")
    private EditText passwordEditText;
    private Button loginButton;

    private LinearLayout containerLinearLayout;
    private ProgressBar progressBar;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {

        containerLinearLayout = (LinearLayout) findViewById(R.id.container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        validator = new Validator(this);
        validator.setValidationListener(this);

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        emailEditText.setText("vicky7230@gmail.com");
        passwordEditText.setText("mac12345");

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

    }

    private void login() {


        Log.d(TAG, "Email : " + emailEditText.getText().toString());
        Log.d(TAG, "Password : " + passwordEditText.getText().toString());
        Log.d(TAG, "FCM token : " + FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "Device Id : " + Util.getDeviceId(this));


        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();

        Call<Login> loginCall = apiInterface.login(
                emailEditText.getText().toString(),
                passwordEditText.getText().toString(),
                Util.getDeviceId(this),
                FirebaseInstanceId.getInstance().getToken()
        );

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.isSuccessful()) {

                    saveDetails(response.body());

                } else {

                    Toast.makeText(LoginActivity.this, "Some error.", Toast.LENGTH_SHORT).show();
                    containerLinearLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Network error.", Toast.LENGTH_SHORT).show();
                containerLinearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void saveDetails(Login login) {

        AppPreference.saveBoolean(LoginActivity.this, AppPreference.IS_LOGGED_IN, true);
        AppPreference.saveString(LoginActivity.this, AppPreference.EMAIL, login.getUser().getEmail());
        AppPreference.saveString(LoginActivity.this, AppPreference.USER_NAME, login.getUser().getUserName());
        AppPreference.saveString(LoginActivity.this, AppPreference.USER_ID, login.getUser().getUserId());
        AppPreference.saveString(LoginActivity.this, AppPreference.TOKEN, login.getUser().getToken());

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        finish();
    }

    @Override
    public void onValidationSucceeded() {

        Toast.makeText(this, "makes sense.", Toast.LENGTH_SHORT).show();
        containerLinearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        login();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
