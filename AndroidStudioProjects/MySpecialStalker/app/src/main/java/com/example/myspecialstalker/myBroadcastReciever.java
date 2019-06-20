package com.example.myspecialstalker;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("My notification")
                        .setContentText("sending message...");

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());

        Log.d("sending", phoneNumber);


        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(String.valueOf(phoneNumber),null,
                content,null,null);

        NotificationCompat.Builder mBuilder1 =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("My notification")
                        .setContentText("message sent successfully!");

        NotificationManager notificationManager1 =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder1.build());


        NotificationCompat.Builder mBuilder2 =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("My notification")
                        .setContentText("message received successfully!");

        NotificationManager notificationManager2 =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder2.build());


    }
}
