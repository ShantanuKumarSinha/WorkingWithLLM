package com.shantanu.workingwithllm.controller;

import com.shantanu.workingwithllm.service.ChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Simple chat endpoint that accepts a message and returns the LLM's plain-text reply.
     * <p>
     * Example: GET /api/chat?message=Hello
     *
     * @param message the user's message (defaults to "Tell me a joke")
     * @return the LLM's response as a plain string
     */
    @GetMapping
    public String chat(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatService.chat(message);
    }

    /**
     * Chat endpoint that returns the full {@link ChatResponse}, including metadata.
     * <p>
     * Example: GET /api/chat/full?message=Hello
     *
     * @param message the user's message (defaults to "Tell me a joke")
     * @return the full ChatResponse object as JSON
     */
    @GetMapping("/full")
    public ChatResponse chatFull(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatService.chatWithFullResponse(message);
    }

    /**
     * Streaming chat endpoint that returns LLM output token by token using
     * Server-Sent Events (SSE).
     * <p>
     * Example: GET /api/chat/stream?message=Hello
     *
     * @param message the user's message (defaults to "Tell me a joke")
     * @return a server-sent event stream of response chunks
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam(defaultValue = "Tell me a joke") String message) {
        return chatService.streamChat(message)
                .map(response -> response.getResult().getOutput().getContent());
    }

    /**
     * Template-based chat endpoint that asks the LLM about a given subject.
     * <p>
     * Example: GET /api/chat/template?subject=Spring%20AI
     *
     * @param subject the topic to ask the LLM about (defaults to "Spring AI")
     * @return the LLM's response as a plain string
     */
    @GetMapping("/template")
    public String chatWithTemplate(@RequestParam(defaultValue = "Spring AI") String subject) {
        return chatService.chatWithTemplate(subject);
    }
}
