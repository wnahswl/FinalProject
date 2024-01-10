package edu.pnu.weboscket;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUserUpdater {

    private final SessionRegistry sessionRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketUserUpdater(SessionRegistry sessionRegistry, SimpMessagingTemplate messagingTemplate) {
        this.sessionRegistry = sessionRegistry;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void updateConnectedUsers() {
        List<String> connectedUsers = sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof Authentication)
                .map(principal -> ((Authentication) principal).getName())
                .collect(Collectors.toList());

        // WebSocket으로 연결된 사용자 목록을 클라이언트에게 전송
        messagingTemplate.convertAndSend("/topic/connected-users", connectedUsers);
    }
}
