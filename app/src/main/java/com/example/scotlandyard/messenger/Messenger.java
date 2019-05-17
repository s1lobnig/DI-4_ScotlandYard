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
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.MessengerInterface;
import com.example.scotlandyard.control.Server;
import com.example.scotlandyard.lobby.Game;
import com.example.scotlandyard.map.motions.Move;
import com.example.scotlandyard.map.roadmap.Entry;
import com.google.android.gms.nearby.Nearby;

import java.util.ArrayList;
import java.util.Map;

public class Messenger extends AppCompatActivity implements MessengerInterface {


    private EditText textMessage;
    private ListView messageList;
    private MessageAdapter mMessageAdapter;
    private Message message;
    private ArrayList<Message> messages;
    private String logTag = "Messenger";
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

        /*get data from Intent*/
        Intent intent = getIntent();
        nickname = intent.getStringExtra("USERNAME");

        /*add messenger observer*/
        Device.getInstance().addMessengerObserver(this);

        /*set the Adapter to listView*/
        mMessageAdapter = new MessageAdapter(this);
        messageList.setAdapter(mMessageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = new Message(textMessage.getText().toString(), nickname);
                /*set message belong to current user*/
                message.setBelongsToCurrentUser(true);

                //Device.send(message);
                updateMessages(messages);

                /*flush EditText and close keyboard after sending*/
                textMessage.setText("");
                textMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void updateMessages(ArrayList<Message> messages) {
        //TODO
        Log.d(logTag, "Chat message received!");

        messages.add(message);

        /*check if message belongs to current user*/
        if(!(message.getNickname().equals(this.nickname))) {
            message.setBelongsToCurrentUser(false);
        }

        /*display message*/
        mMessageAdapter.add(message);

        /*scroll the ListView to the last added element*/
        messageList.setSelection(messageList.getCount() - 1);

    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        //TODO
        Log.d(logTag, "should not be called");
    }

    @Override
    public void showSendingFailed(Object object) {
        //TODO
        Log.d(logTag, "sending had failed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Server) Device.getInstance()).removeMessengerObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((Server) Device.getInstance()).removeMessengerObserver();
    }


}
