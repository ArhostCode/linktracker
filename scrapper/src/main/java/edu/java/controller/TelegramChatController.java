package edu.java.controller;

import edu.java.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat/{id}")
@RequiredArgsConstructor
public class TelegramChatController {

    private final ChatService chatService;

    @Operation(summary = "Зарегистрировать чат")
    @PostMapping
    public void registerChat(@PathVariable Long id) {
        chatService.registerChat(id);
    }

    @Operation(summary = "Удалить чат")
    @DeleteMapping
    public void deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
    }

}
