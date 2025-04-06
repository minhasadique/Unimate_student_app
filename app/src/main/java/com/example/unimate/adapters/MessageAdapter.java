package com.example.unimate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unimate.R;
import com.example.unimate.models.Message;

import java.util.List;

public class MessageAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<Message> messageList;
    private String currentUserId; // ID of the logged-in user

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Two types: Sent & Received
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the message was sent by the current user
        Message message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            if (viewType == VIEW_TYPE_SENT) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            }

            holder.messageText = convertView.findViewById(R.id.textMessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the message object
        Message message = messageList.get(position);

        // Ensure message and text are not null before setting text
        if (message != null && message.getMessage() != null) {
            holder.messageText.setText(message.getMessage());
        } else {
            holder.messageText.setText(""); // Set an empty string instead of crashing
        }

        return convertView;
    }


    static class ViewHolder {
        TextView messageText;
    }
}

