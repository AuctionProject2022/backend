package kr.toyauction.domain.member.service;

import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.domain.member.dto.MemberPostRequest;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.repository.MemberQueryRepository;
import kr.toyauction.domain.member.repository.MemberRepository;
import kr.toyauction.global.event.AlertPublishEvent;
import kr.toyauction.global.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Member getMemberByUsername(String username) {
		Member member = memberRepository.findByUsername(username);
		if (member == null) throw new DomainNotFoundException();
		return member;
	}
}
