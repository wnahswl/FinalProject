package edu.pnu.persistence;

import jakarta.servlet.http.HttpSession;

public interface SocialOauth {
//	각 소셜 로그인 페이지로 redirect할 URL build


//	String getOauthRedirectURL(HttpServletRequest request);
	String getOauthRedirectURL(HttpSession session);

	
}
