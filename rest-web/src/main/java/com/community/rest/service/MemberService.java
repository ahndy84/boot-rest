package com.community.rest.service;

import com.community.rest.domain.Member;
import com.community.rest.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	/**
	 * 회원가입
	 * @param member
	 * @return
	 */
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	/**
	 * 회원중복검증
	 * @param member
	 */
	private void validateDuplicateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());

		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	/**
	 * 전체회원조회
	 * @return
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	/**
	 * 회원조회
	 * @param memberId
	 * @return
	 */
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}
