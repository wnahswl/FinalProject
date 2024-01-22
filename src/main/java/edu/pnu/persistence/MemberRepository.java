package edu.pnu.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	public Optional<Member> findByname(String name);

	public Optional<Member> findByEmailAndProvider(String email, String provider);

	public Optional<Member> findByEmail(String email);

}
