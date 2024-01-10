package edu.pnu.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.reservior.AReservoir;

public interface ARepository extends JpaRepository<AReservoir, Long>{

	@Query("SELECT a FROM AReservoir a WHERE a.dateTime < :date ORDER BY a.dateTime DESC")
	List<AReservoir> findBeforeDate(LocalDateTime date, Pageable pageable);

	@Query("SELECT a FROM AReservoir a WHERE a.dateTime >= :date ORDER BY a.dateTime")
	List<AReservoir> findFromDate(LocalDateTime date, Pageable pageable);

}
