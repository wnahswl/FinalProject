package edu.pnu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {

	@GetMapping("/api/auth/check")
	public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Boolean isLoggedIn = session != null && session.getAttribute("token") != null;

		Map<String, Object> response = new HashMap<>();
		response.put("isLoggedIn", isLoggedIn);
		if (isLoggedIn) {
			String userInfo = (String) session.getAttribute("userInfo");
			response.put("userInfo", userInfo);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/auth/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
