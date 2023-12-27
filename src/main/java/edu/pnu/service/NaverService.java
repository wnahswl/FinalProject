package edu.pnu.service;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.config.Constant;
import edu.pnu.persistence.SocialOauth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService implements SocialOauth {

	private final HttpServletResponse response;
	private String NAVER_SNS_LOGIN_URL = "https://nid.naver.com/oauth2.0/authorize";

	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String NAVER_SNS_CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
	private String NAVER_SNS_CALLBACK_URL;

	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String NAVER_SNS_CLIENT_SECRET;

	@Value("${spring.security.oauth2.client.provider.naver.token-uri}")
	private String NAVER_SNS_TOKEN_URL;

	@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
	private String NAVER_SNS_PROFILE;

	// 세션 유지 및 위조 방지용 토큰 생성
	public static String generateState() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	// 네이버로그인 URL 반환하는 메소드
	@Override
	public String getOauthNaverRedirectURL(HttpSession session) {
		Map<String, Object> params = new HashMap<>();
		// 상태 토큰으로 사용할 랜덤 문자열 생성
		String state = generateState();
		// session에 난수 값 저장
		params.put("client_id", NAVER_SNS_CLIENT_ID);
		params.put("redirect_uri", NAVER_SNS_CALLBACK_URL);
		params.put("response_type", "code");
		params.put("state", state);
		session.setAttribute("state", state);

		// parameter를 형식에 맞춰주는 함수
		String parameterString = params.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue())
				.collect(Collectors.joining("&"));
		String redirectURL = NAVER_SNS_LOGIN_URL + "?" + parameterString;
		System.out.println("redirectURL = " + redirectURL);

		return redirectURL;
	}
	
	@Override
	public String getOauthGoogleRedirectURL(HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOauthKakaoRedirectURL(HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	public void request(Constant.SocialLoginType socialLoginType, HttpSession session) throws IOException {
		String redirectURL;
		switch (socialLoginType) {
		// kakao, google은 추후에 추가 예정
		case NAVER:
			redirectURL = getOauthNaverRedirectURL(session);
			break;
		case KAKAO:
			
		case GOOGLE:
			
		default:
			throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
		}
		response.sendRedirect(redirectURL);
	}

	public Map<String, Object> redirect(String code, String state, HttpServletRequest request)
			throws JsonProcessingException {
		// CSRF 방지를 위한 상태 토큰 검증
		// 세션 또는 별도의 저장 공간에 저장된 상태 토큰과 콜백으로 전달받은 state 파라미터의 값이 일치해야 함
		// 콜백에서 전달해준 code, state 값 가져오기
		code = request.getParameter("code");
		state = request.getParameter("state");
		RestTemplate restTemplate = new RestTemplate();

		// 토큰 발급을 위한 URI
		// 접근 토큰 발급 요청
		MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
		tokenParams.add("grant_type", "authorization_code");
		tokenParams.add("client_id", NAVER_SNS_CLIENT_ID);
		tokenParams.add("client_secret", NAVER_SNS_CLIENT_SECRET);
		tokenParams.add("code", code);
		tokenParams.add("state", state);

		HttpHeaders tokenHeaders = new HttpHeaders();
		tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, tokenHeaders);

		ResponseEntity<Map> tokenResponse = restTemplate.exchange(NAVER_SNS_TOKEN_URL, HttpMethod.POST, tokenRequest,
				Map.class);

		if (tokenResponse.getStatusCode().is2xxSuccessful()) {
			// 토큰에 대한 정보 조회
			Map<String, Object> tokenBody = tokenResponse.getBody();
			String accessToken = (String) tokenBody.get("access_token");
			return getUserInfo(accessToken);
		} else {
			throw new ResponseStatusException(tokenResponse.getStatusCode(), "Failed to obtain access token");
		}
	}

	// AccessToken으로 사용자 정보 조회하고 추출
	private Map<String, Object> getUserInfo(String accessToken) {

		RestTemplate restTemplate = new RestTemplate();
		// Authorization 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		log.info("get AccessCode : {} " , accessToken);
		// HTTP GET 요청 설정
		RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(NAVER_SNS_PROFILE));
		// 네이버 API 호출 및 응답 받기
		ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			// Naver는 response안에 사용자 정보가 있음
			Object getBody = responseEntity.getBody().get("response");
			ObjectMapper objectMapper = new ObjectMapper();
			// JSON으로 전환
			Map<String, Object> memberInfoToJson = objectMapper.convertValue(getBody, Map.class);

			return memberInfoToJson;
		} else {
			throw new ResponseStatusException(responseEntity.getStatusCode(), "Failed to get MemberInfo");
		}
	}

	
}
