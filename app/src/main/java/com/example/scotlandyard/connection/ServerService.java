package com.example.scotlandyard.connection;

import android.support.annotation.NonNull;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
     * @param server                object implementing server interface
     * @param endpointName          name of the device (nickname)
     * @param connectionsClient     connectionsClient of google api of the activity
     */
    public ServerService(@NonNull ServerInterface server, String endpointName, ConnectionsClient connectionsClient) {
        super(endpointName, connectionsClient);
        pendingConnections = new HashMap<>();
        establishedConnections = new HashMap<>();
        this.server = server;
    }

    /**
     * function starts advertising of server
     */
    public void startAdvertising() {
        if (connectionState == ConnectionState.DISCONNECTED || connectionState == ConnectionState.CONNECTED) {
            if (connectionState == ConnectionState.CONNECTED) {
                connectionState = ConnectionState.ADVERTISING_CONNECTED;
            } else {
                connectionState = ConnectionState.ADVERTISING;
            }
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
                                    if (connectionState == ConnectionState.ADVERTISING_CONNECTED) {
                                        connectionState = ConnectionState.CONNECTED;
                                    } else {
                                        connectionState = ConnectionState.DISCONNECTED;
                                    }
                                    Log.d(logTag, "startAdvertising failed", e);
                                    server.onFailedAdvertising();
                                }
                            });
        } else {
            Log.d(logTag, "could not start advertising. Not Disconnected");
        }
    }

    /**
     * function stops advertising of server
     */
    public void stopAdvertising() {
        if (connectionState == ConnectionState.ADVERTISING || connectionState == ConnectionState.ADVERTISING_CONNECTED) {
            Log.d(logTag, "stopped advertising");
            connectionsClient.stopAdvertising();
            if (establishedConnections.isEmpty()) {
                connectionState = ConnectionState.DISCONNECTED;
            } else {
                connectionState = ConnectionState.CONNECTED;
            }
            server.onStoppedAdvertising();
        } else {
            Log.d(logTag, "could not stop advertising. Not Advertising");
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
                            if (connectionState == ConnectionState.ADVERTISING) {
                                connectionState = ConnectionState.ADVERTISING_CONNECTED;
                            }
                            server.onConnected(endpoint);
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
                    if (connectionState == ConnectionState.ADVERTISING_CONNECTED) {
                        if (establishedConnections.isEmpty()) {
                            connectionState = ConnectionState.ADVERTISING;
                        }
                    } else {
                        if (establishedConnections.isEmpty()) {
                            connectionState = ConnectionState.DISCONNECTED;
                        }
                    }
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
                        Log.d(logTag, "acceptConnection success");
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(logTag, "acceptConnection failed", e);
                                server.onFailedAcceptConnection(endpoint);
                            }
                        });
    }

    /**
     * function for declining a connection
     * @param endpoint       endpoint, which connection is rejected
     */
    public void rejectConnection(Endpoint endpoint) {
        connectionsClient
                .rejectConnection(endpoint.getId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "rejected connection");
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(logTag, "rejectConnection failed", e);
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
                        server.onDataReceived(object, endpointId);
                    }
                }

                // no need for implementation for byte[] payload
                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                    Log.d(logTag, String.format("payload update (endpointId=%s, update=%s)", endpointId, update));
                }
            };

    private Payload createPayload(Object object) {
        byte[] data = null;
        try {
            data = serialize(object);
        } catch (IOException ex) {
            Log.d(logTag, "error in serialization", ex);
            server.onSendingFailed(object);
        }
        Payload payload = null;
        if (data != null) {
            if (data.length > ConnectionsClient.MAX_BYTES_DATA_SIZE) {
                Log.d(logTag, "byte array size > MAX_BYTES_DATA_SIZE");
                // will this be a problem ?
            }
            payload = Payload.fromBytes(data);
        }
        return payload;
    }

    /**
     * function for broadcasting data over the connection
     * @param object       object to send
     */
    public void send(Object object) {
        Payload payload = createPayload(object);
        if (payload != null ) {
            sendPayload(payload, establishedConnections.keySet());
        }
    }

    /**
     * function for sending data over the connection
     * @param object       object to send
     */
    public void send(Object object, Set<String> connections) {
        Payload payload = createPayload(object);
        if (payload != null) {
            sendPayload(payload, connections);
        }
    }

    /**
     * function for sending data over the connection
     * @param object       object to send
     * @param endpoint     endpoint to send to
     */
    public void send(Object object, Endpoint endpoint) {
        Set<String> set = new HashSet<>();
        set.add(endpoint.getId());
        send(object, set);
    }

    /**
     * function for sending payload
     * @param payload           payload to send
     * @param endpoints         list of endpoints to send to
     */
    private void sendPayload(final Payload payload, Set<String> endpoints) {
        if (connectionState == ConnectionState.CONNECTED || connectionState == ConnectionState.ADVERTISING_CONNECTED) {
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

    /**
     * function for disconnecting from an endpoint
     * @param endpoint  endpoint to disconnect from
     */
    public void disconnect(@NonNull Endpoint endpoint) {
        if (connectionState == ConnectionState.CONNECTED || connectionState == ConnectionState.ADVERTISING_CONNECTED) {
            Endpoint endpoint2 = establishedConnections.get(endpoint.getId());
            if (endpoint2 != null) {
                Log.d(logTag, "disconnecting from "+endpoint.getName());
                connectionsClient.disconnectFromEndpoint(endpoint.getId());
                establishedConnections.remove(endpoint.getId());
                if (establishedConnections.isEmpty()) {
                    if (connectionState == ConnectionState.ADVERTISING_CONNECTED) {
                        connectionState = ConnectionState.ADVERTISING;
                    } else {
                        connectionState = ConnectionState.DISCONNECTED;
                    }
                }
                server.onDisconnected(endpoint2);
            }
        }
    }

    /**
     * function for disconnecting from all endpoints
     */
    public void disconnectFromAll() {
        if (connectionState == ConnectionState.CONNECTED) {
            Set<String> keys = establishedConnections.keySet();
            for (String k : keys) {
                Endpoint endpoint = establishedConnections.get(k);
                if (endpoint != null) {
                    disconnect(endpoint);
                    server.onDisconnected(endpoint);
                }
            }
            establishedConnections.clear();
            if (connectionState == ConnectionState.ADVERTISING_CONNECTED) {
                connectionState = ConnectionState.ADVERTISING;
            } else {
                connectionState = ConnectionState.DISCONNECTED;
            }
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

    public void disconnect(String playerName){
        if (connectionState == ConnectionState.CONNECTED) {
            Set<String> keys = establishedConnections.keySet();
            for (String k : keys) {
                Endpoint endpoint = establishedConnections.get(k);
                if (endpoint != null && k.equals(playerName)) {
                    disconnect(endpoint);
                    server.onDisconnected(endpoint);
                }
            }
            establishedConnections.remove(playerName);
        }
    }
}
