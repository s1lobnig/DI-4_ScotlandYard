package com.example.scotlandyard.connection;

/**
 * interface for server
 */
public interface ServerInterface extends ConnectionInterface {
    /**
     * function is called, when advertising started
     */
    void onStartedAdvertising();

    /**
     * function is called, when advertising failed
     */
    void onFailedAdvertising();

    /**
     * function is called, when advertising stopped
     */
    void onStoppedAdvertising();

    /**
     * function is called, when an endpoint request an connection
     * @param endpoint              endpoint, who requests the connection
     */
    void onConnectionRequested(Endpoint endpoint);

}
