package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.model.Link;
import edu.java.bot.model.response.ApiErrorResponse;
import edu.java.bot.service.LinkNotificationService;
import java.net.URI;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkUpdatesController.class)
public class LinkUpdatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkNotificationService linkNotificationService;

    @Test
    @DisplayName("Тестирование LinkUpdatesController#handleUpdates с корректными данными")
    public void handleUpdatesShouldWorkCorrectly() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/updates")
                .content("""
                    {
                      "id": 1,
                      "url": "https://example.com",
                      "description": "string",
                      "tgChatIds": [
                        0
                      ]
                    }
                    """)
                .contentType("application/json")
        ).andExpect(status().isOk());

        Mockito.verify(linkNotificationService).notifyLinkUpdate(
            new Link(1L, URI.create("https://example.com"), "string"),
            List.of(0L)
        );
    }

    @Test
    @DisplayName("Тестирование LinkUpdatesController#handleUpdates с некорректными данными")
    public void handleUpdatesShouldReturnErrorWhenInputIncorrect() throws Exception {
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/updates")
                .content("""
                    {
                      "id": 1,
                      "url": "https://example.com"
                    }
                    """)
                .contentType("application/json")
        ).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "MethodArgumentNotValidException");
        Mockito.verify(linkNotificationService, Mockito.times(0)).notifyLinkUpdate(Mockito.any(), Mockito.anyList());
    }

}
