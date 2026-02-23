package com.kizza.helloworld.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class BackendDispatcher extends Dispatcher {
    @NotNull
    @Override
    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {

        } catch (Exception e) {
            log.error("Error encountered: ", e);
        }

        return new MockResponse().setResponseCode(400);
    }
}
