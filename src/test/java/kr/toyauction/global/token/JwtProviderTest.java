package kr.toyauction.global.token;

import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.entity.Platform;
import kr.toyauction.domain.member.entity.Role;
import kr.toyauction.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JwtProviderTest {

	@Autowired JwtProvider jwtProvider;
	@Autowired MemberRepository memberRepository;

	@Test
	@DisplayName("토큰을 성공적으로 생성 합니다.")
	void createToken() {


		// given
		Member member = Member.builder()
				.platformId("test@test.com")
				.platform(Platform.google)
				.picture("test")
				.role(Role.ROLE_USER)
				.username("test")
				.build();
		Member savedMember = memberRepository.save(member);

		// when
		String token = jwtProvider.createToken(savedMember);

		//then
		assertNotNull(token);
	}
}