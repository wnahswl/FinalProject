package edu.pnu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본키

	@Column(nullable = false)
	private String name; // 유저이름

	@Column(nullable = false)
	private String email; // 유저의 구글 이메일

	@Column
	private String picture;

	// JPA로 데이터베이스 저장할 때 Enum은 기본으로 int로 된 숫자
	@Enumerated(EnumType.STRING)
	private Role role; // 유저 권한
	
	private String provider;
	
	//유저의 접속상태를 확인하는 필드
	@Transient
	private boolean isOnline;
	
	@Builder
	public Member(String name, String email, String picture, Role role,String provider) {
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.role = role;
		this.provider = provider;
		
	}

	public Member update(String name, String picture,Role role) {
		this.name = name;
		this.picture = picture;
		if(this.role == Role.ROLE_GUEST)
			this.role = Role.ROLE_USER;
		return this;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	
}
