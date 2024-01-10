package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.pnu.config.auth.dto.SessionUser;
import edu.pnu.domain.PredictLogDomain;
import edu.pnu.domain.reservior.AReservoir;
import edu.pnu.dto.PredictLogDto;
import edu.pnu.persistence.ARepository;
import edu.pnu.persistence.PredictLogRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PredictLogService {

	private final PredictLogRepository logRepository;
	
	private final ARepository aRepo;
	
	
	private final RestTemplate restTemplate;

	public void logUserPredict(@AuthenticationPrincipal SessionUser sessionUser, HttpSession session, PredictLogDto dto)
			throws IllegalAccessException {
		sessionUser = (SessionUser) session.getAttribute("user");
		if (sessionUser != null) {
			PredictLogDomain domain = new PredictLogDomain();
			domain.setName(sessionUser.getName());
			
			domain.setPoolCode(dto.getPoolCode());
			domain.setPredictTime(dto.getPredictTime());
			
			domain.setPresentTime(new Date());
			logRepository.save(domain);
		} else if (sessionUser == null) {
			throw new IllegalAccessException("사용자 정보가 없습니다. ");
		}
	}
	
	

	public String getValuesAndSendToFlask(String pool, LocalDateTime date) {

		Pageable previousDataPageable = PageRequest.of(0, 144, Sort.by("dateTime").descending());
		Pageable nextDataPageable = PageRequest.of(0, 144, Sort.by("dateTime"));

		List<AReservoir> values = new ArrayList<>();

		Map<String, Object> requestData = new HashMap<>();

		if (pool.equals("A")) {
			List<AReservoir> beforeData = aRepo.findBeforeDate(date, previousDataPageable);

			// 역정렬된 데이터를 오름차순으로 재정렬
			Collections.reverse(beforeData);

			List<AReservoir> fromData = aRepo.findFromDate(date, nextDataPageable);

			values.addAll(beforeData);
			values.addAll(fromData);

			requestData.put("values", values);
		} else {
			return "error";
		}

		// Flask 서버의 URL
		String flaskUrl = "http://10.125.121.208:5000/predict";

		// 전송할 데이터 맵 생성
		requestData.put("pool", pool);

		// Flask 서버로 데이터 전송 후 결과 반환
		return restTemplate.postForObject(flaskUrl, requestData, String.class);
	}
	
	

}