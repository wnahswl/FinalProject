package edu.pnu.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.config.Constant.SocialLoginType;
import edu.pnu.config.auth.dto.SessionUser;
import edu.pnu.service.ActiveUserStore;
import edu.pnu.service.KakaoService;
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
	private final SessionRegistry registry;
	private final ActiveUserStore activeUserStore;

	private String getUsernameFromSession(@AuthenticationPrincipal SessionUser sessionUser, HttpSession session) {
		sessionUser = (SessionUser) session.getAttribute("user");
		return sessionUser.getName();
	}

	@GetMapping("/activeUser")
	public List<String> getLoggedInUsers() {
		return activeUserStore.getLoggedInUsers();
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

//		response.sendRedirect("https://www.naver.com");

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

	// logout에 문제가 생김
	// 캐시 쿠기 제거하고 나서 됐음
	@GetMapping("/logout1")
	public ResponseEntity<?> logout(@AuthenticationPrincipal SessionUser sessionUser,HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		log.info("로그아웃 시도 ");
		if (session == null) {
			return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
		} else if(session !=null){
			String username = getUsernameFromSession(sessionUser, session);
			registry.removeSessionInformation(session.getId());
			session.invalidate();
		}
		// 세션 전부 무효화
		return ResponseEntity.ok("토큰삭제 성공");
	}

	@GetMapping("/check-login-status")
	public ResponseEntity<?> checkLoginStatus(@AuthenticationPrincipal OAuth2User oAuth2User) {
		if (oAuth2User != null) {
			// 사용자는 로그인 상태
			return ResponseEntity.ok("User is logged in");
		} else {
			// 사용자는 로그아웃 상태
			return ResponseEntity.ok("User is not logged in");
		}
	}

}
