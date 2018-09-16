package com.hroniko.stats.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogScheduler {
    @Autowired
    LogicalProcessor logicalProcessor;

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        logicalProcessor.runForAllHostname();
    }
}
