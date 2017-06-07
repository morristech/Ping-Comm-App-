package com.yoscholar.ping.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.yoscholar.ping.pojo.Otp;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by agrim on 7/6/17.
 */

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                Object[] pdu_Objects = (Object[]) bundle.get("pdus");

                if (pdu_Objects != null) {

                    for (Object aObject : pdu_Objects) {

                        SmsMessage currentSMS = getIncomingMessage(aObject, bundle);

                        String senderNo = currentSMS.getDisplayOriginatingAddress();

                        String message = currentSMS.getDisplayMessageBody();

                        //Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + message, Toast.LENGTH_LONG).show();

                        if (senderNo.equals("AM-YOSCLR"))
                            if (message != null) {

                                Pattern pattern = Pattern.compile("(|^)\\d{4}");
                                Matcher matcher = pattern.matcher(message);

                                if (matcher.find())
                                    EventBus.getDefault().post(new Otp(matcher.group(0)));
                            }
                    }
                }
            }
        }
    }


    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {

        SmsMessage currentSMS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);

        } else {

            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);

        }

        return currentSMS;
    }
}
