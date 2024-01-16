package edu.pnu.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Reservoir;

public interface ARepository extends JpaRepository<Reservoir, Long>{

	@Query("SELECT a FROM Reservoir a WHERE a.dateTime < :date ORDER BY a.dateTime DESC")
	List<Reservoir> findBeforeDate(LocalDateTime date, Pageable pageable);

	@Query("SELECT a FROM Reservoir a WHERE a.dateTime >= :date ORDER BY a.dateTime")
	List<Reservoir> findFromDate(LocalDateTime date, Pageable pageable);

}
