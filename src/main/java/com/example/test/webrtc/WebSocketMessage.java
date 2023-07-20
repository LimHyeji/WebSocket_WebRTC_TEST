package com.example.test.webrtc;

/*
Client와 주고 받을 메시지
SignalHandler에서 사용
 */

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class WebSocketMessage {
    private String from;//메시지 송신자
    private String type;//메시지 타입
    private String data;//메시지 내용
    private Object candidate;//채팅방 참여자
    private Object sdp;//프로토콜

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WebSocketMessage message = (WebSocketMessage) o;
        return Objects.equals(getFrom(), message.getFrom()) &&
                Objects.equals(getType(), message.getType()) &&
                Objects.equals(getData(), message.getData()) &&
                Objects.equals(getCandidate(), message.getCandidate()) &&
                Objects.equals(getSdp(), message.getSdp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getType(), getData(), getCandidate(), getSdp());
    }
}
