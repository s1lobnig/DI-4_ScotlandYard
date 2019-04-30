package com.example.scotlandyard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.scotlandyard.connection.ClientInterface;
import com.example.scotlandyard.connection.ClientService;
import com.example.scotlandyard.connection.ConnectionInterface;
import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
import com.google.android.gms.nearby.Nearby;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class messanger extends AppCompatActivity implements ClientInterface, ServerInterface{

    private static Button btnSend;
    private static EditText textMessage;

    private MessageAdapter mMessageAdapter;
    private ServerService serverService = ServerService.getInstance();
    private ClientService clientService = ClientService.getInstance();


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


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textM = textMessage.getText().toString();
                Message message = new Message(textM);

                mMessageAdapter.add(message);
                serverService.send(message);

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
}
