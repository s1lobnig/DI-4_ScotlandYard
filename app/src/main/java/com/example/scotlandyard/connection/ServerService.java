package com.example.scotlandyard.connection;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerService extends ConnectionService {
    private String logTag = "ServerService";
    private Map<String, Endpoint> pendingConnections;
    private Map<String, Endpoint> establishedConnections;
    private ServerInterface server;

    ServerService(@NonNull ServerInterface server, String endpointName, Activity activity) {
        super(endpointName, activity);
        pendingConnections = new HashMap<>();
        establishedConnections = new HashMap<>();
        this.server = server;
    }

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

    public void stopAdvertising() {
        if (connectionState == ConnectionState.ADVERTISING) {
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

    public void acceptConnection(final Endpoint endpoint) {
        connectionsClient
                .acceptConnection(endpoint.getId(), payloadCallback)
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(logTag, "acceptConnection failed", e);
                                server.onFailedAcceptConnection(endpoint);
                            }
                        });
    }

    //TODO
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                    Log.d(logTag, String.format("payload received (endpointId=%s, payload=%s)", endpointId, payload));
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                    Log.d(logTag, String.format("payload update (endpointId=%s, update=%s)", endpointId, update));
                }
            };

    public void sendPayload(Payload payload) {
        send(payload, establishedConnections.keySet());
    }

    //TODO
    private void send(Payload payload, Set<String> endpoints) {
        if (connectionState == ConnectionState.CONNECTED) {
            Log.d(logTag, "sending payload");
            connectionsClient
                    .sendPayload(new ArrayList<>(endpoints), payload)
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(logTag, "sending payload failed", e);
                                }
                            });
        }
    }


}
