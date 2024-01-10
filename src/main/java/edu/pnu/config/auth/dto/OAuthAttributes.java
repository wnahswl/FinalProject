package edu.pnu.config.auth.dto;

import java.util.Map;

import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OAuthAttributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;
	private String provider;
	private Role role;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
			String picture, String provider) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.provider = provider;

	}

	// Oauth2user에서 반환하는 정보는 Map이기 때문에 값 하나하나를 변환
	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
			Map<String, Object> attributes)   {
		if(registrationId.equals("naver")) {
		return ofNaver("id", attributes);
		}else if(registrationId.equals("kakao")) {
			return ofKakao("id", attributes);
		}else if(registrationId.equals("google")) {
			return ofGoogle("id", attributes);
		}else {
			return null;
		}
	}

	private static OAuthAttributes ofGoogle(String string, Map<String, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		//kakao_account안에 profile이라는 JSON 객체가 있다 (nickname, profile_image)
		Map<String, Object> kakao = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>) kakao.get("profile");
		return OAuthAttributes.builder().name((String)kakaoProfile.get("nickname")).email((String)kakaoProfile.get("email"))
				.picture((String)kakaoProfile.get("profile_image")).attributes(kakaoProfile)
				.nameAttributeKey(userNameAttributeName).build();
	}

	// Naver
	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuthAttributes.builder().name((String) response.get("name")).email((String) response.get("email"))
				.picture((String) response.get("profile_image")).provider("provider").attributes(response)
				.nameAttributeKey(userNameAttributeName).build();
	}

	// 엔티티를 생성하는 시점은 처음 가입할 때,기본 권한을 Guest로 준다
	public Member toEntity() {
		return Member.builder().name(name).email(email).picture(picture).role(Role.GUEST).build();
	}
}
