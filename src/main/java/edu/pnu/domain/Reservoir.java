package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservoir {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	private LocalDateTime dateTime;
	private Double data1;// 수위1
	private Double data2;// 수위2
	private Double data3;// 유입유량
	private Double data4; // 유출유량
	
	
	//B,D,E,F,W
	private Double waterRate; 			//수위
	//C,J,G,H,I,L,U,S,Q,N,M
	private Double inletVelveRate;		//유입밸브 개도율
	private Double outletVelveRate;		//유출밸브 개도율
	//V
	private Double inletMainVelveRate;	//유입밸브 메인 개도율
	private Double outletMainVelveRate;	//유출밸브 메인 개도율
	//T
	private Double localInFlowRate;		//유입유량(지자체)
	//P
	private Double localOutFlowRate;	//유출유량(지자체)
	//O
	private Double wideAreaFlowRate;		//광역유량
	
	
//	private Double instantlyInflowRate;	//유입유량 순시
//	private Double instantlyOutflowRate;//유출유량 순시

}
