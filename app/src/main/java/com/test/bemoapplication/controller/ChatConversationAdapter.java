package com.test.bemoapplication.controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.bemoapplication.R;

import com.test.bemoapplication.model.chat.ChatConversation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pardypanda05 on 1/8/17.
 */

public class ChatConversationAdapter extends RecyclerView.Adapter<ChatConversationAdapter.MyViewHolder> {

    List<ChatConversation> arrayListChatMessageList = new ArrayList<>();
    Activity activity;
    View view;

    public ChatConversationAdapter(Activity activity, List<ChatConversation> arrayListChatMessageList) {
        this.arrayListChatMessageList = arrayListChatMessageList;
        this.activity = activity;
    }

    @Override
    public ChatConversationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);

        return new ChatConversationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatConversationAdapter.MyViewHolder holder, final int position) {
        //holder.textViewStoreName.setText(arrayListSurvey.get(position).getName());


        holder.textViewMessage.setText(arrayListChatMessageList.get(position).getChatMessage());/*
        Picasso.with(activity)
                .load(APIHandler.restAPI.imageUrl+arrayListChatMessageList.get(position).getBackdropPath()).placeholder(R.drawable.im_place_holder)
                .into(holder.imageViewDelete);

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailsActivity.class);
                intent.putExtra("movieID", arrayListChatMessageList.get(position).getId());
                activity.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arrayListChatMessageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage;
        ImageView imageViewDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewMessage = (TextView) itemView.findViewById(R.id.text_message);

        }
    }

    public void updateList(List<ChatConversation> movieList) {
        arrayListChatMessageList = movieList;
        notifyDataSetChanged();
    }
}
