package kr.toyauction.domain.member.service;

import kr.toyauction.domain.member.dto.MemberPatchRequest;
import kr.toyauction.domain.member.dto.MemberPatchResponse;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.repository.MemberRepository;
import kr.toyauction.global.dto.FieldErrorResponse;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.exception.OverlapException;
import kr.toyauction.global.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	@Transactional(readOnly = true)
	public Member getMemberByUsername(String username) {
		return memberRepository.findByUsername(username)
				.orElseThrow(DomainNotFoundException::new);
	}

	@Transactional
	public MemberPatchResponse patchMember(Long memberId, MemberPatchRequest request){
		Optional<Member> overlapMember = memberRepository.findByUsername(request.getUsername());
		if (overlapMember.isPresent()) throw new OverlapException("username");

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new DomainNotFoundException(memberId));
		member.setUsername(request.getUsername());
		String token = jwtProvider.createToken(member); // 토큰 발행
		return new MemberPatchResponse(token);
	}

}
