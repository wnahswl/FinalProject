package edu.pnu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.pnu.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final OAuth2AuthorizedClientService authorizedClientService;
	private final CustomOAuth2UserService service;
	private final CustomeLogoutHandler logoutHandler;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll()).csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.formLogin(frmLogin -> frmLogin.disable()).httpBasic(basic -> basic.disable())
				.sessionManagement(ssmn -> ssmn.maximumSessions(1).sessionRegistry(sessionRegistry()))
				.oauth2Login(oauth2 -> oauth2.loginPage("/login").userInfoEndpoint(user -> user.userService(service))
						.successHandler(new CustomAuthenticationSuccessHandler(authorizedClientService)))
				.logout(oauth -> oauth.logoutUrl("/logout").logoutSuccessHandler(logoutHandler)
						.deleteCookies("JSESSIONID"));
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		// URL패턴을 기반으로 한 Cors 구성 제공
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Cors 구성 객체 생성
		CorsConfiguration config = new CorsConfiguration();
		// 특정 도메인 또는 IP 허용하도록 설정
		
		config.addAllowedOrigin("http://10.125.121.217:3000");//내 ip
//		config.addAllowedOrigin("http://10.125.121.208:3000");
//		config.addAllowedOrigin("/*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		config.addExposedHeader("Authorization");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);

		// CorsFilter 생성자가 UrlBasedCorsConfigurationSource를 받도록 변경
		return source;
	}

	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
		return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
	}
}
