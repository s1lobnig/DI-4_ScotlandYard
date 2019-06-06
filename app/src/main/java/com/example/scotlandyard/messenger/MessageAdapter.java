package com.example.scotlandyard.messenger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.scotlandyard.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    List<Message> messages = new ArrayList<>();
    Context context;
    String nickname;

    public MessageAdapter(Context context, String nickname) {
        this.context = context;
        this.nickname = nickname;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        View myView = convertView;
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        // this message was sent by us so let's create a basic chat bubble on the right
        if (message.getNickname().equals(nickname)) {
            myView = messageInflater.inflate(R.layout.sent_message, null);
            holder.messageBody = myView.findViewById(R.id.message_body);
            myView.setTag(holder);
            holder.messageBody.setText(message.getMessage());

            // this message was sent by someone else so let's create an advanced chat bubble on the left
        } else {
            myView = messageInflater.inflate(R.layout.received_message, null);
            holder.name = myView.findViewById(R.id.name);
            holder.messageBody = myView.findViewById(R.id.message_body);
            myView.setTag(holder);
            holder.messageBody.setText(message.getMessage());
            holder.name.setText(message.getNickname());
        }

        return myView;
    }

}

