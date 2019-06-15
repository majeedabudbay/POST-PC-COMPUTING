package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber;
    EditText text;
    TextView textView;

    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         phoneNumber = findViewById(R.id.phoneNumber);
         text = findViewById(R.id.Text);
         textView = findViewById(R.id.textView);






         content = text.getText().toString() + phoneNumber.getText().toString();
        textView.setText(content);

        SharedPreferences.Editor editor = getSharedPreferences("", MODE_PRIVATE).edit();
        editor.putString("phoneNumber", String.valueOf(phoneNumber));
        editor.putString("text", content);
        editor.apply();



    }


}
