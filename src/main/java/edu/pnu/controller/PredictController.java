package edu.pnu.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.config.auth.dto.SessionUser;
import edu.pnu.dto.PredictLogDto;
import edu.pnu.service.PredictLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PredictController {
	
	private final PredictLogService predictLogService;

	@GetMapping("/predict")
	public String testFlaskService(@RequestParam String pool, @RequestParam LocalDateTime date) {
		return predictLogService.getValuesAndSendToFlask(pool, date);
	}
	
	@PostMapping("/predict")
	ResponseEntity<?> predictLog(@AuthenticationPrincipal SessionUser sessionUser,HttpSession session ,@RequestBody PredictLogDto dto) throws IllegalAccessException{
		sessionUser = (SessionUser) session.getAttribute("user");
		predictLogService.logUserPredict(sessionUser, session, dto);
		return ResponseEntity.ok("ok");
	}

}
