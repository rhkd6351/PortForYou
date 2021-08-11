package com.limbae.pfy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/pfy/stomp").setAllowedOriginPatterns("*").withSockJS();
        //웹에서 위 엔드포인트로 핸드쉐이크 진행함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        //메시지를 받을 api의 prefix
        registry.enableSimpleBroker("/sub");
        //위 api를 구독하고 있는 클라이언트에게 메시지를 전달하는 용도
    }
}
