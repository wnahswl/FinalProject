package edu.pnu.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import edu.pnu.config.auth.dto.OAuthAttributes;
import edu.pnu.config.auth.dto.SessionUser;
import edu.pnu.domain.Member;
import edu.pnu.persistence.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final HttpSession session;
	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("login 성공");
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		String accessToken = userRequest.getAccessToken().getTokenValue();
		session.setAttribute("accessToken", accessToken);
		log.info("토큰 값 : {}",session.getAttribute("accessToken"));
		// Social 구분
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
				oAuth2User.getAttributes());
		Member member = saveOrUpdate(attributes,registrationId);
		
		//세션에 사용자 정보 저장
		member.setOnline(true);
		session.setAttribute("user", new SessionUser(member));

		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
				attributes.getAttributes(), attributes.getNameAttributeKey());
	}

	private Member saveOrUpdate(OAuthAttributes attributes,String registrationId) {
		Member member = memberRepository.findByEmailAndProvider(attributes.getEmail(), attributes.getProvider())
				.map(entity -> entity.update(attributes.getName(),attributes.getPicture(),attributes.getRole()))
				.orElse(attributes.toEntity(registrationId));
		memberRepository.save(member);
		return member;
	}

}
