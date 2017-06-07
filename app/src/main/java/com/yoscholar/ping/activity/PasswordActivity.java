package com.yoscholar.ping.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.yoscholar.ping.R;
import com.yoscholar.ping.pojo.Otp;
import com.yoscholar.ping.receiver.SmsReceiver;
import com.yoscholar.ping.retrofitPojo.passwordSet.PasswordSet;
import com.yoscholar.ping.retrofitPojo.tokenDetails.TokenDetails;
import com.yoscholar.ping.utils.AppPreference;
import com.yoscholar.ping.utils.RetrofitApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {

    private SmsReceiver smsReceiver;
    private IntentFilter intentFilter;

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 200;
    private Toolbar toolbar;
    private Validator validator;

    private TokenDetails tokenDetails;

    private View layer;
    private LinearLayout generateOtpContainer;
    private LinearLayout otpContainer;
    private LinearLayout newPasswordContainer;

    private EditText mobileNumberEditText;
    private Button generateOtpButton;

    private EditText otpEditText;
    private Button verifyOtpButton;

    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC, message = "Password should be at least 6 characters long alphanumeric string.")
    private EditText passwordEditText;
    @ConfirmPassword
    private EditText confirmPasswordEditText;
    private Button setPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        if (ContextCompat.checkSelfPermission(PasswordActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(PasswordActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

        }

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

        smsReceiver = new SmsReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

        unregisterReceiver(smsReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted


                } else {

                    // permission denied
                    Toast.makeText(this, "Permission Denied, cannot continue.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(SettingsActivity.OPERATION));

        layer = findViewById(R.id.layer);
        generateOtpContainer = (LinearLayout) findViewById(R.id.generate_otp_container);
        otpContainer = (LinearLayout) findViewById(R.id.otp_container);
        newPasswordContainer = (LinearLayout) findViewById(R.id.new_password_container);


        mobileNumberEditText = (EditText) findViewById(R.id.mobile_number);
        generateOtpButton = (Button) findViewById(R.id.generate_otp_button);
        generateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobileNumberEditText.getText().toString().length() != 10) {
                    mobileNumberEditText.setError("Mobile number should contain a valid 10 digit phone number");
                } else {
                    generateOTP();
                }

            }
        });

        otpEditText = (EditText) findViewById(R.id.otp);
        verifyOtpButton = (Button) findViewById(R.id.verify_otp_button);
        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tokenDetails.getOtp() == Integer.parseInt(otpEditText.getText().toString())) {
                    Toast.makeText(PasswordActivity.this, "OTP verified.", Toast.LENGTH_SHORT).show();
                    otpContainer.setVisibility(View.GONE);
                    newPasswordContainer.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(PasswordActivity.this, "Invalid OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

                changePassword();

            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(PasswordActivity.this);

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(PasswordActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        passwordEditText = (EditText) findViewById(R.id.password);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirm_password);
        setPasswordButton = (Button) findViewById(R.id.set_password_button);
        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validate();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void generateOTP() {

        layer.setVisibility(View.VISIBLE);

        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();
        Call<TokenDetails> tokenDetailsCall = apiInterface.generateOTP(
                mobileNumberEditText.getText().toString()
        );

        tokenDetailsCall.enqueue(new Callback<TokenDetails>() {
            @Override
            public void onResponse(Call<TokenDetails> call, Response<TokenDetails> response) {

                layer.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        tokenDetails = response.body();

                        Toast.makeText(PasswordActivity.this, tokenDetails.getMessage(), Toast.LENGTH_SHORT).show();

                        generateOtpContainer.setVisibility(View.GONE);
                        otpContainer.setVisibility(View.VISIBLE);

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {

                        Toast.makeText(PasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(PasswordActivity.this, "Some error.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<TokenDetails> call, Throwable t) {

                layer.setVisibility(View.GONE);

                Toast.makeText(PasswordActivity.this, "Network error.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void changePassword() {

        layer.setVisibility(View.VISIBLE);

        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();

        Call<PasswordSet> passwordSetCall = apiInterface.setPassword(
                tokenDetails.getTokenId(),
                confirmPasswordEditText.getText().toString()
        );

        passwordSetCall.enqueue(new Callback<PasswordSet>() {
            @Override
            public void onResponse(Call<PasswordSet> call, Response<PasswordSet> response) {

                layer.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        Toast.makeText(PasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        if (AppPreference.getBoolean(PasswordActivity.this, AppPreference.IS_LOGGED_IN)) {

                            AppPreference.logOut(PasswordActivity.this);

                            Toast.makeText(PasswordActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();

                            openLoginScreen();

                            finish();

                        } else {

                            finish();
                        }

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {

                        Toast.makeText(PasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(PasswordActivity.this, "Some error.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<PasswordSet> call, Throwable t) {

                layer.setVisibility(View.GONE);

                Toast.makeText(PasswordActivity.this, "Network error.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    /**
     * Called from {@link SmsReceiver#onReceive(Context, Intent)}
     * @param otp otp
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Otp otp) {

        otpEditText.setText(otp.getOtp());

    }
}
