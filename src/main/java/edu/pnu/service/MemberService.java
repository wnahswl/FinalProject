package edu.pnu.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.dto.MemberDto;
import edu.pnu.persistence.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {
	private Set<String> activeUsers = new HashSet<>();
	private final MemberRepository memberRepository;
	private final SessionRegistry sessionRegistry;

	public MemberService(SessionRegistry sessionRegistry, MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
		this.sessionRegistry = sessionRegistry;
	}

	public Set<String> getActiveUsers() {
		return activeUsers;
	}

	public void addUser(String username) {
		activeUsers.add(username);
	}

	public void removeUser(String username) {
		activeUsers.remove(username);
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

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public void updateRole(String provider, String id,Role role ) throws IllegalAccessException {
		Optional<Member> optMember = memberRepository.findByEmailAndProvider(id, provider);
		if (optMember.isPresent()) {
			Member member = optMember.get();
			member.setRole(role);
			memberRepository.save(member);
		} else if (optMember.isEmpty()) {
			throw new IllegalAccessException("해당 사용자가 존재하지 않습니다.");
		}
		
	}

}
