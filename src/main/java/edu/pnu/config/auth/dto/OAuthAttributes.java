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
			Map<String, Object> attributes) {
		return ofNaver("id", attributes);
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
		return Member.builder().name(name).email(email).picture(picture).provider(provider).role(Role.GUEST).build();
	}
}
