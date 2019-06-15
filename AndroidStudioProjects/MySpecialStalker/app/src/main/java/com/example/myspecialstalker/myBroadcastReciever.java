package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class myBroadcastReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {




        String callingPhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        SharedPreferences sharedPreferences = context.getSharedPreferences("", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", null);
        String content = sharedPreferences.getString("text", null);
        Log.d("sending", "Sending an SMS!");
        Log.d("sending", phoneNumber);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(String.valueOf(phoneNumber),null,
                content,null,null);


    }
}
