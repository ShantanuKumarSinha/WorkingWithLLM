package com.shantanu.workingwithllm;

import com.shantanu.workingwithllm.controller.ChatController;
import com.shantanu.workingwithllm.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private ChatModel chatModel;

    @Test
    void chat_returnsResponse() throws Exception {
        when(chatService.chat(anyString())).thenReturn("Why did the chicken cross the road?");

        mockMvc.perform(get("/api/chat").param("message", "Tell me a joke"))
                .andExpect(status().isOk())
                .andExpect(content().string("Why did the chicken cross the road?"));
    }

    @Test
    void chat_usesDefaultMessage() throws Exception {
        when(chatService.chat("Tell me a joke")).thenReturn("Here's a funny joke!");

        mockMvc.perform(get("/api/chat"))
                .andExpect(status().isOk())
                .andExpect(content().string("Here's a funny joke!"));
    }

    @Test
    void chatFull_returnsResponse() throws Exception {
        ChatResponse chatResponse = new ChatResponse(
                List.of(new Generation(new AssistantMessage("Hello!")))
        );
        when(chatService.chatWithFullResponse(anyString())).thenReturn(chatResponse);

        mockMvc.perform(get("/api/chat/full").param("message", "Hello"))
                .andExpect(status().isOk());
    }

    @Test
    void chatWithTemplate_returnsResponse() throws Exception {
        when(chatService.chatWithTemplate(anyString()))
                .thenReturn("Spring AI is a framework for building AI-powered applications.");

        mockMvc.perform(get("/api/chat/template").param("subject", "Spring AI"))
                .andExpect(status().isOk())
                .andExpect(content().string("Spring AI is a framework for building AI-powered applications."));
    }

    @Test
    void streamChat_returnsStream() throws Exception {
        ChatResponse chunk = new ChatResponse(
                List.of(new Generation(new AssistantMessage("Hello")))
        );
        when(chatService.streamChat(anyString())).thenReturn(Flux.just(chunk));

        mockMvc.perform(get("/api/chat/stream").param("message", "Hi"))
                .andExpect(status().isOk());
    }
}
