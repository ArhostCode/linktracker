package edu.java.scheduler;

import edu.java.client.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.provider.InformationProviders;
import edu.java.provider.api.InformationProvider;
import edu.java.provider.api.LinkInformation;
import edu.java.service.LinkService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkService linkService;
    private final ApplicationConfig appConfig;
    private final InformationProviders informationProviders;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.info("Update started");
        linkService.listOldLinks(appConfig.scheduler().forceCheckDelay(), appConfig.scheduler().maxLinksPerCheck())
            .forEach(link -> {
                log.info("Updating link {}", link);
                URI uri = URI.create(link.getUrl());
                InformationProvider provider = informationProviders.getProvider(uri.getHost());
                LinkInformation linkInformation = provider.fetchInformation(uri);
                linkInformation = provider.filter(linkInformation, link.getUpdatedAt(), link.getDescription());

//                if (linkInformation.lastModified().isAfter(link.getUpdatedAt())) {
//                    linkService.update(link.getId(), linkInformation.lastModified());
//                    botClient.handleUpdates(new LinkUpdate(
//                        link.getId(),
//                        uri,
//                        linkInformation.updateType(),
//                        linkService.getLinkSubscribers(link.getId()).stream()
//                            .map(TgChat::getId)
//                            .toList()
//                    ));
//                }
            });
        log.info("Update finished");
    }

}
