package edu.pnu.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictLogDto {
	
	//유저이름
	private String name;
	//배수지코드
	private String poolCode;
	//예측시간
	private LocalDateTime predictTime;
	//현재시간
	private Date presentTime;
}
