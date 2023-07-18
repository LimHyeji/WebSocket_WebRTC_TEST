package com.example.test;

import lombok.Data;

@Data
//@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode 한번에 정의
public class ChatMessage {
    private String name;
    private String message;
}
