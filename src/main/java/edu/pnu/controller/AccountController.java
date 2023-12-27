package edu.pnu.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.config.Constant.SocialLoginType;
import edu.pnu.service.NaverService;
import jakarta.servlet.http.Cookie;
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

	@GetMapping("/auth/{socialLoginType}")
	public ResponseEntity<?> socialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath,
			HttpSession session) throws IOException {
		SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
		naverService.request(socialLoginType, session);
		return ResponseEntity.ok("로그인 페이지로 이동");
	}

	@GetMapping("auth/naver/callback")
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
		log.info("name : {} " , username);
		// 세션에 데이터 저장
		session.setAttribute("memberInfo", memberInfo);
		log.info("getSession '{}' added to session " , session.getAttribute("memberInfo"));
		Cookie memberName = new Cookie("memberInfo", username);
		response.addCookie(memberName);
		log.info("check cookie : {} " , memberName);
		
//		response.sendRedirect("https://www.naver.com");
		
		return ResponseEntity.ok(memberInfo);
	}
}
