package com.example.myapplication_2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


class myAdapterViewHolder extends RecyclerView.ViewHolder {

    LinearLayout MsgLayout;

    TextView MsgTextView;

    myAdapterViewHolder(View itemView) {
        super(itemView);

        MsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_layout);
        MsgTextView = (TextView) itemView.findViewById(R.id.chat_msg_text_view);
    }
}