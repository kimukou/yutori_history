package com.example.android_jetty;

import org.eclipse.jetty.websocket.WebSocket;

public class ChatWebSocketFactory {
    private ChatRoom chatroom;
    public ChatWebSocketFactory(ChatRoom chatroom) {
        if (chatroom == null)
            throw new IllegalArgumentException();
        this.chatroom = chatroom;
    }

    public WebSocket getNewWebSocket() {
        return new ChatWebSocket(chatroom);
    }
}
