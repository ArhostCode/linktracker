package edu.java.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.info("Updating links");
    }

}
