package kr.toyauction.domain.member.service;

import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.domain.member.dto.MemberPostRequest;
import kr.toyauction.domain.member.entity.Member;
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
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public Member registerMember(@NonNull final MemberPostRequest memberPostRequest) {

		Member member = Member.builder()
				.username(memberPostRequest.getUsername())
				.build();
		member.validation();
		Member saved = memberRepository.save(member);

		// AlertPublishEvent 는 global 에 위치시켜 여러 도메인에서 호출할 수 있게 한다.
		applicationEventPublisher.publishEvent(new AlertPublishEvent(saved.getId(), "ALERT_REGISTER_MEMBER"));
		return saved;
	}

	@Transactional(readOnly = true)
	public Member getMember(Long memberId) {
		return this.memberRepository.findById(memberId)
				.orElseThrow(() -> new DomainNotFoundException(memberId));
	}

	@Transactional(readOnly = true)
	public Page<Member> pageMember(@NonNull final Pageable pageable, final MemberGetRequest memberGetRequest) {
		return this.memberRepository.page(pageable, memberGetRequest);
	}
}
