package edu.java.bot.client.scrapper.dto.response;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
