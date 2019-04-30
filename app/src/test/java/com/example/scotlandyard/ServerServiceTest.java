package com.example.scotlandyard;

import android.app.Activity;

import com.example.scotlandyard.connection.Endpoint;
import com.example.scotlandyard.connection.ServerInterface;
import com.example.scotlandyard.connection.ServerService;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

public class ServerServiceTest {
    /* not working, needs to be discussed with tutor
    @Mock
    ServerInterface serverInterface;

    @Mock
    ConnectionsClient connectionsClient;

    Activity activity = new MainActivity();

    @InjectMocks
    ServerService serverService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        serverService = null;
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionSingletonNotSet()  {
        ServerService.getInstance();
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionSingletonAlreadySet()  {
        ServerService.getInstance(serverInterface, "testEndpoint", activity);
        ServerService.getInstance(serverInterface, "testEndpoint", activity);
    }*/
}
