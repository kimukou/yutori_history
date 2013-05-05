package com.example.android_jetty;


import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import javax.servlet.http.HttpServletRequest;

public class WSServlet extends WebSocketServlet {
    private static final long serialVersionUID = 8925349860339039372L;
    private ChatWebSocketFactory factory;

    public WSServlet(ChatWebSocketFactory factory) {
        if (factory == null)
            throw new IllegalArgumentException();

        this.factory = factory;
    }

    // WebSocket の URL に接続がきたら WebSocket を返す
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
        System.out.println("doWebSocketConnect");
        return factory.getNewWebSocket();
    }
}
