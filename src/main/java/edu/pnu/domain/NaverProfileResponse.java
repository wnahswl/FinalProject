package edu.pnu.domain;

import edu.pnu.config.auth.dto.NaverDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverProfileResponse {
	private String resultcode;
	private String message;
	private NaverDTO response;

	public NaverProfileResponse() {
	}

	public String getResultcode() {
		return resultcode;
	}
}
