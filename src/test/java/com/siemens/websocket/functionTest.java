package com.siemens.websocket;

import com.siemens.websocket.enums.WebSocketStatus;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class functionTest {

    @Test
    public void stringTest() {

        String s = "aaaa#gggghh";
        String[] s1 = s.split("#");
        for (String it : s.split("#")) {
            System.out.println(it);
        }
    }

    @Test
    public void test2() {

        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> webSocketTopicMap = new ConcurrentHashMap();
        ConcurrentHashMap<String, String> topicMap = new ConcurrentHashMap<>();
        webSocketTopicMap.put("wb1", topicMap);
        if (topicMap.containsKey("getdata")) {

        }
    }

    @Test
    public void test3() {

        Enum status = WebSocketStatus.FAILED;
        System.out.println(status.toString());
        //System.out.println();
    }
}
