package edu.pnu.domain.reservior;

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
@Table(name = "P")
public class PReservoir {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private Double waterRate; 			//수위
	private Double outFlowRate;			//유출유량
	private Double inletVelveRate;		//유입밸브 개도율
	private Double outletVelveRate;		//유출밸브 개도율
	private Double inFlowRate;			//유입유량
	private Double localInFlowRate;		//유입유량(지자체)
	private Double localOutFlowRate;	//유출유량(지자체)
}
	