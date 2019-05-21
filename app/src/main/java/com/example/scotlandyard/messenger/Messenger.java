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
    private static final String MYLISTKEY = "myMessageList";
    Parcelable myListInstanceState;

    private String logTag = "Messanger";
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

        if(savedInstanceState!=null) {
            myListInstanceState = savedInstanceState.getParcelable(MYLISTKEY);
        }

        /* add Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Spieler Chat");
        setSupportActionBar(toolbar);

        /* find views */
        textMessage = findViewById(R.id.edittext_chatbox);
        Button btnSend = findViewById(R.id.button_chatbox_send);
        messageListView = findViewById(R.id.message_list);

        /*get data from Intent*/
        Intent intent = getIntent();
        nickname = intent.getStringExtra("USERNAME");

        /*add messenger observer*/
        Device.getInstance().addMessengerObserver(this);

        /*set the Adapter to listView*/
        mMessageAdapter = new MessageAdapter(this);
        messageListView.setAdapter(mMessageAdapter);

        /*restore old messages if they exist*/
       // messageListView.onRestoreInstanceState(myListInstanceState);

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
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void updateMessages(ArrayList<Message> messages) {
        //TODO
        Log.d(logTag, "Chat message received!");
        /*check if message belongs to current user*/
        message = messages.get(messages.size()-1);
        if(!(message.getNickname().equals(this.nickname))) {
            message.setBelongsToCurrentUser(false);
        }
        messages.add(message);

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


    /*methods to restore message list*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);

        // Save the user's current game state
        savedInstanceState.putParcelable(MYLISTKEY, messageListView.onSaveInstanceState());
    }

    @Override
    protected void onPause() {
        super.onPause();
        myListInstanceState = messageListView.onSaveInstanceState();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        savedInstanceState.putParcelable(MYLISTKEY, messageListView.onSaveInstanceState());
    }


}
