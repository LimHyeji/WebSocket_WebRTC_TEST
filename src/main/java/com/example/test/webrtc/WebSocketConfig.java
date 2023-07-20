package com.example.test.webrtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket//웹소켓 자동 설정
public class WebSocketConfig implements WebSocketConfigurer {
    /* 추가 설정 */

    @Autowired
    private SignalHandler signalHandler;

    /*
    WebSocketHandler 추가
    구현한 webSocketHandler를 registry에 등록

    1. 클라이언트 접속 시 특정 메소드 호출
    2. 클라이언트 접속 해제 시 특정 메소드 호출
    3. 클라이언트가 메시지 보낼 때 특정 메소드 호출
     */

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /*
        client에서 GET /signal 호출해 서버 정보 취득
        WebSocketHandlerRegistry에 WebSocketHandler 구현체 등록
        등록된 Handler는 특정 endpoing("/signal")로 handshake 완료 후 맺어진 connection 관리
         */

        System.out.println("regiserWebSocketHandlers execute");
        registry.addHandler(signalHandler,"/signal")
                .setAllowedOrigins("*"); // allow all origins <-pub,sub의 sub
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer(){
        System.out.println("createWebSocketContainer execute");
        ServletServerContainerFactoryBean container=new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
