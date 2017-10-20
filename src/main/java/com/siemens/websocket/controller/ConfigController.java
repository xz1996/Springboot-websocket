package com.siemens.websocket.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siemens.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Controller
public class ConfigController {

    @Autowired
    private Gson gson;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @PostMapping("/api/setWorkflow")
    public ResponseEntity setWorkflow(@RequestBody String assetInfoString) {

       /*
        1. acquire the Asset name from front-end info.
        2. create a asset in mindsphere according the info.
        3. save asset info in DB.
        4. get asset id by name from DB.
        */

        CopyOnWriteArrayList<WebSocket> webSocketList = WebSocket.getWebSocketList();
        LinkedBlockingQueue<String> msgFromQueue;
        JsonObject jsonObject;
        String content = null;
        try {
            for (WebSocket webSocket : webSocketList) {

                msgFromQueue = webSocket.getMsgFromQueue();
                String msg = msgFromQueue.poll(3, TimeUnit.SECONDS);
                if (msg != null) {
                    jsonObject = new JsonParser().parse(msg).getAsJsonObject();
                    String topic = jsonObject.get("Topic").getAsString();
                    switch (topic) {
                        case "getConfigurationData":
                            content = getConfigurationData();
                            break;

                        case "getAssetData":
                            content = getAssetData();
                            break;
                    }
                    jsonObject.addProperty("Content", content);
                    webSocket.sendMessage(jsonObject.toString());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private String getConfigurationData() {

        return "Configuration data.";
    }

    private String getAssetData() {

        return "Asset Data.";
    }

}
