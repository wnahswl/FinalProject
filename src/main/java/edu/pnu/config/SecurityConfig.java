package edu.pnu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.pnu.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private final CustomOAuth2UserService service;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		http.csrf(csrf -> csrf.disable());
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.formLogin(frmLogin -> frmLogin.disable());
		http.httpBasic(basic -> basic.disable());
		http.oauth2Login(oauth2Login->oauth2Login.userInfoEndpoint(user->user.userService(service)));

		log.info("filter chain test");
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		// URL패턴을 기반으로 한 Cors 구성 제공
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Cors 구성 객체 생성
		CorsConfiguration config = new CorsConfiguration();
		// 특정 도메인 또는 IP 허용하도록 설정
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedOrigin("http://10.125.121.208:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		config.addExposedHeader("Authorization");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);

		// CorsFilter 생성자가 UrlBasedCorsConfigurationSource를 받도록 변경
		return source;
	}
}
