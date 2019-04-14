package com.example.scotlandyard.connection;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class ClientService extends ConnectionService {
    private String logTag;
    private Map<String, Endpoint> discoveredEndpoints;
    private Endpoint connection;
    private ClientInterface client;

    ClientService(@NonNull ClientInterface clientInterface, String endpointName, Activity activity) {
        super(endpointName, activity);
        discoveredEndpoints = new HashMap<>();
        logTag = "ClientService";
        this.client = clientInterface;
    }

    public void startDiscovery() {
        if (connectionState == ConnectionState.DISCONNECTED) {
            connectionState = ConnectionState.DISCOVERING;
            discoveredEndpoints.clear();
            final DiscoveryOptions.Builder discoveryOptions = new DiscoveryOptions.Builder();
            discoveryOptions.setStrategy(strategy);
            connectionsClient
                    .startDiscovery(
                            serviceID,
                            endpointDiscoveryCallback,
                            discoveryOptions.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unusedResult) {
                                    Log.d(logTag, "started discovering");
                                    client.onStartedDiscovery();
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    connectionState = ConnectionState.DISCONNECTED;
                                    Log.d(logTag,"starting discovering failed", e);
                                    client.onFailedDiscovery();
                                }
                            });
        }
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
            Log.d(logTag,
                    String.format("endpoint found (endpointId=%s, serviceId=%s, endpointName=%s)",
                            endpointId, info.getServiceId(), info.getEndpointName()));
            if (serviceID.equals(info.getServiceId())) {
                Endpoint endpoint = new Endpoint(endpointId, info.getEndpointName());
                discoveredEndpoints.put(endpointId, endpoint);
                client.onEndpointFound(discoveredEndpoints);
            }
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            Log.d(logTag, String.format("endpoint lost (endpointId=%s)", endpointId));
            Endpoint endpoint = discoveredEndpoints.get(endpointId);
            if (endpoint != null) {
                discoveredEndpoints.remove(endpointId);
                client.onEndpointLost(discoveredEndpoints);
            }
        }
    };

    public void stopDiscovery() {
        if (connectionState == ConnectionState.DISCOVERING) {
            Log.d(logTag, "stopped discovering");
            connectionsClient.stopDiscovery();
            connectionState = ConnectionState.DISCONNECTED;
            client.onStoppedDiscovery();
        }
    }

    public void connectToEndpoint(@NonNull final Endpoint endpoint) {
        if (connectionState != ConnectionState.CONNECTING) {
            if (connectionState == ConnectionState.DISCOVERING) {
                stopDiscovery();
                connectionState = ConnectionState.CONNECTING;
                Log.d(logTag, "sending a connection request to endpoint " + endpoint);
                connectionsClient
                        .requestConnection(endpointName, endpoint.getId(), connectionLifecycleCallback)
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(logTag, "requestConnection failed", e);
                                        connectionState = ConnectionState.DISCONNECTED;
                                        client.onFailedConnecting(endpoint);
                                    }
                                });
            }
        }
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull String endpointId, final ConnectionInfo connectionInfo) {
                    Log.d(logTag, String.format("connection initiated (endpointId=%s, endpointName=%s)",
                                    endpointId, connectionInfo.getEndpointName()));
                    connection = new Endpoint(endpointId, connectionInfo.getEndpointName());
                    connectionsClient
                            .acceptConnection(connection.getId(), payloadCallback)
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(logTag, "acceptConnection failed", e);
                                            connectionState = ConnectionState.DISCONNECTED;
                                            client.onFailedAcceptConnection(connection);
                                        }
                                    });
                }

                @Override
                public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
                    Log.d(logTag, String.format("connection response (endpointId=%s, result=%s)", endpointId, result));
                    if (result.getStatus().isSuccess()) {
                        connectionState = ConnectionState.CONNECTED;
                        client.onConnected();
                    } else {
                        connectionState = ConnectionState.DISCONNECTED;
                        client.onFailedConnecting(connection);
                    }
                }

                @Override
                public void onDisconnected(@NonNull String endpointId) {
                    Log.d(logTag, String.format("disconnected from endpoint (endpoint=%s)", endpointId));
                    connectionState = ConnectionState.DISCONNECTED;
                    client.onDisconnected();
                }
            };

    //TODO
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(@NonNull String endpointId,@NonNull Payload payload) {
                    Log.d(logTag, String.format("payload received (endpointId=%s, payload=%s)", endpointId, payload));
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                    Log.d(logTag, String.format("payload update (endpointId=%s, update=%s)", endpointId, update));
                }
            };

    //TODO
    private void sendPayload(Payload payload) {
        if (connectionState == ConnectionState.CONNECTED) {
            Log.d(logTag, "sending payload");
            connectionsClient
                    .sendPayload(connection.getId(), payload)
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
