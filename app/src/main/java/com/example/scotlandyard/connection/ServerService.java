package com.example.scotlandyard.connection;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.scotlandyard.Game;
import com.example.scotlandyard.Message;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// TODO: Add disconnectEndpoint(Endpoint e) method. Reason: com.google.android.gms.common.api.ApiException: 8003: STATUS_ALREADY_CONNECTED_TO_ENDPOINT (endpoint cannot reconnect after connection lost/terminated).
// TODO: It would be almost necessary to know from which endpoint the message comes: onMessage(Object message, Endpoint e) and onGameData(Object game, Endpoint e).
// TODO: Make sending to a single client possible. Suggestion: sendMessage(Object object, Endpoint endpoint).
// TODO: Check why discovery stops after connected to a client.

/**
 * class representing a server in the service
 * logTag:                  log tag for log messages
 * pendingConnections:      current pending connections
 * establishedConnections:  current established connections
 * server:                  interface to server activity
 */
public class ServerService extends ConnectionService{
    private String logTag = "ServerService";
    private Map<String, Endpoint> pendingConnections;
    private Map<String, Endpoint> establishedConnections;
    private ServerInterface server;

    /**
     * Constructor
     * @param server            object implementing server interface
     * @param endpointName      name of the device (nickname)
     * @param activity          current activity
     */
    public ServerService(@NonNull ServerInterface server, String endpointName, Activity activity) {
        super(endpointName, activity);
        pendingConnections = new HashMap<>();
        establishedConnections = new HashMap<>();
        this.server = server;
    }

    /**
     * function starts advertising of server
     */
    public void startAdvertising() {
        if (connectionState == ConnectionState.DISCONNECTED) {
            connectionState = ConnectionState.ADVERTISING;
            AdvertisingOptions.Builder advertisingOptions = new AdvertisingOptions.Builder();
            advertisingOptions.setStrategy(strategy);
            connectionsClient
                    .startAdvertising(
                            endpointName,
                            serviceID,
                            connectionLifecycleCallback,
                            advertisingOptions.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unusedResult) {
                                    Log.d(logTag, "Now advertising endpoint " + endpointName);
                                    server.onStartedAdvertising();
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    connectionState = ConnectionState.DISCONNECTED;
                                    Log.d(logTag, "startAdvertising failed", e);
                                    server.onFailedAdvertising();
                                }
                            });
        }
    }

    /**
     * function stops advertising of server
     */
    public void stopAdvertising() {
        /* TODO: Check if this ConnectionState is really sufficient. Reason: After switching to CONNECTED - stopAdvertising() doesn't do anything, thus, advertising cannot be stopped. */
        if (true || connectionState == ConnectionState.ADVERTISING) { // Added 'true || ' here as temporary fix.
            Log.d(logTag, "stopped advertising");
            connectionsClient.stopAdvertising();
            if (establishedConnections.isEmpty()) {
                connectionState = ConnectionState.DISCONNECTED;
            } else {
                connectionState = ConnectionState.CONNECTED;
            }
            server.onStoppedAdvertising();
        }
    }

    /**
     * callback function for connection lifecycle (connection, disconnection)
     */
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull String endpointId, ConnectionInfo connectionInfo) {
                    Log.d(logTag, String.format("connection initiated (endpointId=%s, endpointName=%s)",
                            endpointId, connectionInfo.getEndpointName()));
                    Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                    pendingConnections.put(endpointId, endpoint);
                    server.onConnectionRequested(endpoint);
                }

                @Override
                public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
                    Log.d(logTag, String.format("connection response (endpointId=%s, result=%s)", endpointId, result));
                    Endpoint endpoint = pendingConnections.get(endpointId);
                    if (endpoint != null) {
                        if (result.getStatus().isSuccess()) {
                            establishedConnections.put(endpointId, endpoint);
                            server.onConnected(establishedConnections);
                        } else {
                            server.onFailedConnecting(endpoint);
                        }
                    }
                    pendingConnections.remove(endpointId);
                }

                @Override
                public void onDisconnected(@NonNull String endpointId) {
                    Endpoint endpoint = establishedConnections.get(endpointId);
                    if (endpoint != null) {
                        server.onDisconnected(endpoint);
                    }
                    establishedConnections.remove(endpointId);
                }
            };

    /**
     * function for accepting a connection
     * @param endpoint          endpoint, where connection is accepted
     */
    public void acceptConnection(final Endpoint endpoint) {
        connectionsClient
                .acceptConnection(endpoint.getId(), payloadCallback)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: Temporary fix added because the server couldn't sendPayload() because the ConnectionState was still DISCONNECTED. Please check & verify this fix.
                        connectionState = establishedConnections.isEmpty() ? ConnectionState.DISCONNECTED : ConnectionState.CONNECTED;
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(logTag, "acceptConnection failed", e);
                                server.onFailedAcceptConnection(endpoint);

                                // TODO: Temporary fix added because the server couldn't sendPayload() because the ConnectionState was still DISCONNECTED. Please check & verify this fix.
                                connectionState = establishedConnections.isEmpty() ? ConnectionState.DISCONNECTED : ConnectionState.CONNECTED;
                            }
                        });
    }

    /**
     * callback function for received payload
     */
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                    Log.d(logTag, String.format("payload received (endpointId=%s, payload=%s)", endpointId, payload));
                    Object object = null;
                    try {
                        object = deserialize(payload.asBytes());
                    } catch (Exception ex) {
                        Log.d(logTag, "error in deserialization", ex);
                    }
                    if (object != null) {
                        if (object instanceof Message) {
                            server.onMessage(object);
                        }
                        if (object instanceof  Game) {
                            server.onGameData(object);
                        }
                    }
                }

                // no need for implementation for byte[] payload
                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                    Log.d(logTag, String.format("payload update (endpointId=%s, update=%s)", endpointId, update));
                }
            };

    /**
     * function for sending a chat message or game data
     * @param object       object to send (game data or chat message)
     */
    public void send(Object object) {
        if (object instanceof Message || object instanceof Game) {
            byte[] data = null;
            try {
                data = serialize(object);
            } catch (IOException ex) {
                Log.d(logTag, "error in serialization", ex);
                server.onSendingFailed(object);
            }
            if (data != null) {
                if (data.length > ConnectionsClient.MAX_BYTES_DATA_SIZE) {
                    Log.d(logTag, "byte array size > MAX_BYTES_DATA_SIZE");
                    // will this be a problem ?
                }
                Payload payload = Payload.fromBytes(data);
                sendPayload(payload, establishedConnections.keySet());
            }
        }
    }

    /**
     * function for sending payload
     * @param payload           payload to send
     * @param endpoints         list of endpoints to send to
     */
    private void sendPayload(final Payload payload, Set<String> endpoints) {
        if (connectionState == ConnectionState.CONNECTED) {
            Log.d(logTag, "sending payload");
            connectionsClient
                    .sendPayload(new ArrayList<>(endpoints), payload)
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(logTag, "sending payload failed", e);
                                    Object object = null;
                                    try {
                                        object = deserialize(payload.asBytes());
                                    } catch (Exception ex) {
                                        Log.d(logTag, "error in deserialization", ex);
                                    }
                                    server.onSendingFailed(object);
                                }
                            });
        }
    }

    public Map<String, Endpoint> getPendingConnections() {
        return pendingConnections;
    }

    public Map<String, Endpoint> getEstablishedConnections() {
        return establishedConnections;
    }

    public void setServer(ServerInterface server) {
        this.server = server;
    }
}
