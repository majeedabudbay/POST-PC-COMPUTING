package com.example.small_demo_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NavigationRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class editText extends AppCompatActivity {

    private EditText editText;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editext);
        //TextView textView = (TextView) findViewById(R.id.textView);
        editText = findViewById(R.id.numEdit);
        //editText.setText("");

        //Bundle bundle = getIntent().getExtras();
        //String numbers = bundle.getString("numbers");
        float size = editText.getTextSize();
        while (size < 5){
            if(size == 5){
                intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("numbers", editText.getText().toString());
                startActivity(intent);
            }
            size++;
        }


    }

}
