package edu.pnu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import edu.pnu.domain.chat.ChatDto;
import edu.pnu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	
	private final Map<String,Long> map = new HashMap<>();
	
	private String generateChatRoomId(String user1, String user2) {
		// 간단한 로직으로 채팅 방 식별자 생성
		return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
	}
	
	@MessageMapping("/chat/create/{friendId}")
    public void createChatRoom(@DestinationVariable String friendId,@AuthenticationPrincipal OAuth2User oAuth2User) {
        // 채팅 방 생성 및 채팅 방 식별자 생성 로직
        String chatRoomId = generateChatRoomId(oAuth2User.getAttribute("name"), friendId);
        String username = oAuth2User.getAttribute("name");
        // 채팅 방에 참가한 사용자들에게 알림
 
    }
	
	@MessageMapping("/chat/{rommId}/sendMessage")
	@SendTo("/topic/chat/{roomId}")
	public ChatDto sendMessage(@Payload ChatDto chatDto,@DestinationVariable String roomId) {
		return chatDto;
	}
	
	@MessageMapping("/chat/{roomId}/addUser")
	@SendTo("/topic/chat/{roomId}")
	public ChatDto addUser(@Payload ChatDto chatDto,SimpMessageHeaderAccessor headerAccessor,@DestinationVariable String roomId) {
		headerAccessor.getSessionAttributes().put("username", chatDto.getSender());
		return chatDto;
	}
	
	@MessageMapping("/chat/enter")
	public void enter(ChatDto chatDto) {
		List<String> liveUser = new ArrayList<>();	
	}
	

}
