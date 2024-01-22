package edu.pnu.domain;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "PredictLogData")
public class PredictLogDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//유저이름
	private String name;
	
	//배수지코드
	private String poolCode;
	
	//예측시간(year,month,datOfMonth,hour,minute)
	private LocalDateTime predictTime;
	
	//현재시간
	private Date presentTime;

}
