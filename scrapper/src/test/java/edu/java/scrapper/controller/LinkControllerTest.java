package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.LinkController;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkIsNotSupportedException;
import edu.java.service.LinkService;
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

@WebMvcTest(LinkController.class)
public class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    @Test
    @DisplayName("Тестирование LinkController#listLinks")
    public void listLinksShouldWorkCorrectly() throws Exception {
        Mockito.when(linkService.listLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(new LinkResponse(1L, URI.create("http://localhost"))), 1));
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/links")
                .header("Tg-Chat-Id", 1L)
                .contentType("application/json")
        ).andExpect(status().isOk()).andExpect(
            result -> Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"links\":[{\"id\":1,\"url\":\"http://localhost\"}],\"size\":1}")
        );
        Mockito.verify(linkService).listLinks(1L);
    }

    @Test
    @DisplayName("Тестирование LinkController#listLinks с отсутвующим чатом")
    public void listLinksShouldReturnErrorWhenChatIsMissing() throws Exception {
        Mockito.when(linkService.listLinks(1L))
            .thenThrow(new ChatNotFoundException(1L));
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/links")
                .contentType("application/json")
                .header("Tg-Chat-Id", 1L)
        ).andExpect(status().isNotFound()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("404", "ChatNotFoundException");
    }

    @Test
    @DisplayName("Тестирование LinkController#listLinks без заголовка Tg-Chat-Id")
    public void listLinksShouldReturnErrorWhenTgChatIdIsMissing() throws Exception {
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/links")
                .contentType("application/json")
        ).andExpect(status().isBadRequest()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "MissingRequestHeaderException");
    }

    @Test
    @DisplayName("Тестирование LinkController#addLink")
    public void addLinkShouldWorkCorrectly() throws Exception {
        Mockito.when(linkService.addLink(URI.create("http://localhost"), 1L))
            .thenReturn(new LinkResponse(1L, URI.create("http://localhost")));
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/links")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(URI.create("http://localhost"))))
                .header("Tg-Chat-Id", 1L)
        ).andExpect(status().isOk()).andExpect(
            result -> Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"id\":1,\"url\":\"http://localhost\"}")
        );

        Mockito.verify(linkService).addLink(URI.create("http://localhost"), 1L);
    }

    @Test
    @DisplayName("Тестирование LinkController#addLink с неверными данными")
    public void addLinkShouldReturnErrorWhenDataIsInvalid() throws Exception {
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/links")
                .contentType("application/json")
                .content("{}")
                .header("Tg-Chat-Id", 1L)
        ).andExpect(status().isBadRequest()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "MethodArgumentNotValidException");
    }

    @Test
    @DisplayName("Тестирование LinkController#addLink с неподдерживаемой ссылкой")
    public void addLinkShouldReturnErrorWhenLinkIsNotSupported() throws Exception {
        Mockito.when(linkService.addLink(URI.create("http://localhost"), 1L))
            .thenThrow(new LinkIsNotSupportedException(URI.create("http://localhost")));
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/links")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(URI.create("http://localhost"))))
                .header("Tg-Chat-Id", 1L)
        ).andExpect(status().isBadRequest()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "LinkIsNotSupportedException");
    }

    @Test
    @DisplayName("Тестирование LinkController#removeLink")
    public void removeLinkShouldWorkCorrectly() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/links")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(1L)))
                .header("Tg-Chat-Id", 1L)
        ).andExpect(status().isOk());

        Mockito.verify(linkService).removeLink(1L, 1L);
    }
}
