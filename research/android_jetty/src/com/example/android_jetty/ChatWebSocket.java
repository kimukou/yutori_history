package com.example.android_jetty;


import org.eclipse.jetty.websocket.WebSocket;

import java.io.IOException;

public class ChatWebSocket implements WebSocket.OnTextMessage {
    private Connection connection = null;
    private ChatRoom chatroom;

    public ChatWebSocket(ChatRoom chatroom) {
        if (chatroom == null)
            throw new IllegalArgumentException();

        this.chatroom = chatroom;
    }

    // WebSocket が繋がったら chatroom に join する
    @Override
    public void onOpen(Connection con) {
        connection = con;
        chatroom.join(this);
    }

    // WebSocket が切れたら chatroom から leave する
    @Override
    public void onClose(int arg0, String arg1) {
        chatroom.leave(this);
    }

    // メッセージが飛んできたら chatroom にお任せする
    @Override
    public void onMessage(String msg) {
        chatroom.onMessage(this, msg);
    }

    // この接続先にメッセージを投げる
    public void send(String msg) throws IOException {
        connection.sendMessage(msg);
    }
}

