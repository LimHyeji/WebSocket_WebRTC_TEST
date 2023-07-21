package com.example.test.webrtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class SignalHandler extends TextWebSocketHandler {
    @Autowired
    private RoomService roomService;

    private final ObjectMapper objectMapper=new ObjectMapper();
    private Map<String,Room> sessionIdToRoomMap=new HashMap<>();

    // message types, used in signalling:
    // text message
    private static final String MSG_TYPE_TEXT = "text";
    // SDP Offer message
    private static final String MSG_TYPE_OFFER = "offer";
    // SDP Answer message
    private static final String MSG_TYPE_ANSWER = "answer";
    // New ICE Candidate message
    private static final String MSG_TYPE_ICE = "ice";
    // join room data message
    private static final String MSG_TYPE_JOIN = "join";
    // leave room data message
    private static final String MSG_TYPE_LEAVE = "leave";

    /*
    브라우저에서 접속 해제 시 호출
    세션 목록에서 세션 제거
     */
    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status){
        System.out.println("SIGNALHANDLER: afterConnectionClosed");
        System.out.println("[ws] Session has been closed with status "+status);
        sessionIdToRoomMap.remove(session.getId());
    }

    /*
    브라우저와 웹소켓의 핸드셰이크 완료 및 연결/세션 생성 시 호출
    (양쪽에서 접속 해제할 때까지 지속)
    세션 목록에 세션 추가
     */
    @Override
    public void afterConnectionEstablished(final WebSocketSession session){
        // webSocket has been opened, send a message to the client
        // when data field contains 'true' value, the client starts negotiating
        // to establish peer-to-peer connection, otherwise they wait for a counterpart
        System.out.println("SIGNALHANDLER: afterConnectionEstablished");
        sendMessage(session,new WebSocketMessage("Server",MSG_TYPE_JOIN,Boolean.toString(!sessionIdToRoomMap.isEmpty()),null,null));
    }

    private void sendMessage(WebSocketSession session,WebSocketMessage message){
        System.out.println("SIGNALHANDLER: sendMessage");
        try{
            String json=objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /*
    특정 세션이 웹소켓에 메시지 보낼 때마다 호출
    웹소켓에 연결된 모든 세션을 반복해 각 세션에 메시지 전송(전송한 세션 제외)
     */
    @Override
    protected  void handleTextMessage(final WebSocketSession session,final TextMessage textMessage){
        System.out.println("SIGNALHANDLER: handleTextMessage");
        // a message has been received
        try{
            System.out.println(textMessage.getPayload());
            WebSocketMessage message=objectMapper.readValue(textMessage.getPayload(),WebSocketMessage.class);
            System.out.print("[ws] Message of "+message.getType()+" type from "+ message.getFrom()+" received");
            String userName=message.getFrom(); // origin of the message
            Object data=message.getData(); // payload // room id가 들어온다

            Room room;
            switch (message.getType()){
                // text message from client has been received
                case MSG_TYPE_TEXT:
                    System.out.println("[ws] Text message : "+message.getData());
                    // message.data is the text sent by client
                    // process text message if needed
                    break;

                // process signal received from client
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_ICE:
                    Object candidate=message.getCandidate();
                    Object sdp=message.getSdp();
                    System.out.println("[ws] Signal: "+candidate!=null?candidate.toString().substring(0,64):sdp.toString().substring(0,64));

                    Room rm=sessionIdToRoomMap.get(session.getId());
                    if(rm!=null){
                        Map<String,WebSocketSession> clients=roomService.getClients(rm);
                        for(Map.Entry<String,WebSocketSession> client:clients.entrySet()){
                            // send messages to all clients except current user
                            if (!client.getKey().equals(userName)) {
                                // select the same type to resend signal
                                sendMessage(client.getValue(),
                                        new WebSocketMessage(
                                                userName,
                                                message.getType(),
                                                data,
                                                candidate,
                                                sdp));
                            }
                        }
                    }
                    break;

                // identify user and their opponent
                case MSG_TYPE_JOIN:
                    // message.data contains connected room id
                    System.out.println("[ws] "+userName+" has joined Room: #"+message.getData());
                    room = roomService.findRoomByStringId("1")
                            .orElseThrow(() -> new IOException("Invalid room number received!"));
                    // add client to the Room clients list
                    roomService.addClient(room, userName, session);
                    sessionIdToRoomMap.put(session.getId(), room);
                    break;

                case MSG_TYPE_LEAVE:
                    // message data contains connected room id
                    System.out.println("[ws] "+userName+" is going to leave Room: #"+message.getData());
                    // room id taken by session id
                    room = sessionIdToRoomMap.get(session.getId());
                    // remove the client which leaves from the Room clients list
                    Optional<String> client = roomService.getClients(room).entrySet().stream()
                            .filter(entry -> Objects.equals(entry.getValue().getId(), session.getId()))
                            .map(Map.Entry::getKey)
                            .findAny();
                    client.ifPresent(c -> roomService.removeClientByName(room, c));
                    break;

                // something should be wrong with the received message, since it's type is unrecognizable
                default:
                    System.out.println("[ws] Type of the received message "+message.getType()+" is undefined!");
                    // handle this if needed
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

            }
        }

