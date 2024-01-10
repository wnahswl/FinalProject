package edu.pnu.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActiveUserStore {
	private Set<String> activeUsers = new HashSet<>();
	private final SessionRegistry sessionRegistry;

	public Set<String> getActiveUsers() {
		return activeUsers;
	}

	public void addUser(String username) {
		activeUsers.add(username);
	}

	public void removeUser(String username) {
		activeUsers.remove(username);
	}

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

	public ActiveUserStore(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public List<String> getLoggedInUsers() {
		return sessionRegistry.getAllPrincipals().stream().filter(principal -> principal instanceof OAuth2User)
				.map(principal -> {
					OAuth2User oAuth2User = (OAuth2User) principal;
					String username = oAuth2User.getAttribute("name");
					List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, true);
					List<String> sessionIds = sessions.stream().map(SessionInformation::getSessionId)
							.collect(Collectors.toList());
					// 여기서 필요에 따라 사용자의 세션 ID 목록을 추가로 처리할 수 있습니다.
					log.info("현재 접속중인 모든 사용자 : {}", username);
					return username;
				}).collect(Collectors.toList());
	}

	public void logoutUser(String username) {
		sessionRegistry.getAllSessions(SecurityContextHolder.getContext().getAuthentication(), false)
				.forEach(sessionInformation -> sessionInformation.expireNow());
	}

}
