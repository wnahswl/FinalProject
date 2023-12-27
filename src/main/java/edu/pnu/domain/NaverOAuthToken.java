package edu.pnu.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverOAuthToken {
	private String access_token;
	private String refresh_token;
	private String token_type;
	private String expires_in;

	public NaverOAuthToken() {
	}
}
