package com.yoscholar.ping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.yoscholar.ping.retrofitPojo.tokenDetails.TokenDetails;
import com.yoscholar.ping.utils.RetrofitApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Validator validator;
    private Validator validator2;

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

        init();

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

        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

                Toast.makeText(PasswordActivity.this, "Validation Successful.", Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(PasswordActivity.this, "OTP generated successfully, wait for OTP message.", Toast.LENGTH_SHORT).show();

                        generateOtpContainer.setVisibility(View.GONE);
                        otpContainer.setVisibility(View.VISIBLE);

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {

                        Toast.makeText(PasswordActivity.this, "This mobile number does not exist in the Database.", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(PasswordActivity.this, "Some error.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<TokenDetails> call, Throwable t) {

                layer.setVisibility(View.GONE);

                Toast.makeText(PasswordActivity.this, "Network error.", Toast.LENGTH_SHORT).show();

                Log.d("DIG","Error : "+ t.getMessage());

            }
        });

    }


}
