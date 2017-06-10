package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.yoscholar.ping.R;
import com.yoscholar.ping.utils.AppPreference;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class SettingsActivity extends AppCompatActivity {

    public static final String OPERATION = "operation";
    private Toolbar toolbar;
    private IconTextView signOut;
    private IconTextView changePassword;
    private IconTextView openSourceLicenses;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userNameTextView = (TextView) findViewById(R.id.user_name);
        userNameTextView.setText(AppPreference.getString(SettingsActivity.this, AppPreference.USER_NAME));

        openSourceLicenses = (IconTextView) findViewById(R.id.open_source_licenses);
        openSourceLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Notices notices = new Notices();
                notices.addNotice(new Notice("ragunathjawahar/android-saripaar", "https://github.com/ragunathjawahar/android-saripaar", "Copyright 2012 - 2015 Mobs & Geeks", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("square/okhttp", "https://github.com/square/okhttp", "Copyright 2016 Square, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("square/retrofit", "https://github.com/square/retrofit", "Copyright 2013 Square, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("google/gson", "https://github.com/google/gson", "Copyright 2008 Google Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("JoanZapata/android-iconify", "https://github.com/JoanZapata/android-iconify", "Copyright 2015 Joan Zapata", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("greenrobot/EventBus", "https://github.com/greenrobot/EventBus", "Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("amulyakhare/TextDrawable", "https://github.com/amulyakhare/TextDrawable", "Copyright (c) 2014 Amulya Khare", new MITLicense()));

                new LicensesDialog.Builder(SettingsActivity.this)
                        .setTitle("Libraries")
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();

            }
        });

        changePassword = (IconTextView) findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, PasswordActivity.class);
                intent.putExtra(OPERATION, "Change Password");
                startActivity(intent);

            }
        });

        signOut = (IconTextView) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppPreference.logOut(SettingsActivity.this);

                Toast.makeText(SettingsActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();

                openLoginScreen();

                finish();
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

    private void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
