package com.example.small_demo_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fishermanImage();
            }
        });

        textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), editText.class);
                intent.putExtra("numbers", textView.toString());
                startActivity(intent);
                textView.setText("you have entered: "+getIntent().getStringExtra("numbers"));
            }
        });


        //textView.setText("you have entered: "+getIntent().getStringExtra("EdiTtEXTvALUE"));




    }

    public void fishermanImage(){
        Uri uri = Uri.parse("https://cdn.pixabay.com/photo" +
                "/2018/02/08/15/40/fisherman-3139703_1280.jpg");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }



}
