package edu.pnu.domain.reservior;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "A")
public class AReservoir {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	private LocalDateTime dateTime;
	//수위1
	private Double data1;
	//수위2
	private Double data2;
	//유입유량
	private Double data3;
	//유출유량
	private Double data4;
	
//	private Double waterRate; 			//수위
//	private Double inletVelveRate;		//유입밸브 개도율
//	private Double instantlyInflowRate;	//유입유량 순시
//	private Double outletVelveRate;		//유출밸브 개도율
//	private Double instantlyOutflowRate;//유출유량 순시
	
}
