package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/updates", consumes = "application/json")
@RequiredArgsConstructor
public class LinkUpdatesController {

    private final LinkNotificationService linkNotificationService;

    @PostMapping
    public void handleUpdates(@RequestBody @Valid LinkUpdate linkUpdate) {
        linkNotificationService.notifyLinkUpdate(linkUpdate);
    }
}
