package edu.java.bot.client.scrapper;

import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface ScrapperClient {

    @PostExchange("/tg-chat/{id}")
    OptionalAnswer<Void> registerChat(@PathVariable Long id);

    @DeleteExchange("/tg-chat/{id}")
    OptionalAnswer<Void> deleteChat(@PathVariable Long id);

    @GetExchange("/links")
    OptionalAnswer<ListLinksResponse> listLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId);

    @PostExchange("/links")
    OptionalAnswer<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    );

    @DeleteExchange("/links")
    OptionalAnswer<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    );
}
