package edu.pnu.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoginHandler implements AuthenticationSuccessHandler {

	private final OAuth2AuthorizedClientService authorizedClientService;


    public CustomLoginHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
			String name = user.getAttribute("name");
			log.info("name : {} ", name);
			OAuth2AuthorizedClient client = authorizedClientService
					.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

			if (client != null) {
				HttpSession session = request.getSession();
				session.setAttribute("token", client.getAccessToken().getTokenValue());
				

				

//				String destination = "/user/" + username + "/queue/notifications"; //유저별 큐 설정				
//                webSocketSessionService.connectToWebSocket(username, destination);
//				session.setAttribute("userInfo", username);
//				response.sendRedirect("http://10.125.121.217:3000");
			}
		}

//         토큰이 없는 경우, 기본적인 리다이렉션
	}
}
