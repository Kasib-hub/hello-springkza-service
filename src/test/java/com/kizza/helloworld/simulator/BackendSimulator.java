package com.kizza.helloworld.simulator;

import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;

public class BackendSimulator {

    public static void main(String[] args) throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start(9091);
        mockWebServer.setDispatcher(new BackendDispatcher());
    }
}
