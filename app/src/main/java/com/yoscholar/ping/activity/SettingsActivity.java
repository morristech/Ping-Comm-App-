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
import com.yoscholar.ping.BuildConfig;
import com.yoscholar.ping.R;
import com.yoscholar.ping.utils.AppPreference;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView versionInfoTextView;
    private IconTextView signOutTextView;

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

        versionInfoTextView = (TextView) findViewById(R.id.version_info);
        versionInfoTextView.setText("version : " + BuildConfig.VERSION_NAME);

        signOutTextView = (IconTextView) findViewById(R.id.sign_out);
        signOutTextView.setOnClickListener(new View.OnClickListener() {
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
