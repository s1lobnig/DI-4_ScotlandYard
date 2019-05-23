package com.example.scotlandyard.messenger;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.MessengerInterface;
import com.example.scotlandyard.control.Server;

import java.util.ArrayList;

public class Messenger extends AppCompatActivity implements MessengerInterface {


    private EditText textMessage;
    private ListView messageListView;

    private MessageAdapter mMessageAdapter;
    private Message message;
    private String logTag = "Messanger";
    private String nickname;
    private ArrayList<Message> messageArrayList = Device.getInstance().getMessageList();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);

        /* add Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Spieler Chat");
        setSupportActionBar(toolbar);

        /*get data from Intent*/
        Intent intent = getIntent();
        nickname = intent.getStringExtra("USERNAME");

        /* find views */
        textMessage = findViewById(R.id.edittext_chatbox);
        Button btnSend = findViewById(R.id.button_chatbox_send);
        messageListView = findViewById(R.id.message_list);

        /*set the Adapter to listView*/
        mMessageAdapter = new MessageAdapter(this);


        if(messageArrayList.size() != 0) {
            for(Message message : messageArrayList){
                if(message.getNickname().equals(this.nickname)){
                    message.setBelongsToCurrentUser(true);
                    mMessageAdapter.add(message);
                }else{
                    message.setBelongsToCurrentUser(false);
                    mMessageAdapter.add(message);
                }

            }
        }

        messageListView.setAdapter(mMessageAdapter);

        /*add messenger observer*/
        Device.getInstance().addMessengerObserver(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = new Message(textMessage.getText().toString(), nickname);
                /*set message belong to current user*/
                message.setBelongsToCurrentUser(true);

                Device.getInstance().send(message);
                mMessageAdapter.add(message);

                /*flush EditText and close keyboard after sending*/
                textMessage.setText("");
                textMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);

                /*scroll the ListView to the last added element*/
                messageListView.setSelection(messageListView.getCount() - 1);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void updateMessages(ArrayList<Message> messages) {
        Log.d(logTag, "Chat message received!");
        /*check if message belongs to current user*/
        message = messages.get(messages.size()-1);
        if(!(message.getNickname().equals(this.nickname))) {
            message.setBelongsToCurrentUser(false);
        }

        /*display message*/
        mMessageAdapter.add(message);

        /*scroll the ListView to the last added element*/
        messageListView.setSelection(messageListView.getCount() - 1);

    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        //TODO
        Log.d(logTag, "should not be called");
    }

    @Override
    public void showSendingFailed(Object object) {
        //TODO
        Log.d(logTag, "sending has failed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Device.getInstance().removeMessengerObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Device.getInstance().removeMessengerObserver();
    }

}
