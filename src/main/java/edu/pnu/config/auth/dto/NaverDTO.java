package edu.pnu.config.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaverDTO {
	private String id;
	private String email;
	private String name;
}
