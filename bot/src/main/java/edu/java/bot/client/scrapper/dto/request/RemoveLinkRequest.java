package edu.java.bot.client.scrapper.dto.request;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(
    @NotNull Long id
) {
}
