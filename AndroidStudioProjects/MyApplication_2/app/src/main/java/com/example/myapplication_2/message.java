package com.example.myapplication_2;

import android.os.Parcel;
import android.os.Parcelable;

public class message implements Parcelable {

    private String msgContent;
    message(String msgContent) {
        this.msgContent = msgContent;
    }

    private message(Parcel in) {
        msgContent = in.readString();
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