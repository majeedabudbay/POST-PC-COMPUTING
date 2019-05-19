package com.example.myapplication_3;


import android.os.Parcel;
import android.os.Parcelable;


import java.sql.Timestamp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "message")
public class message implements Parcelable {

    @ColumnInfo(name = "message_content")
    private String msgContent;


    @ColumnInfo(name = "message_timestamp")
    private String timestamp;

    @PrimaryKey(autoGenerate = true)
    public int messageId;

    message(String msgContent, String timestamp) {
        this.msgContent = msgContent;
        this.timestamp = timestamp;
    }

    private message(Parcel in) {
        msgContent = in.readString();
    }

    int getmessageId(){
        return messageId;
    }

    String getmsgContent(){
        return msgContent;
    }

    String getTimestamp(){
        return timestamp;
    }

    public static final Creator<message> CREATOR = new Creator<message>() {
        @Override
        public message createFromParcel(Parcel in) {
            return new message(in);
        }

        @Override
        public message[] newArray(int size) {
            return new message[size];
        }
    };

    String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msgContent);
    }
}