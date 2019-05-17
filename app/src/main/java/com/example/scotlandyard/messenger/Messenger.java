package com.example.scotlandyard.messenger;

import android.content.Intent;
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
import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
import com.example.scotlandyard.control.MessengerInterface;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;

import java.util.ArrayList;
import java.util.Map;

public class Messenger extends AppCompatActivity implements MessengerInterface {


    private EditText textMessage;
    private ListView messageList;

    private MessageAdapter mMessageAdapter;
    private boolean isServer;
    private String logTag;
    private String nickname;

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


        /* find views */

        textMessage = findViewById(R.id.edittext_chatbox);
        Button btnSend = findViewById(R.id.button_chatbox_send);
        messageList = findViewById(R.id.message_list);

        /*get data from Intent to distinguish between host and client*/
        Intent intent = getIntent();
        isServer = intent.getBooleanExtra("IS_SERVER", false);
        nickname = intent.getStringExtra("USERNAME");

        /*set the Adapter to listView*/
        mMessageAdapter = new MessageAdapter(this);
        messageList.setAdapter(mMessageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textM = textMessage.getText().toString();

                /*set message belong to current user*/
                Message message = new Message(textM, nickname);
                message.setBelongsToCurrentUser(true);

                /*add message to the listView*/
                mMessageAdapter.add(message);

                /*test to check if message comes from server or not*/
                if (isServer) {
                    /*send message to all players*/
                    onMessage(message);

                } else {
                    Log.d(logTag, "Client is sending chat message to server");
                    //clientService.send(message);
                }

                /*scroll the ListView to the last added element*/
                messageList.setSelection(messageList.getCount() - 1);

                /*flush EditText and close keyboard after sending*/
                textMessage.setText("");
                textMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //TODO add this to new interface function
    public void onMessage(Message message) {
        Log.d(logTag, "Chat message received!");

        String textOfMessage = ((Message) message).getMessage();
        Message receivedMessage = new Message(textOfMessage,((Message) message).getNickname());

        if (isServer) {
            Log.d(logTag, "Server is sending chat message to clients");

            /*show received message for all users*/
            //serverService.send(receivedMessage);

        }

        /*check if message belongs to current user*/
        if(!(receivedMessage.getNickname().equals(this.nickname))) {
            receivedMessage.setBelongsToCurrentUser(false);

            mMessageAdapter.add(receivedMessage);
            /*scroll the ListView to the last added element*/
            messageList.setSelection(messageList.getCount() - 1);
        }
    }

    @Override
    public void updateMessages(ArrayList<Message> messages) {
        //TODO
    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        //TODO
    }

    @Override
    public void showSendingFailed(Object object) {
        //TODO
    }
}
