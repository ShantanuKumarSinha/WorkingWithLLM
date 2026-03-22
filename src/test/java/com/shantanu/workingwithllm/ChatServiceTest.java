package com.shantanu.workingwithllm;

import com.shantanu.workingwithllm.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Test
    void chatWithFullResponse_callsChatModel() {
        ChatModel chatModel = mock(ChatModel.class);
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class);
        when(builder.build()).thenReturn(chatClient);

        ChatResponse expectedResponse = new ChatResponse(
                List.of(new Generation(new AssistantMessage("Hi there!")))
        );
        when(chatModel.call(any(Prompt.class))).thenReturn(expectedResponse);

        ChatService chatService = new ChatService(chatModel, builder);
        ChatResponse response = chatService.chatWithFullResponse("Hello");

        assertThat(response).isEqualTo(expectedResponse);
        verify(chatModel).call(any(Prompt.class));
    }

    @Test
    void streamChat_callsChatModelStream() {
        ChatModel chatModel = mock(ChatModel.class);
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class);
        when(builder.build()).thenReturn(chatClient);

        ChatResponse chunk = new ChatResponse(
                List.of(new Generation(new AssistantMessage("Hello")))
        );
        when(chatModel.stream(any(Prompt.class))).thenReturn(Flux.just(chunk));

        ChatService chatService = new ChatService(chatModel, builder);
        Flux<ChatResponse> flux = chatService.streamChat("Hello");

        List<ChatResponse> results = flux.collectList().block();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getResult().getOutput().getContent()).isEqualTo("Hello");
    }
}
