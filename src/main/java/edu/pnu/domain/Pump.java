package edu.pnu.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pump {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double velveOpeningRate;// 벨브 개도율
	private Double pressure; // 압력
	@Builder.Default
	private Double flowRate = 0.0; // 유량
	// B,E,F,L,M,N
	@Builder.Default
	private Double outPressure = 0.0; // 후단 압력
	@Builder.Default
	private Double inPressure = 0.0; // 전단 압력
	
	@Builder.Default
	@Temporal(TemporalType.TIMESTAMP)
	private Date reportedDate = new Date(); // 보고 일시

}
