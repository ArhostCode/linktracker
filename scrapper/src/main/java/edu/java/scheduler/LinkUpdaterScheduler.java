package edu.java.scheduler;

import edu.java.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LinkUpdaterScheduler {

//    private final ApplicationConfig config;
//
//    public LinkUpdaterScheduler(ApplicationConfig config) {
//        this.config = config;
//    }

    //    @Scheduled(fixedDelayString = "#{@app-edu.java.configuration.ApplicationConfig.scheduler.interval}")
    public void update() {
        log.info("Updating links");
    }

    @Autowired
    private ApplicationConfig config;

    @PostConstruct
    public void init() {
        System.out.println(config.help());
    }

}
