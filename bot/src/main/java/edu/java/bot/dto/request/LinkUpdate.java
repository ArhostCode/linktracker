package edu.java.bot.dto.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Map;

public record LinkUpdate(
    @NotNull Long id,
    @NotNull URI url,
    @NotNull String description,
    @NotNull List<Long> tgChatIds,
    @NotNull Map<String, String> metaInformation
) {
}
