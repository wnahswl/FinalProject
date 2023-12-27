package edu.pnu.config.auth.dto;

import java.io.Serializable;

import edu.pnu.domain.Member;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable{
	//세션에 사용자 정보를 저장하기 위한 DTO클래스

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	private String picture;
	
	public SessionUser(Member member) {
		this.name = member.getName();
		this.email = member.getEmail();
		this.picture = member.getPicture();
	}

}
