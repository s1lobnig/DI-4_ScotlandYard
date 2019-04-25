package com.example.scotlandyard.connection;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.scotlandyard.Game;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * class representing a client in the service
 * logTag:                  log tag for log messages
 * discoveredEndpoints      list of current discovered endpoints
 * connection               current active connection
 * client                   interface to client activity
 */
public class ClientService extends ConnectionService {
    private String logTag;
    private Map<String, Endpoint> discoveredEndpoints;
    private Endpoint connection;
    private ClientInterface client;

    /**
     * Constructor
     *
     * @param clientInterface object implementing client interface
     * @param endpointName    name of the device (nickname)
     * @param activity        current activity
     */
    ClientService(@NonNull ClientInterface clientInterface, String endpointName, Activity activity) {
        super(endpointName, activity);
        discoveredEndpoints = new HashMap<>();
        logTag = "ClientService";
        this.client = clientInterface;
    }

    /**
     * function starts discovery of client
     */
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
                                    Log.d(logTag, "starting discovering failed", e);
                                    client.onFailedDiscovery();
                                }
                            });
        }
    }

    /**
     * callback function, when an endpoint is found or lost
     */
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

    /**
     * function stops discovery
     */
    public void stopDiscovery() {
        if (connectionState == ConnectionState.DISCOVERING) {
            Log.d(logTag, "stopped discovering");
            connectionsClient.stopDiscovery();
            connectionState = ConnectionState.DISCONNECTED;
            client.onStoppedDiscovery();
        }
    }

    /**
     * function for connecting to an endpoint
     *
     * @param endpoint endpoint to connect to
     */
    public void connectToEndpoint(@NonNull final Endpoint endpoint) {
        if (connectionState != ConnectionState.CONNECTING && connectionState == ConnectionState.DISCOVERING) {
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

    /**
     * callback function for connection lifecycle (connection, disconnection)
     */
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
                        client.onConnected(connection);
                    } else {
                        connectionState = ConnectionState.DISCONNECTED;
                        client.onFailedConnecting(connection);
                    }
                }

                @Override
                public void onDisconnected(@NonNull String endpointId) {
                    Log.d(logTag, String.format("disconnected from endpoint (endpoint=%s)", endpointId));
                    if (connection != null && connection.getId().equals(endpointId)) {
                        connectionState = ConnectionState.DISCONNECTED;
                        client.onDisconnected(connection);
                    }
                }
            };

    /**
     * callback function for a received payload
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
                            client.onMessage(object);
                        }
                        if (object instanceof Game) {
                            client.onGameData(object);
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
     *
     * @param object object to send (game data or chat message)
     */
    public void send(Object object) {
        if (object instanceof Message || object instanceof Game) {
            byte[] data = null;
            try {
                data = serialize(object);
            } catch (IOException ex) {
                Log.d(logTag, "error in serialization", ex);
                client.onSendingFailed(object);
            }
            if (data != null) {
                if (data.length > ConnectionsClient.MAX_BYTES_DATA_SIZE) {
                    Log.d(logTag, "byte array size > MAX_BYTES_DATA_SIZE");
                    // will this be a problem ?
                }
                Payload payload = Payload.fromBytes(data);
                sendPayload(payload);
            }
        }
    }

    /**
     * function for sending a payload
     *
     * @param payload payload to send
     */
    private void sendPayload(final Payload payload) {
        if (connectionState == ConnectionState.CONNECTED) {
            Log.d(logTag, "sending payload");
            connectionsClient
                    .sendPayload(connection.getId(), payload)
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
                                    client.onSendingFailed(object);
                                }
                            });
        }
    }

    public Map<String, Endpoint> getDiscoveredEndpoints() {
        return discoveredEndpoints;
    }

    public Endpoint getConnection() {
        return connection;
    }

    public void setClient(@NonNull ClientInterface client) {
        this.client = client;
    }
}
