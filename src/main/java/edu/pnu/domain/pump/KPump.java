package edu.pnu.domain.pump;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "KPump")
public class KPump {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double velveOpeningRate;// 벨브 개도율
	private Double flowRate;		// 유량(초음파 실선)
	private Double pressure;		// 압력

}
