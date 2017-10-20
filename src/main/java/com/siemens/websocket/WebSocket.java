package com.siemens.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siemens.websocket.enums.WebSocketStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

@ServerEndpoint("/websocket")
@Component
@Slf4j
public class WebSocket {

    //the total number of websocket connections
    private static CopyOnWriteArrayList<WebSocket> webSocketList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<String> topicList = new CopyOnWriteArrayList<>();
    private LinkedBlockingQueue<String> msgFromQueue = new LinkedBlockingQueue<>();
    //private LinkedBlockingQueue<String> msgToQueue = new LinkedBlockingQueue<>();
    private JsonObject jsonObject;
    private Session session;

    @OnOpen
    public void onOpen(Session session) {

        this.session = session;
        webSocketList.add(this);
        log.info("open session id:" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {

        webSocketList.remove(this);
        log.info("close session id:" + session.getId());
    }

    @OnMessage
    public void onMessage(String msgFromClient, Session session) {

        if (msgFromClient.length() > 0) {

            jsonObject = new JsonParser().parse(msgFromClient).getAsJsonObject();
            String assetID = jsonObject.get("Id").getAsString();
            String topic = jsonObject.get("Topic").getAsString();
            String action = jsonObject.get("Action").getAsString();
            WebSocketStatus status = null;
            if (action.equals("subscribe")) {

                if (!containsTopic(topic)) {

                    topicList.add(topic);
                    try {

                        msgFromQueue.put(msgFromClient);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    status = WebSocketStatus.OK;
                    log.info("id:" + assetID + "with topic:" + topic +" subscribe successfully.");

                } else {
                    status = WebSocketStatus.FAILED;
                    log.info("id:" + assetID + "with topic:" + topic +"subscribe failed, you have subscribed already.");

                }
            } else if (action.equals("unsubscribe")) {

                if (!containsTopic(topic)) {
                    status = WebSocketStatus.FAILED;
                    log.warn("unsubscribe failed, because there is no this topic.");
                } else {

                    topicList.remove(topic);
                    status = WebSocketStatus.FAILED;
                    log.info("unsubscribe successfully!");
                }
            } else { //transfer data
                try {

                    msgFromQueue.put(msgFromClient);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            jsonObject.addProperty("Status", status.getReasonPhrase());
            jsonObject.addProperty("Code", status.getValue());
            this.sendMessage(jsonObject.toString());

        } else {

            log.info("no message!");
        }

        log.info("message session id:" + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error){

        log.error(error.toString());
        log.info("error session id:" + session.getId());
    }

    private boolean containsTopic(String topic) {

        return topicList.contains(topic);
    }

    public void sendMessage(String msgToClient) {

        try {

            this.session.getBasicRemote().sendText(msgToClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getOnlineCount() {

        return  webSocketList.size();
    }

    public static CopyOnWriteArrayList getWebSocketList() {

        return webSocketList;
    }

    public List<String> getTopicList() {

        return topicList;
    }

    public LinkedBlockingQueue<String> getMsgFromQueue() {

        return msgFromQueue;
    }
}
