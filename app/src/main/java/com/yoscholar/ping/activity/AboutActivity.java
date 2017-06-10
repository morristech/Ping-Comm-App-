package com.yoscholar.ping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yoscholar.ping.BuildConfig;
import com.yoscholar.ping.R;

public class AboutActivity extends AppCompatActivity {

    private TextView versionInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
    }

    private void init() {

        versionInfoTextView = (TextView) findViewById(R.id.version_info);
        versionInfoTextView.setText("version : " + BuildConfig.VERSION_NAME);

    }
}
