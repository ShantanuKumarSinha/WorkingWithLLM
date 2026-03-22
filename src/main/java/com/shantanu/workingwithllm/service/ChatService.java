package com.shantanu.workingwithllm.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ChatService {

    private final ChatModel chatModel;
    private final ChatClient chatClient;

    public ChatService(ChatModel chatModel, ChatClient.Builder chatClientBuilder) {
        this.chatModel = chatModel;
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Sends a simple message to the LLM and returns the response as a plain string.
     *
     * @param message the user's message
     * @return the LLM's response content
     */
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * Sends a message to the LLM and returns the full {@link ChatResponse} object,
     * which includes metadata such as token usage and finish reason.
     *
     * @param message the user's message
     * @return the full ChatResponse from the LLM
     */
    public ChatResponse chatWithFullResponse(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.call(prompt);
    }

    /**
     * Streams the LLM response token by token for a given message.
     *
     * @param message the user's message
     * @return a {@link Flux} of {@link ChatResponse} chunks
     */
    public Flux<ChatResponse> streamChat(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

    /**
     * Uses a named {@link PromptTemplate} to compose a message from a subject,
     * then sends it to the LLM.
     *
     * @param subject the topic to ask the LLM about
     * @return the LLM's response content
     */
    public String chatWithTemplate(String subject) {
        PromptTemplate promptTemplate = new PromptTemplate(
                "Tell me about {subject} in 3 sentences."
        );
        Prompt prompt = promptTemplate.create(Map.of("subject", subject));
        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
