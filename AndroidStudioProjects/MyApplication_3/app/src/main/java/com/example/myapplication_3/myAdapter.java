package com.example.myapplication_3;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapterViewHolder> {

    public messageLongClickCallBack messageLongClickCallBack;

    private List<message> msgList;

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
        final myAdapterViewHolder myAdapterViewHolder = new myAdapterViewHolder(view);
        myAdapterViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                message message = msgList.get(myAdapterViewHolder.getAdapterPosition());
                if(messageLongClickCallBack != null){
                    messageLongClickCallBack.onLongMessageClick(message);
                    return true;
                }
                return false;
            }
        });
        return myAdapterViewHolder;
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