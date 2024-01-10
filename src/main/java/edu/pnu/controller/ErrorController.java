package edu.pnu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
		// 에러 처리 로직 추가
		return "error"; // 에러 페이지의 뷰 이름
	}

	public String getErrorPath() {
		return "/error";
	}
}
