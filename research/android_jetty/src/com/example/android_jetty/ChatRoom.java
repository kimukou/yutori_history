package com.example.android_jetty;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

public class ChatRoom {
    private int idcounter = 0;
    private Map<ChatWebSocket, Integer> users = new ConcurrentHashMap<ChatWebSocket, Integer>();

    // WebSocketコネクションを登録する
    // ついでに適当なIDを割り当てる
    public void join(ChatWebSocket ws) {
        if (users.containsKey(ws))
            return;
        idcounter++;
        users.put(ws, idcounter);
        sendAll("login " + idcounter);
        System.out.println("login " + idcounter);
    }

    // 登録されていたコネクションを取り除く
    public void leave(ChatWebSocket ws) {
        int id = users.get(ws);
        users.remove(ws);
        sendAll("logoff " + id);
        System.out.println("logoff " + id);
    }

    // WebSocketからメッセージが届いたら付加情報を付けて全コネクションに流す
    public void onMessage(ChatWebSocket ws, String msg) {
        int id = users.get(ws);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msg = id + ": " + msg + " " + fmt.format(new Date());
        sendAll(msg);
        System.out.println(msg);
    }

    // 抱えてる全コネクションに流す
    public void sendAll(String msg) {
        for (ChatWebSocket socket : users.keySet()) {
            try {
                socket.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
