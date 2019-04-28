package com.example.scotlandyard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class messanger extends AppCompatActivity {

    private static Button btnSend;
    private static EditText textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textMessage = findViewById(R.id.edittext_chatbox);
        btnSend = findViewById(R.id.button_chatbox_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textMessage.getText().toString();
                //sendMessage(message);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /* 
    private void sendMessage( String message ) {
        if( mIsHost ) {
            Nearby.Connections.sendReliableMessage( mGoogleApiClient,
                    mRemotePeerEndpoints,
                    message.getBytes() );

            mMessageAdapter.add( message );
            mMessageAdapter.notifyDataSetChanged();
        } else {
            Nearby.Connections.sendReliableMessage( mGoogleApiClient,
                    mRemoteHostEndpoint,
                    ( Nearby.Connections.getLocalDeviceId( mGoogleApiClient ) + " says: " + message ).getBytes() );
        }
    }
    */
}
