package edu.pnu.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.config.auth.dto.SessionUser;
import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {


	private final MemberService memberService;

	@GetMapping("/onlineUsers")
	public ResponseEntity<Set<String>> getOnlineUsers() {
		Set<String> onlineUsers = memberService.getOnlineUsers();
		return ResponseEntity.ok(onlineUsers);
	}

	// 현재 사용자 출력
	@GetMapping("/getuser")
	public String getUser(@AuthenticationPrincipal SessionUser sessionUser, HttpSession session)
			throws IllegalAccessException {
		sessionUser = (SessionUser) session.getAttribute("user");
		if (sessionUser != null) {
			String username = sessionUser.getName();
			log.info("현재 로그인한 사용자 호출");
			return username;
		} else {
			throw new IllegalAccessException("사용자 정보가 없습니다");
		}
	}

	@GetMapping("/check-login-status")
	public ResponseEntity<?> checkLoginStatus(@AuthenticationPrincipal OAuth2User oAuth2User) {

		if (oAuth2User != null) {
			Collection<? extends GrantedAuthority> role = oAuth2User.getAuthorities();
			log.info("유저의 권한 : {} ", role);
			// 사용자는 로그인 상태
			return ResponseEntity.ok("User is logged in");
		} else {
			// 사용자는 로그아웃 상태
			return ResponseEntity.ok("User is not logged in");
		}
	}

	// 모든 회원 출력 메소드
	@GetMapping("/members")
	public List<Member> getAllMembers() {
		return memberService.getAllMembers();
	}

	// 회원 권한 변경 URL에 직접적으로 추가
	@PostMapping("/updateRole/{provider}/{id}")
	public ResponseEntity<?> updateRole(@PathVariable String provider, @PathVariable String id, @RequestParam Role role)
			throws IllegalAccessException {
		memberService.updateRole(provider, id, role);
		return ResponseEntity.ok("Change role successfully");
	}

	@DeleteMapping("/delete/{provider}/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable String provider, @PathVariable String id)
			throws IllegalAccessError {
		try {
			memberService.deleteMember(provider, id);
			return ResponseEntity.ok("Delete Member Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
