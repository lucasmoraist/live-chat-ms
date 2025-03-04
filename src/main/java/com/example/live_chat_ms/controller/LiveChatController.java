package com.example.live_chat_ms.controller;

import com.example.live_chat_ms.dto.ChatRequest;
import com.example.live_chat_ms.dto.ChatResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class LiveChatController {

    @MessageMapping("/new-message")
    @SendTo("/topics/livechat")
    public ChatResponse newMessage(ChatRequest request) {
        return new ChatResponse(HtmlUtils.htmlEscape(request.user() + ": " + request.message()));
    }

}
