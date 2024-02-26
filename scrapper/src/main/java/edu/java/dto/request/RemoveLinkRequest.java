package edu.java.dto.request;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(
    @NotNull Long id
) {
}
