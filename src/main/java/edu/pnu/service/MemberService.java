package edu.pnu.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.dom4j.IllegalAddException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.persistence.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {

	private Set<String> onlineUsers = new HashSet<>();
	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;

	}

	public Set<String> getOnlineUsers() {
		return onlineUsers;
	}

	public void userConnected(String username) {
		log.info("connected User : {} ", username);
		onlineUsers.add(username);
	}

	public void userDisconnected(String username) {
		log.info("disconnected User : {} ", username);
		onlineUsers.remove(username);
	}

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public void updateRole(String provider, String id, Role role) throws IllegalAccessException {
		Optional<Member> optMember = memberRepository.findByEmailAndProvider(id, provider);
		if (optMember.isPresent()) {
			Member member = optMember.get();
			member.setRole(role);
			memberRepository.save(member);
		} else if (optMember.isEmpty()) {
			throw new IllegalAccessException("해당 사용자가 존재하지 않습니다.");
		}

	}

	public void deleteMember(String provider, String id) {
		Optional<Member> optMember = memberRepository.findByEmailAndProvider(id, provider);
		if (optMember.isPresent()) {
			Member member = optMember.get();
			memberRepository.delete(member);
		} else if (optMember.isEmpty()) {
			throw new IllegalAddException("해당 사용자가 존재하지 않습니다 .");
		}
	}
	

}
