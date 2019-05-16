package com.example.scotlandyard.connection;

import android.support.annotation.NonNull;

import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Strategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * abstract basic class of the service
 * connectionsClient:       main API class
 * strategy:                network strategy
 * serviceID:               service id of app for identification
 * endpointName             name of local device
 * connectionState          state of the service
 */
public abstract class ConnectionService {
    ConnectionsClient connectionsClient;
    Strategy strategy;
    String serviceID;
    String endpointName;
    ConnectionState connectionState;

    /**
     * Constructor
     * @param endpointName          name of the device (nickname)
     * @param connectionsClient     connectionsClient of google api of the activity
     */
    ConnectionService(@NonNull String endpointName, @NonNull ConnectionsClient connectionsClient) {
        this.connectionsClient = connectionsClient;
        strategy = Strategy.P2P_STAR;
        serviceID = "com.google.aau.scotland_yard";
        this.endpointName = endpointName;
        connectionState = ConnectionState.DISCONNECTED;
    }

    /**
     * function for serializing an object
     * @param object            object to serialize
     * @return                  byte[] of object
     * @throws IOException
     */
    byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * function for deserializing an object
     * @param bytes             byte[] of object
     * @return                  object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public abstract void send(Object object);

}
