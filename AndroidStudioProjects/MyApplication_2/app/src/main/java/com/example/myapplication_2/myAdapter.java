package com.example.myapplication_2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapterViewHolder> {

    private List<message> msgList = null;

    myAdapter(List<message> msgList) {
        this.msgList = msgList;
    }

    @Override
    public void onBindViewHolder(myAdapterViewHolder holder, int position) {
        message msg = this.msgList.get(position);
        holder.MsgLayout.setVisibility(LinearLayout.VISIBLE);
        holder.MsgTextView.setText(msg.getMsgContent());
    }

    @NonNull
    @Override
    public myAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message, parent, false);
        return new myAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(msgList==null)
        {
            msgList = new ArrayList<message>();
        }
        return msgList.size();
    }
}