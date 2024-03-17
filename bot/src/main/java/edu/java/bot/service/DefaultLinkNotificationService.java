package edu.java.bot.service;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.util.TextResolver;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class DefaultLinkNotificationService implements LinkNotificationService {

    private final RequestExecutor requestExecutor;
    private final ScrapperClient scrapperClient;
    private final TextResolver textResolver;

    @Override
    public void notifyLinkUpdate(LinkUpdate link) {
        link.tgChatIds().forEach(chatId -> {
            SendResponse response = requestExecutor.execute(
                new SendMessage(
                    chatId,
                    textResolver.resolve(
                        "link.update",
                        Map.of(
                            "link",
                            link.url().toString(),
                            "description",
                            textResolver.resolve(
                                link.description(),
                                link.metaInformation(),
                                "Общее обновление информации"
                            )
                        )
                    )
                ).disableWebPagePreview(true)
                    .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true))
            );
            if (response.message() == null) {
                scrapperClient.deleteChat(chatId);
            }
        });
    }

    private String resolve(String template, Map<String, String> args) {
        return textResolver.resolve(template, args);
    }

}
