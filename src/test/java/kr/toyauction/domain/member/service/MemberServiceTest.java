package kr.toyauction.domain.member.service;

import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.entity.Platform;
import kr.toyauction.domain.member.entity.Role;
import kr.toyauction.domain.member.repository.MemberRepository;
import kr.toyauction.global.exception.DomainNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@ExtendWith({MockitoExtension.class})
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService memberService;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository);
    }

    @Test
    @DisplayName("username 으로 유저 정보 조회")
    void getMemberByUsername(){
        // given
        String username = "test";

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username(username)
                .build();

        memberRepository.save(member);

        // when
        Member memberByUsername = memberService.getMemberByUsername(username);

        // then
        assertNotNull(memberByUsername.getId());
        assertEquals(member.getPlatformId(),memberByUsername.getPlatformId());
        assertEquals(member.getPlatform(),memberByUsername.getPlatform());
        assertEquals(member.getPicture(),memberByUsername.getPicture());
        assertEquals(member.getRole(),memberByUsername.getRole());
        assertEquals(member.getUsername(),memberByUsername.getUsername());
        assertEquals(member.getCreateDatetime(),memberByUsername.getCreateDatetime());
        assertEquals(member.getUpdateDatetime(),memberByUsername.getUpdateDatetime());
    }

    @Test
    @DisplayName("username 의 유저 정보가 존재 하지 않을 때")
    void getMemberByUsernameNull(){
        // given
        String username = "test";

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("testNull")
                .build();

        memberRepository.save(member);

        // when
        assertThrows(DomainNotFoundException.class, () -> memberService.getMemberByUsername(username));
    }
}
