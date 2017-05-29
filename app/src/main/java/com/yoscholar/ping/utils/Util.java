package com.yoscholar.ping.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by agrim on 16/11/16.
 */

public class Util {

    public static String getDeviceId(Context context) {

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static void openYoScholarSite(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yoscholar.com/"));
        context.startActivity(intent);
    }
}
