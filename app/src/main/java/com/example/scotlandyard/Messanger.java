package com.example.scotlandyard;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;

import java.util.Map;

public class Messanger extends AppCompatActivity implements ServerInterface, ClientInterface {

    private Button btnSend;
    private EditText textMessage;
    private ListView messageList;

    private MessageAdapter mMessageAdapter;
    private Game game;
    private boolean isServer;
    private String logTag;
    private ServerService serverService;
    private ClientService clientService;
    private boolean isBelongsToCurrentUser = false; //if no send button is clicked, then message is received


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);

        /* add Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* find views */
        textMessage = findViewById(R.id.edittext_chatbox);
        btnSend = findViewById(R.id.button_chatbox_send);
        messageList = findViewById(R.id.message_list);

        /*get data from Intent to distinguish between host and client*/
        Intent intent = getIntent();
        //game = ((Game)intent.getSerializableExtra("GAME"));
        isServer = intent.getBooleanExtra("IS_SERVER", true);

        /*check if user is server or client*/
        if (isServer) {
            serverService = ServerService.getInstance();
            serverService.setServer(this);
            logTag = "SERVER_SERVICE";
        } else {
            clientService = ClientService.getInstance();
            clientService.setClient(this);
            logTag = "CLIENT_SERVICE";
        }

        /*set the Adapter to listView*/
        mMessageAdapter = new MessageAdapter(this);
        messageList.setAdapter(mMessageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textM = textMessage.getText().toString();

                /*set message belong to current user*/
                isBelongsToCurrentUser = true;
                Message message = new Message(textM, isBelongsToCurrentUser);
                mMessageAdapter.add(message);

                /*test to check if message comes from server or not*/
                if (isServer) {
                    textM = " from Server";
                    /*send message to all players*/
                    onMessage(message);

                } else {
                    textM = " from Client";
                    Log.d(logTag, "Client is sending chat message to server");
                    clientService.send(message);
                }

                /*Make Toast to check if receiving message is working*/
                Toast.makeText(Messanger.this, "" + message.getMessage() + textM, Snackbar.LENGTH_LONG).show();

                /*scroll the ListView to the last added element*/
                messageList.setSelection(messageList.getCount() - 1);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStartedDiscovery() {

    }

    @Override
    public void onFailedDiscovery() {

    }

    @Override
    public void onEndpointFound(Map<String, Endpoint> discoveredEndpoints) {

    }

    @Override
    public void onEndpointLost(Map<String, Endpoint> discoveredEndpoints) {

    }

    @Override
    public void onStoppedDiscovery() {

    }

    @Override
    public void onConnected(Endpoint endpoint) {

    }

    @Override
    public void onStartedAdvertising() {

    }

    @Override
    public void onFailedAdvertising() {

    }

    @Override
    public void onStoppedAdvertising() {

    }

    @Override
    public void onConnectionRequested(Endpoint endpoint) {

    }

    @Override
    public void onConnected(Map<String, Endpoint> establishedConnections) {

    }

    @Override
    public void onGameData(Object game) {

    }

    @Override
    public void onMessage(Object message) {
        Log.d(logTag, "Chat message received!");

        /*show received message for all users*/
        isBelongsToCurrentUser = false;
        String textOfMessage = ((Message) message).getMessage();
        Message receivedMessage = new Message(textOfMessage,isBelongsToCurrentUser);

        if (isServer) {
            Log.d(logTag, "Server is sending chat message to clients");
            serverService.send(receivedMessage);
            if(!isServer) {
                mMessageAdapter.add(receivedMessage);
                /*scroll the ListView to the last added element*/
                messageList.setSelection(messageList.getCount() - 1);
            }

        }


    }

    @Override
    public void onSendMove(Object sendMove) {

    }

    @Override
    public void onFailedConnecting(Endpoint endpoint) {

    }

    @Override
    public void onDisconnected(Endpoint endpoint) {

    }

    @Override
    public void onFailedAcceptConnection(Endpoint endpoint) {

    }

    @Override
    public void onSendingFailed(Object object) {

    }

    public void addMessageAdapter(Message message){

    }
}
