package edu.pnu.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import edu.pnu.domain.chat.ChatDto;
import edu.pnu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	
	private MemberService activeUserStore;
	
	@MessageMapping("/chat/{id}")
	public void sendMessage(@Payload ChatDto chatDto, @DestinationVariable Integer id) {
		this.messagingTemplate.convertAndSend("/queue/addChatToClient/"+id,chatDto);
	}

	@MessageMapping("/chat")
	@SendTo("/topic/chat")
	public String handleChatMessage(String message) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		// 채팅 메시지를 받아 처리하고 다시 클라이언트에게 전송
		return username + ": " + message;
	}

}
