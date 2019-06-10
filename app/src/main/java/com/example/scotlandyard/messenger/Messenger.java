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
import android.widget.Toast;

import com.example.scotlandyard.R;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.control.Device;
import com.example.scotlandyard.control.MessengerInterface;

import java.util.ArrayList;

public class Messenger extends AppCompatActivity implements MessengerInterface {


    private EditText textMessage;
    private ListView messageListView;

    private MessageAdapter mMessageAdapter;
    private Message message;
    private String logTag = "Messanger";
    private String nickname;
    private ArrayList<Message> messageArrayList = Device.getInstance().getMessageList();

    protected void onResume() {
        super.onResume();
        mMessageAdapter.setMessages(Device.getInstance().getMessageList());
        /*scroll the ListView to the last added element*/
        messageListView.setSelection(messageListView.getCount() - 1);
        try {
            Device.getInstance().addMessengerObserver(this);
        } catch (IllegalStateException ex) {
            Log.d("Messenger", "messenger observer already added.");
        }
    }

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
        mMessageAdapter = new MessageAdapter(this, nickname);
        mMessageAdapter.setMessages(Device.getInstance().getMessageList());

        messageListView.setAdapter(mMessageAdapter);

        /*add messenger observer*/
        Device.getInstance().addMessengerObserver(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = new Message(textMessage.getText().toString(), nickname);

                Device.getInstance().send(message);
                Device.getInstance().addMessage(message);

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

        /*display message*/
        mMessageAdapter.setMessages(messages);

        /*scroll the ListView to the last added element*/
        messageListView.setSelection(messageListView.getCount() - 1);

    }

    @Override
    public void showDisconnected(Endpoint endpoint) {
        Toast.makeText(Messenger.this, getString(R.string.lostConnection) + " " + endpoint.getName() + " verloren!", Toast.LENGTH_LONG).show();
        Log.d(logTag, "should not be called");
    }

    @Override
    public void showSendingFailed(Object object) {
        Toast.makeText(Messenger.this, message + " konnte nicht gesendet werden!", Toast.LENGTH_LONG).show();
        //TODO give possibility to sync the chat again
        Log.d(logTag, "sending has failed");
    }

    @Override
    public void onReceivedToast(String toast) {
        Toast.makeText(Messenger.this, toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showReconnected(String endpointName) {
        Toast.makeText(Messenger.this, getString(R.string.lostConnection) + " " + endpointName + " wiederhergestellt!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showReconnectFailed(String endpointName) {
        Toast.makeText(Messenger.this, getString(R.string.lostConnection) + " " + endpointName + " konnte nicht wiederhergestellt werden!", Toast.LENGTH_LONG).show();
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
