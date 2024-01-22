package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.PredictLogDomain;

public interface PredictLogRepository extends JpaRepository<PredictLogDomain, Long>{

}
