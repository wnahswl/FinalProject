package edu.pnu.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	
	ROLE_GUEST("ROLE_GUEST", "손님"),
	ROLE_USER("ROLE_USER", "일반사용자"),
	ROLE_ADMIN("ROLE_ADMIN", "관리자");
	
	private final String key;
	private final String title;

}
