package edu.pnu.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.pnu.config.Constant.SocialLoginType;
import edu.pnu.persistence.SocialOauth;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService implements SocialOauth {
	private final HttpServletResponse response;

	@Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
	private String KAKAO_SNS_LOGIN_URL;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String KAKAO_SNS_CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String KAKAO_SNS_CALLBACK_URL;

	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String KAKAO_SNS_CLIENT_SECRET;

	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String KAKAO_SNS_TOKEN_URL;

	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String KAKAO_SNS_PROFILE;

	// 세션 유지 및 위조 방지용 토큰 생성
	public static String generateState() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	@Override
	public String getOauthRedirectURL(HttpSession session) {
		Map<String, Object> params = new HashMap<>();
		// 상태 토큰으로 사용할 랜덤 문자열 생성
		String state = generateState();
		// session에 난수 값 저장
		session.setAttribute("state", state);
		params.put("state", state);
		params.put("client_id", KAKAO_SNS_CLIENT_ID);
		params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
		params.put("response_type", "code");

		// parameter를 형식에 맞춰주는 함수
		String parameterString = params.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue())
				.collect(Collectors.joining("&"));
		String redirectURL = KAKAO_SNS_LOGIN_URL + "?" + parameterString;
		log.info("redirectURL : {} ", redirectURL);
		return redirectURL;
	}

	public void request(SocialLoginType socialLoginType, HttpSession session) throws IOException {
		String redirectURL = getOauthRedirectURL(session);
		response.sendRedirect(redirectURL);
	}

}
