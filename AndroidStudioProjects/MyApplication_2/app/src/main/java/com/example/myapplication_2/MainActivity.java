package com.example.myapplication_2;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<message> msgList = new ArrayList<message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(savedInstanceState != null){
            msgList = savedInstanceState.getParcelableArrayList("key");

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView msgRecyclerView = (RecyclerView)findViewById(R.id.output);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        final myAdapter chatMsgAdapter = new myAdapter(msgList);
        msgRecyclerView.setAdapter(chatMsgAdapter);
        final EditText InputText = (EditText)findViewById(R.id.inputTxt);
        Button SendButton = (Button)findViewById(R.id.button);
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgContent = InputText.getText().toString();
                if(!TextUtils.isEmpty(msgContent))
                {
                    message msg = new message(msgContent);
                    msgList.add(msg);
                    int newMsgPosition = msgList.size() - 1;
                    chatMsgAdapter.notifyItemInserted(newMsgPosition);
                    msgRecyclerView.scrollToPosition(newMsgPosition);
                    InputText.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "you can't send an empty message," +
                            " oh silly!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) msgList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}