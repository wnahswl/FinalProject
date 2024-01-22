package edu.pnu.dto;

import edu.pnu.domain.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
	private Long id; // 기본키
	private String name; // 유저이름
	private String email; // 유저의 구글 이메일
	private String picture;
	// JPA로 데이터베이스 저장할 때 Enum은 기본으로 int로 된 숫자
	@Enumerated(EnumType.STRING)
	private Role role; // 유저 권한
	private String provider;

}
