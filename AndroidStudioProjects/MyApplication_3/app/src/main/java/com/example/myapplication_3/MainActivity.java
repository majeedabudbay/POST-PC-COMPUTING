package com.example.myapplication_3;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements messageLongClickCallBack {



    List<message> msgList = new ArrayList<message>();

    myAdapter chatMsgAdapter = null;


    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        SelfChatApp selfChatApp = (SelfChatApp) getApplication();
        final messageDao dao = selfChatApp.messageDao;




        final dbWorker worker = selfChatApp.dbWorker;
        worker.getAll(new dbWorker.GetAllCallback() {
            @Override
            public void resultsReady(final List<message> result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msgList = result;
                        chatMsgAdapter = new myAdapter(msgList);

                    }
                });
            }
        });

        msgList = dao.getAll();





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView msgRecyclerView = (RecyclerView)findViewById(R.id.output);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        chatMsgAdapter = new myAdapter(msgList);
        chatMsgAdapter.messageLongClickCallBack = this;
        msgRecyclerView.setAdapter(chatMsgAdapter);
        final EditText InputText = (EditText)findViewById(R.id.inputTxt);
        Button SendButton = (Button)findViewById(R.id.button);
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgContent = InputText.getText().toString();
                if(!TextUtils.isEmpty(msgContent))
                {
                    message msg = new message(msgContent, getCurrentTimeStamp());
                    msgList.add(msg);
                    dao.insertAll(msg);
                    worker.insertAll(msg);
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
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) msgList);
        super.onSaveInstanceState(outState);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        msgList =  savedInstanceState.getParcelableArrayList("key");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onLongMessageClick(final message message) {
        SelfChatApp selfChatApp = (SelfChatApp) getApplication();
        final messageDao dao = selfChatApp.messageDao;



        AlertDialog.Builder deleteMessage = new AlertDialog.Builder(this);
        deleteMessage.setMessage("Are You Sure you want to delete?");
        deleteMessage.setCancelable(true);

        deleteMessage.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        dao.delete(message);
                        msgList.remove(message);
                        chatMsgAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });

        deleteMessage.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog deleteAlert = deleteMessage.create();
        deleteAlert.show();


    }
}