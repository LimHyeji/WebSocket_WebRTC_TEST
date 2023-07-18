package com.example.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatSocketHandler extends TextWebSocketHandler {
    ObjectMapper objectMapper=new ObjectMapper();
    List<WebSocketSession> list= Collections.synchronizedList(new ArrayList<>());

    /* 웹 소켓 연결될 때 호출 */

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("===========접속===========");
        System.out.println("session ID = "+ session.getId());
        System.out.println("session Accept Protocol = "+ session.getAcceptedProtocol());
        System.out.println("session LocalAddress = "+ session.getLocalAddress());
        System.out.println("session RemoteAddress = "+ session.getRemoteAddress());
        System.out.println("session Uri = "+ session.getUri());
        System.out.println("===========접속===========");

        list.add(session);
    }

    /* 메시지 전송 받았을 때 호출 */

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("====메세지 도착====");
        //payload는 전송되는 데이터를 의미
        System.out.println("session Id = "+session.getId()+", 받은 message payload = "+message.getPayload());
        System.out.println("====메세지 끝====");

        System.out.println("session ID = "+ session.getId());
        System.out.println("session Accept Protocol = "+ session.getAcceptedProtocol());
        System.out.println("session LocalAddress = "+ session.getLocalAddress());
        System.out.println("session RemoteAddress = "+ session.getRemoteAddress());
        System.out.println("session Uri = "+ session.getUri());

        ChatMessage chatMessage=new ChatMessage();

    }
}
