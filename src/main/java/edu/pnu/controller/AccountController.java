package edu.pnu.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.config.Constant.SocialLoginType;
import edu.pnu.config.auth.dto.SessionUser;
import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.service.KakaoService;
import edu.pnu.service.MemberService;
import edu.pnu.service.NaverService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

	private final NaverService naverService;
	private final KakaoService kakaoService;
	private final MemberService memberService;

	@GetMapping("/activeUser")
	public List<List<String>> getLoggedInUsers() {
		return memberService.getLoggedInUsers();
	}

	@GetMapping("/oauth/{socialLoginType}")
	public ResponseEntity<?> socialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath,
			HttpSession session) throws IOException {
		SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
		log.info("url 설정");
		switch (socialLoginType) {
		case NAVER:
			naverService.request(socialLoginType, session);
			break;
		case KAKAO:
			kakaoService.request(socialLoginType, session);
			break; // 각 case별로 break 추가
		case GOOGLE:
			log.debug("구글은 아직 개발중 ... ");
			break;
		default:
			throw new IOException("알 수 없는 소셜 로그인 형식 입니다.");
		}
		return ResponseEntity.ok("로그인 페이지로 이동");
	}

	@GetMapping("/login/oauth2/code/naver")
	public ResponseEntity<?> naverGetInfo(@RequestParam(name = "code") String code,
			@RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 세션에 저장해둔 state값 가져오기
		HttpSession session = request.getSession();
		String storedState = (String) session.getAttribute("state");
		// state가 일치하는지 확인
		if (state == null || !state.equals(storedState)) {
			throw new IllegalArgumentException("Invalid state");
		}
		// 사용자 정보 조회
		// 이 memberInfo에 response로 담겨있음
		Map<String, Object> memberInfo = naverService.redirect(code, state, request);
		String username = (String) memberInfo.get("name");
		log.info("name : {} ", username);
		// 세션에 데이터 저장
		session.setAttribute("memberInfo", memberInfo);
		log.info("getSession '{}' added to session ", session.getAttribute("memberInfo"));



		return ResponseEntity.ok(memberInfo);
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
			log.info("유저의 권한 : {} " , role);
			// 사용자는 로그인 상태
			return ResponseEntity.ok("User is logged in");
		} else {
			// 사용자는 로그아웃 상태
			return ResponseEntity.ok("User is not logged in");
		}
	}
	
	//모든 회원 출력 메소드
	@GetMapping("/members")
	public List<Member> getAllMembers(){
		return memberService.getAllMembers();
	}

	//회원 권한 변경 URL에 직접적으로 추가
	@PostMapping("/updateRole/{provider}/{id}")
	public ResponseEntity<?> updateRole(@PathVariable String provider,@PathVariable String id,@RequestParam Role role) throws IllegalAccessException{
		memberService.updateRole(provider,id,role);
		return ResponseEntity.ok("Change role successfully");
	}
}
