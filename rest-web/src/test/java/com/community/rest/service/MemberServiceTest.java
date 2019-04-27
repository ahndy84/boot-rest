package com.community.rest.service;

import com.community.rest.domain.Member;
import com.community.rest.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MemberServiceTest {
	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Test
	public void 회원가입() throws Exception {
		// given
		Member member = new Member();
		member.setName("kim1");

		// when
		Long saveId = memberService.join(member);
		// then
		assertThat(member, is(memberRepository.findOne(saveId)));
	}

	@Test(expected = IllegalStateException.class)
	public void 중복_회원_예외() throws Exception {
		// given
		Member member1 = new Member();
		member1.setName("kim2");
		Member member2 = new Member();
		member2.setName("kim3");

		// when
		memberService.join(member1);
		memberService.join(member2);

		//then
		fail("예외가 발생해야 한다.");
	}
}
