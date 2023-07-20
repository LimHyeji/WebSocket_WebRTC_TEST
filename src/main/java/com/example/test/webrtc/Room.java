package com.example.test.webrtc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* 채팅방 : id, Clients로 구성 */
@Getter
public class Room {
    @NotNull
    private final Long id;//방번호

    /*
    사용자 이름으로 socket.
    WebSocketSession : Spring에서 Websocket Connection이 맺어진 세션. 고수준 소켓.
     */
    private final Map<String, WebSocketSession> clients=new HashMap<>();

    public Room(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Room room = (Room) o;
        return Objects.equals(getId(), room.getId()) &&
                Objects.equals(getClients(), room.getClients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClients());
    }

}
