package edu.pnu.weboscket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//topic을 구독한 모든 유저에게 보낼 수 있음
		//queue는 1:1 구독방식으로 일대일 메시지 전달
		registry.enableSimpleBroker("/topic","/queue");
		//특정 유저에게 보낼 수 있음
		registry.setApplicationDestinationPrefixes("/app");
	}

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		log.info("connect server");
		registry.addEndpoint("/ws").setAllowedOrigins("*");
	}
}
