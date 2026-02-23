package com.kizza.helloworld.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

@Service
@Slf4j
public class ApiScheduledTask extends TimerTask {
    @Override
    public void run() {
        // Goal here is to call a public api, save it db for 6hrs then delete it and log what happened

    }
}
